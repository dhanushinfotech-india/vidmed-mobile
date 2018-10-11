package org.telemedicine.Web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.MyApplication;
import org.telemedicine.PatientInfoActivity;

import java.net.URI;
import java.util.Map;

public class WebSocketExampleClient extends WebSocketClient {
    Context context;
    public static boolean isRunning = false;

    public WebSocketExampleClient(Context context, URI serverUri, Draft draft, Map<String, String> headers, int timeout) {
        super(serverUri, draft, headers, timeout);
        Log.d("websocket", "construct"+serverUri);
        this.context = context;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("websocket", "open");
        isRunning = true;

    }

    @Override
    public void onMessage(String message) {

        final String msg = message;
        Log.d("websocketuserwebsocketid", "websocketuserwebsocketid "+msg);
        //Handle this message
        if (getDataFromJson(message, "id") != null) {

            PatientInfoActivity.userWSID = getDataFromJson(message, "id");
        } else {
            if (context == null) {
                context = MyApplication.getAppContext();
            }
            message = getDataFromJson(message, "info");
            String status = getDataFromJson(message, "status");
//        String mettingURL = getDataFromJson(message, "meetingURL");
            String callerName = getDataFromJson(message, "callerName");
            String receiverName = getDataFromJson(message, "receiverName");
            String callerWSID = getDataFromJson(message, "callerWSID");
            String patientID = getDataFromJson(message, "patientID");
            Log.d("Status_fromcoket", "busy"+status);
            switch (status) {
//                case "no_answer":
//                    Log.e("Stat", "No Answer");
//                    context.startActivity(new Intent(context, PatientInfoActivity.class).putExtra("isNewRegister", false).putExtra("patientId", patientID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    break;

               /* case "reject":

                    stopThreadAndDialog();*/
                  //  Toast.makeText(contextthis,"doctor busy",Toast.LENGTH_SHORT).show();

//                    PatientInfoActivity.ft.stop();
                  //  Log.e("Stat", "busy");
//                context.startActivity(new Intent(context, PatientInfoActivity.class).putExtra("isNewRegister", false).putExtra("patientId", patientID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                   // break;

//            case "disconnect":
//                if (NewActionActivity.ringtone != null && NewActionActivity.ringtone.isPlaying())
//                    NewActionActivity.ringtone.stop();
//                context.startActivity(new Intent(context, HomeActivity.class).putExtra("isNewRegister", false).putExtra("patientId", patientID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                break;
                case "answer":
                    stopThreadAndDialog("");
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(UrlList.JOIN_CALL, "pharma", receiverName, patientID))).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    Log.d("join_call url","="+String.format(UrlList.JOIN_CALL, "pharma", receiverName, patientID));
                    break;
//            case "call":
//                //receiver side
//                Log.e("recciver msg", message);
//                if (!NewActionActivity.isBusy)
//                    context.startActivity(new Intent(context, NewActionActivity.class).putExtra("meetingURL", mettingURL).putExtra("callerName", callerName).putExtra("callerWSID", callerWSID).putExtra("patientID", patientID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                break;
                default:
                    stopThreadAndDialog(status);
                    break;
            }
        }
    }

    private void stopThreadAndDialog(String status) {
//        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        if (PatientInfoActivity.callProgressDialog != null && PatientInfoActivity.callProgressDialog.isShowing()) {
            PatientInfoActivity.callProgressDialog.dismiss();


        }
        PatientInfoActivity.sendStatus(context, status);
        if (PatientInfoActivity.ft.isAlive())
            PatientInfoActivity.ft.interrupt();

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("websocket", "closed");
        isRunning = false;
        PatientInfoActivity.userWSID = null;
    }


    private String getDataFromJson(String jsonStr, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject.has(key) ? jsonObject.getString(key) : null;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("unable convert WS", jsonStr);
        }
        return null;
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}