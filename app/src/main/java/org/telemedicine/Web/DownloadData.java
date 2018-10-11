//package org.telemedicine.Web;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.telemedicine.DbHelper;
//import org.telemedicine.model.Patient;
//
///**
// * Created by Naveen.k on 9/2/2016.
// */
//public class DownloadData implements JsonResult {
//    Context context;
//    DbHelper dbHelper;
//
//    public DownloadData(Context context) {
//        this.context = context;
//        dbHelper = DbHelper.getInstance(context);
//    }
//
//    public void downlaodrecords() {
//        getDataFromWS();
//    }
//
//    public String getUserId() {
//        return getProfileInfoDB("userId");
//    }
//
//    public String getRole() {
//        return getProfileInfoDB("role");
//    }
//
//    public String getUserName() {
//        return getProfileInfoDB("firstName") + " " + getProfileInfoDB("lastName");
//    }
//
//    public String getProfile() {
//        return getProfileInfoDB("userId");
//    }
//
//    private String getProfileInfoDB(String key) {
//        Cursor profileCursor = dbHelper.getProfile();
//        try {
//            while (profileCursor.moveToNext()) {
//                return profileCursor.getString(profileCursor.getColumnIndex(key));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//        return null;
//    }
//
//
//    private void getDataFromWS() {
//        HttpAgent httpAgent = new HttpAgent(context);
//        Data data = new Data();
//        //data.setBaseUrl(UrlList.login_url + user+"&pwd="+pwd+"&role="+StoreValues.role);
//        data.setBaseUrl(String.format(UrlList.GET_PATIENTS_URL, getUserId(), getRole().toUpperCase()));
//        data.setUrlId(UrlList.GET_PATIENTS_URL_ID);
//        data.setIsGET(true);
//        data.setParams(new JSONObject());
//        httpAgent.executeWs(data);
//    }
//
//    public String getDataFromJsonObj(JSONObject jsonObject, String key) {
//        try {
//            Log.d("keyvalue" + key, "jsonobject" + jsonObject);
//            return jsonObject.has(key) ? jsonObject.getString(key) : "";
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public String getGenderCode(String value) {
//        switch (value.toLowerCase()) {
//            case "male":
//                return "M";
//            case "female":
//                return "F";
//            case "a":
//                break;
//        }
//        return "";
//    }
//
//    public String getGender(String value) {
//        switch (value) {
//            case "M":
//                return "Male";
//            case "F":
//                return "Female";
//            case "a":
//                break;
//        }
//        return "";
//    }
//
//
//    @Override
//    public void getData(JSONArray response, int urlID) throws JSONException {
//        if (urlID == UrlList.GET_PATIENTS_URL_ID) {
//            JSONObject resultJson = response.getJSONObject(0);
//            JSONArray patientsArray = resultJson.has("patients") ? resultJson.getJSONArray("patients") : new JSONArray();
//            for (int i = 0; i < patientsArray.length(); i++) {
//                JSONObject patientJson = patientsArray.getJSONObject(i);
//                dbHelper.insertPatientDetails(new Patient(getDataFromJsonObj(patientJson, "fname"), getDataFromJsonObj(patientJson, "lname"), getDataFromJsonObj(patientJson, "age"), getDataFromJsonObj(patientJson, "pnumber"),
//                        getDataFromJsonObj(patientJson, "email"), getDataFromJsonObj(patientJson, "addr1"), getDataFromJsonObj(patientJson, "addr2"),
//                        getDataFromJsonObj(patientJson, "city"), getDataFromJsonObj(patientJson, "state"), getGender(getDataFromJsonObj(patientJson, "gender")), "", getDataFromJsonObj(patientJson, "pnumber")).withPoc(getDataFromJsonObj(patientJson, "patient_poc")));
//
//            }
//            patientsList.clear();
//            patientsList.addAll(dbHelper.getPatientDetails(0));
//            onItemsLoadComplete();
//            // Toast.makeText(HomeActivity.this, "Updated", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}
