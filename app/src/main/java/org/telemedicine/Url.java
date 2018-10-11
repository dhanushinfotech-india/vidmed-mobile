package org.telemedicine;

import static org.telemedicine.Constants.*;

/**
 * Created by Naveen.k on 10/20/2016.
 */

public enum Url {
   /* Testing_Https_Server("http", "202.153.34.169", "8888"),
    Testing_Ws_Server("wss", "202.153.34.169", "8002"),
    Testing_webrtc_Server("https", "202.153.34.169", "8002"),*/
   Testing_Https_Server("https", "202.153.34.169", "8010"),
    Testing_Ws_Server("wss", "202.153.34.169", "8002"),
    Testing_webrtc_Server("https", "202.153.34.169", "8002"),

    //("https", "202.153.34.169", "8010")
    //("http", "10.10.11.6", "8880")
    //http://202.153.34.169:8888
    //    Production_Ws_Server("wss", "103.15.74.24", "8002"),
    //   Production_Https_Server("http", "10.10.10.124", "8888"), http://202.153.34.169:8888
  //  Production_Https_Server("http", "202.153.34.169", "8888"),

      // Production_Https_Server("http", "10.10.10.124", "8888"),

    Production_Https_Server("https", "pharma.vidmed.in", "443"),
    Production_Ws_Server("ws", "103.15.74.24", "8001"),
    Production_webrtc_Server("https", "pharma.vidmed.in", "8002");





    String protocol, ip, port;

    Url(String protocol, String ip, String port) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
    }

    public static String getURL(int serverType) {
        Url _url = getType(serverType);
        return _url.protocol + "://" + _url.ip + ":" + _url.port;
    }

    public static Url getType(int serverType) {
        switch (serverType) {
            case TEST_Http_SEVER:
                return Url.Testing_Https_Server;
            case TEST_WS_SEVER:
                return Url.Testing_Ws_Server;
            case TEST_WEBRTC_SEVER:
                return Url.Testing_webrtc_Server;
            case PRODUCT_Http_SEVER:
                return Url.Production_Https_Server;
            case PRODUCT_WS_SEVER:
                return Url.Production_Ws_Server;
            case PRODUCT_WEBRTC_SEVER:
                return Url.Production_webrtc_Server;
        }
        return null;
    }
}
