# ebarimt-java-server
A java web server for ebarimt's VAT bill on Linux OS.

## Initializing Shared Library JNI

Download library files from https://ebarimt.mn/posapi for Linux OS.

Extract PosAPI_2.1_libs_Linux.zip.
```console
$./install.sh #x64 copies all so library files to /usr/libs and creates links.
```

I tried on Centos 7, java 1.8. So it requires to install few packages, if you're using other linux distributors it will also requires packages shown below: 
```console
$yum group install "Development Tools" #gcc, g++ compiler but its older version
$yum install devtoolset-9 #g++ higher version(mine was 9) which supports c++11 
$yum install java-1.8.0-openjdk-devel #for JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk/
$yum install libsqlite3x-devel.x86_64 #installing sqlite3
$yum install centos-release-scl #enabling g++'s latest version
$echo "source /opt/rh/devtoolset-9/enable" >> /etc/bashrc
$export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib #adding path
$yum install prelink
```
Need to copy libPosAPI.so file to /usr/lib folder.

Open init folder(there is a dummy libPosAPI.so) and run cmd.sh file like $./cmd.sh or $sh cmd.sh

Description of cmd.sh file:

1) Compiling and linking Bridge library
```console
$g++ -Wall -std=c++11 -c -fPIC -I/usr/lib/jvm/java-1.8.0-openjdk/include -I/usr/lib/jvm/java-1.8.0-openjdk/include/linux com_github_uugan_ebarimt_BridgePosAPI.cpp -o com_github_uugan_ebarimt_BridgePosAPI.o
```
2) G++ linker then links the C++ object files into our bridged library.
```console
$g++ -std=c++11 -fPIC -shared -lPosAPI -L/usr/lib -o libBridgePosAPI.so com_github_uugan_ebarimt_BridgePosAPI.o -lc
$cp -r libBridgePosAPI.so /usr/lib/libBridgePosAPI.so 
```
3) Testing bridge library:
```console
$javac -d . BridgePosAPI.java && java -Djava.library.path="/usr/lib" com.github.uugan.ebarimt.BridgePosAPI
getInformation = {
    "branchNo": "",
    "dbDirPath": "/root/.vatps",
    "posId": "",
    "registerNo": ""
}
checkAPI = {
    "config": {
        "success": true
    },
    "database": {
        "success": true
    },
    "network": {
        "success": true
    },
    "success": true
}
result = {
    "success": true
}

```
## Run web server

```console
mvn package
```
### Run server:
```console
java -Dport=8080 -Dlog4j.configuration=file:./logger.xml -Djava.library.path="/usr/lib" -jar ebarimt-java-server-1.0.jar
```
### API paths are:

- GET localhost:8080/info
- GET localhost:8080/check
- GET localhost:8080/senddata
- POST localhost:8080/put
- POST localhost:8080/return

POST requests JSON bodies are must be same as in [documentation](https://ebarimt.mn/img/Pos%20API%202.1.2%20User%20Guide_mn.pdf).
Also you can use ebarimt-java-client for generating json request.
## TODOs:
- Create scheduled job for calling senddata every 10 minutes.
