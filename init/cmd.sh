#!/bin/bash 
#set -x
g++ -Wall -std=c++11 -c -fPIC -I/usr/lib/jvm/java-1.8.0-openjdk/include -I/usr/lib/jvm/java-1.8.0-openjdk/include/linux com_github_uugan_ebarimt_BridgePosAPI.cpp -o com_github_uugan_ebarimt_BridgePosAPI.o && echo "compile shared library: OK" || echo "compile shared library: NOK"
g++ -std=c++11 -fPIC -shared -lPosAPI -L/usr/lib -o libBridgePosAPI.so com_github_uugan_ebarimt_BridgePosAPI.o -lc && echo "creating so file: OK" || echo "creating so file: NOK"
cp -r libBridgePosAPI.so /usr/lib/libBridgePosAPI.so && echo "copy so file to /usr/lib: OK" || echo "copy so file to /usr/lib: NOK"
javac -d . BridgePosAPI.java && java -Djava.library.path="/usr/lib" com.github.uugan.ebarimt.BridgePosAPI && echo "run java: OK" || echo "run java: NOK"

