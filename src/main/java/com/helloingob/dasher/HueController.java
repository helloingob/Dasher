package com.helloingob.dasher;

import java.util.List;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLightState;

public class HueController {

    private String username;
    private String ip;
    private PHHueSDK phHueSDK;
    private PHSDKListener listener = new PHSDKListener() {

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPointsList) {
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        }

        @Override
        public void onBridgeConnected(PHBridge bridge, String username) {
            //System.out.println("Connected to bridge.");
            phHueSDK.setSelectedBridge(bridge);
            phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);
        }

        @Override
        public void onCacheUpdated(List<Integer> arg0, PHBridge arg1) {
        }

        @Override
        public void onConnectionLost(PHAccessPoint arg0) {
        }

        @Override
        public void onConnectionResumed(PHBridge arg0) {
        }

        @Override
        public void onError(int code, final String message) {
            System.out.println("Code #" + code + ": " + message);
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
            for (PHHueParsingError parsingError : parsingErrorsList) {
                System.out.println("ParsingError : " + parsingError.getMessage());
            }
        }
    };

    public HueController(String username, String ip) {
        phHueSDK = PHHueSDK.create();
        phHueSDK.getNotificationManager().registerSDKListener(this.getListener());
    }

    public boolean connectToLastKnownAccessPoint() {
        PHAccessPoint accessPoint = new PHAccessPoint();
        accessPoint.setIpAddress(ip);
        accessPoint.setUsername(username);
        phHueSDK.connect(accessPoint);
        return true;
    }

    /** This is for MY lightbulb configuration! */
    public void setRed() {
        PHLightState lightState = new PHLightState();
        float xy[] = PHUtilities.calculateXYFromRGB(255, 0, 0, "LCT001");
        lightState.setX(xy[0]);
        lightState.setY(xy[1]);
        PHBridge pHBridge = phHueSDK.getSelectedBridge();
        pHBridge.updateLightState("1", lightState, null);
        pHBridge.updateLightState("3", lightState, null);
        phHueSDK.disableHeartbeat(pHBridge);
        phHueSDK.disconnect(pHBridge);
    }

    public PHSDKListener getListener() {
        return listener;
    }

}
