/*
package org.telemedicine;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private static final String VIDMED_BASE_URL = "VIDMED_BASE_URL";
    private static final String VIDMED_DJANGO_BASE_URL = "VIDMED_DJANGO_BASE_URL";
    private static final String HOST = "HOST";
    private static final String PORT = "PORT";
    private static final String SHOULD_VERIFY_CERTIFICATE = "SHOULD_VERIFY_CERTIFICATE";
    */
/* private static final String SYNC_DOWNLOAD_BATCH_SIZE = "SYNC_DOWNLOAD_BATCH_SIZE";
     private static final String DRISHTI_AUDIO_URL = "DRISHTI_AUDIO_URL";
     private static final String DRISHTI_VIDEO_URL = "DRISHTI_VIDEO_URL";
     private static final String DRISHTI_WEBSOCKET_URL = "DRISHTI_WEBSOCKET_URL";*//*

    private Properties properties = new Properties();
    SharedPreferences preferences;

    public Configuration(AssetManager assetManager, android.content.Context context) {
        try {
            properties.load(assetManager.open("app.properties"));
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String get(String key) {
        return properties.getProperty(key);
    }

   public String host() {
        return this.get(HOST);
    }

    public int port() {
        return Integer.parseInt(this.get(PORT));
    }

    public boolean shouldVerifyCertificate() {
        return Boolean.parseBoolean(this.get(SHOULD_VERIFY_CERTIFICATE));
    }

    public String dristhiBaseURL() {
        return !preferences.getString("prefBaseURL", "").equals("") ? "http://" + preferences.getString("prefBaseURL", "") + "/drishti-web-0.1-SNAPSHOT" : this.get(VIDMED_BASE_URL);
    }

     public String dristhiDjangoBaseURL() {
          return !preferences.getString("prefDjangoBaseURL", "").equals("") ? "http://" + preferences.getString("prefDjangoBaseURL", "") : this.get(VIDMED_DJANGO_BASE_URL);
      }

   */
/* public String drishtiVideoURL() {
        return (!preferences.getString("prefVideoURL", "").equals("") ? preferences.getString("prefVideoURL", "") : this.get(DRISHTI_VIDEO_URL));

    }*//*


  */
/*  public String drishtiWSURL() {

        return (!preferences.getString("prefWebSocketURL", "").equals("") ? preferences.getString("prefWebSocketURL", "") : this.get(DRISHTI_WEBSOCKET_URL));
    }
*//*


  */
/*  public String drishtiAudioURL() {
        return this.get(DRISHTI_AUDIO_URL);
    }*//*


   */
/* public int syncDownloadBatchSize() {
        return IntegerUtil.tryParse(this.get(SYNC_DOWNLOAD_BATCH_SIZE), 100);
    }*//*


   */
/* public String getClientAPPURL() {
        return !preferences.getString("prefClientURL", "").equals("") ? preferences.getString("prefClientURL", "") : "https://play.google.com/store/apps/details?id=org.mozilla.firefox";
    }

    public String getClientBrowserUrl() {
        return !preferences.getString("prefVideoBrowserURL", "").equals("") ? preferences.getString("prefVideoBrowserURL", "") : "org.mozilla.firefox";
    }

    public String getClientBrowserAPPUrl() {
        return !preferences.getString("prefVideoBrowserAppURL", "").equals("") ? preferences.getString("prefVideoBrowserAppURL", "") : "org.mozilla.firefox.App";
    }*//*

}

*/
