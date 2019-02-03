# Dasher

This is a poc of an [Amazon Dash button](https://en.wikipedia.org/wiki/Amazon_Dash) interceptor. If a button press is detected, an action is triggered.
A loop intercepts ARP requests and executes an external program and changes the light.

## Requirements
- [Java 1.8](https://java.com/de/download/)
- [Maven](https://maven.apache.org/download.cgi)
- Amazon Dash Button
- [Phillips Hue](www.meethue.com/)
- [Kodi](https://kodi.tv/)
- [Wireshark](https://www.wireshark.org/)
- [Npcap](https://nmap.org/npcap/#download)

## Important Note
This special build is for **WINDOWS 64bit**. It was developed on **Windows 10**. It works with every OS, but you need the specific [jNetPcap library](https://sourceforge.net/projects/jnetpcap/files/jnetpcap/Latest/) and [Npcap](https://nmap.org/npcap/#download) files.
  1) Copy the file [jnetpcap.dll](/lib/jNetPcap/1.4.r1425/jnetpcap.dll) to your "/Windows/System32" folder
  2) Install [Npcap](https://nmap.org/npcap/#download)

## Setup Dash Button
Setup Button with Amazon app, but cancel the last setup step with the top right "x"! (*Disable notification if you need*)

## Get Dash Button MAC address
1) Start Wireshark
2) Select your interface connected to the same network as the button
3) Write "arp" in filterbox
4) Press Dash Button
5) Look for the source "AmazonTe"
6) Note MAC adress down


![Wireshark ARP Request](/img/wireshark_arp.png?raw=true)

## Get Hue Settings
1) Clone [PhilipsHueSDK-DesktopApp](https://github.com/PhilipsHue/PhilipsHueSDK-Java-MultiPlatform-Android/tree/master/JavaDesktopApp)
2) Import as Java Project
3) Run
4) Setup up connection
5) Note down your ip & username from generated "MyHue.properties"

## Build
  - Edit **MAC**, **KODI_EXECUTABLE**, **HUE_IP** & **HUE_USER** in [DasherMain.java](/src/main/java/com/helloingob/dasher/DasherMain.java) and select your interface in line 15.
  - Run "**mvn clean package**"
  
## Live Demo
- Kodi & Chill: https://vimeo.com/186109134
