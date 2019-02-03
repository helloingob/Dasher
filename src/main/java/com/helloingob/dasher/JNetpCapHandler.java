package com.helloingob.dasher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;

public class JNetpCapHandler {

    private StringBuilder errorBuffer;
    private List<PcapIf> allDevices;
    private PcapIf device;
    private Pcap pcap;

    public JNetpCapHandler() {
        errorBuffer = new StringBuilder();
        allDevices = new ArrayList<PcapIf>();
    }

    /** Get a list of devices on this system */
    public List<PcapIf> getInterfaces() {
        int r = Pcap.findAllDevs(allDevices, errorBuffer);
        if (r == Pcap.ERROR || r == Pcap.WARNING || allDevices.isEmpty()) {
            System.out.println(String.format("Can't read list of devices, error is %s", errorBuffer.toString()));
            return Collections.emptyList();
        }
        System.out.println("Network devices found:");
        int i = 0;
        for (PcapIf device : allDevices) {
            String description = (device.getDescription() != null) ? device.getDescription() : "No description available";
            System.out.println(String.format("#%d: %s [%s]", i++, device.getName(), description));
        }
        return allDevices;
    }

    public void openDevice(int number) {
        device = allDevices.get(number);
        System.out.println(String.format("Listening on => '%s'", (device.getDescription() != null) ? device.getDescription() : device.getName()));
        // Capture all packets, no trucation
        int snaplen = 64 * 1024;
        // Capture all packets
        int flags = Pcap.MODE_PROMISCUOUS;
        // 10 seconds in millis
        int timeout = 10 * 1000;
        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errorBuffer);
        if (pcap == null) {
            System.out.println(String.format("Error while opening device for capture: '%s'", errorBuffer.toString()));
            return;
        }
    }

    public void addPacketHandler(String macAddress, String hueIp, String hueUsername, String kodiPath) {
        HueController hueController = new HueController(hueUsername, hueIp);

        PcapPacketHandler<String> pcapPacketHandler = new PcapPacketHandler<String>() {
            // Preallocate a Arp header
            Arp arp = new Arp();
            // Preallocate a Ethernet header
            Ethernet ethernet = new Ethernet();

            @Override
            public void nextPacket(PcapPacket packet, String user) {
                if (packet.hasHeader(arp) && packet.hasHeader(ethernet) && FormatUtils.asString(ethernet.source()).equals(macAddress)) {
                    // System.out.println("PRESSED");
                    fadeToRed(hueController);
                    startKodi(kodiPath);
                }
            }
        };

        // Capture infinite packets.
        pcap.loop(Pcap.LOOP_INFINITE, pcapPacketHandler, "jNetPcap rocks!");
        // Close the pcap handle
        pcap.close();
    }

    private static Integer startKodi(String kodiPath) {
        CommandLine cmdLine = CommandLine.parse(kodiPath);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(1);
        Integer exitValue = null;
        try {
            exitValue = executor.execute(cmdLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exitValue;
    }

    private static void fadeToRed(HueController hueController) {
        hueController.connectToLastKnownAccessPoint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hueController.setRed();
    }

}
