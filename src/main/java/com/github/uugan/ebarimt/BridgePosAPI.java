package com.github.uugan.ebarimt;
/**
 * @author Uugan
 * @since 1.0
 */
public class BridgePosAPI {
    static {
        String os = System.getProperty("os.name");
        if (os.toUpperCase().contains("WINDOWS")) {
            System.loadLibrary("icudt53");
            System.loadLibrary("icuuc53");
            System.loadLibrary("icuin53");
            System.loadLibrary("Qt5Core");
            System.loadLibrary("Qt5Sql");
            System.loadLibrary("Qt5Network");
            System.loadLibrary("PosAPI");
        }
        System.loadLibrary("BridgePosAPI");
    }

    public static native String put(String data);

    public static native String returnBill(String data);

    public static native String sendData();

    public static native String checkApi();

    public static native String getInformation();

    public static native String callFunction(String funcName, String data);


}
