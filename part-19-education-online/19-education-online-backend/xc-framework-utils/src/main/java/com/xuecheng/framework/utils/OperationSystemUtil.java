package com.xuecheng.framework.utils;

public class OperationSystemUtil {

    public static final String MAC = "MAC";

    public static final String WINDOWS = "WINDOWS";

    public static final String LINUX = "LINUX";

    public static String getOperationSystemType() {
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        if (osName.startsWith("Mac OS")) {
            // 苹果
            return MAC;
        } else if (osName.startsWith("Windows")) {
            // windows
            return WINDOWS;
        } else {
            // unix or linux
            return LINUX;
        }
    }

    public static void main(String[] args) {
        String operationSystemType = getOperationSystemType();
        System.out.println(operationSystemType);
    }

}
