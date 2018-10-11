package org.telemedicine.Web;

import org.telemedicine.Url;

import static org.telemedicine.Constants.*;

/**
 * Created by bhavana on 8/5/16.
 */
public interface UrlList {
   /*String ip = Url.getURL(PRODUCT_Http_SEVER);
    String webrtcIP = Url.getURL(PRODUCT_WEBRTC_SEVER);
    String wsIP = Url.getURL(PRODUCT_WS_SEVER);*/

  String ip = Url.getURL(TEST_Http_SEVER);
    String webrtcIP = Url.getURL(TEST_WEBRTC_SEVER);
    String wsIP = Url.getURL(TEST_WS_SEVER);

    String INITIATEWSURL = wsIP + "/attendance/?name=%s&practice=%s&fullname=%s";
    String JOIN_CALL = ip + "/join_call/%s/%s/%s";

    String login_url = ip + "/auth/?userid=%s&pwd=%s";
    int loginurl_id = 101;

    String forgot_pass_url = ip + "/forgotPass/?user_id=%s";
    int forgot_passurl_id = 128;

    String save_url_patient = ip + "/savepatient/";
    int saveurl_patient_id = 102;

    String update_url_patient = ip + "/updatepatient/";
    int updateurl_patient_id = 127;

    String GET_PATIENTS_URL = ip + "/patientslist/?user=%s&role=%s";
    int GET_PATIENTS_URL_ID = 103;

    String poc_url_patient = ip + "/patientprofile/?patientid=";
    int pocurl_patient_id = 112;

    String drug_url_patient = ip + "/investigationinfo/";
    int drugurl_patient_id = 113;

    String poc_save_url_patient = ip + "/pocsave/?";
    int pocsaveurl_patient_id = 114;

    String renew_url_patient = ip + "/renewpatient/?patientid=";
    int renewurl_patient_id = 115;

    String ONLINEUSERS = webrtcIP + "/online_attendants/";
    int online_users_id = 122;

    String NOTIFYURL = webrtcIP + "/notify_ws/%s/?";
    int NOTIFY_URL_ID = 123;

    String RESERVEROOMURL = webrtcIP + "/meeting/reserve_room/?duration=45";
    int RESERVE_ROOM_URL_ID = 124;


    String DISCONNECTURL = webrtcIP + "/notify_status/call/?";
    int DISCONNECT_URL_ID = 125;

    String NOTIFYSTATUSURL = webrtcIP + "/notify_status/call/?";
    int NOTIFY_STATUS_URL_ID = 126;
String consultationlisturl=ip+"/getpoc_by_pharmacistid/?from=%s&to=%s&pharmacist_id=%s";
    //http://10.10.10.124:8888/getpoc_by_pharmacistid/?from=1490086102541&to=1490086102541&pharmacist_id=ph7207528943
 int consiltationurl_id=127;
 String sendmail_url=ip+"/pharmareportmail/?from=%s&to=%s&pharmacist_id=%s";
 int sendmail_url_id=129;
}