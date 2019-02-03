package com.helloingob.dasher;

public class DasherMain {

    private static final String MAC = "AC:63:BE:5:D:XX";
    private static final String KODI_EXECUTABLE = "C:\\Program Files (x86)\\Kodi\\Kodi.exe";
    private static final String HUE_USER = "helloingob";
    private static final String HUE_IP = "localhost";

    public static void main(String[] args) {
        JNetpCapHandler jNetpCapHandler = new JNetpCapHandler();
        // List all interfaces
        jNetpCapHandler.getInterfaces();
        // Select interface (mine is #4)
        jNetpCapHandler.openDevice(4);
        // Add listener for arp packet with given mac
        jNetpCapHandler.addPacketHandler(MAC, HUE_IP, HUE_USER, KODI_EXECUTABLE);
    }

}
