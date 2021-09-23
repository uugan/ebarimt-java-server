
package com.github.uugan.ebarimt;

public class BridgePosAPI {
    static {
        System.loadLibrary("BridgePosAPI");
    }

    public static native String put(String data);

    public static native String returnBill(String data);

    public static native String sendData();

    public static native String checkApi();

    public static native String getInformation();

    public static void main(String[] args) {

        try {
            BridgePosAPI posapi = new BridgePosAPI();
            String res_info = posapi.getInformation();
            System.out.println("getInformation = " + res_info);

            String res_check = posapi.checkApi();
            System.out.println("checkAPI = " + res_check);

            String result = posapi.sendData();
            System.out.println("result = " + result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
} 
