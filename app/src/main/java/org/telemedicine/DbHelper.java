package org.telemedicine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.telemedicine.model.Patient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by bhavana on 8/6/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    Context ctx;


    private static String dbPath = "/data/data/org.telemedicine/databases/";
    public static final String DATABASE_NAME = "telemedicine.db";
    public static final String PATIENT_TABLE = "Patients";
    public static final String PROFILE_INFO_TABLE = "ProfileInfo";
    public static final String IMAGE_PATIENT="patientImage";
    private static DbHelper mInstance = null;
    private final Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    String seq_str;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public static DbHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DbHelper(ctx.getApplicationContext());
            try {
                mInstance.createDataBase();
                mInstance.openDataBase();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mInstance;
    }

    private boolean checkDataBase() {
        File dbFile = new File(dbPath + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        try {

            InputStream input = context.getAssets().open(DATABASE_NAME);
            String outPutFileName = dbPath + DATABASE_NAME;
            OutputStream output = new FileOutputStream(outPutFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            Log.v("error", e.toString());
        }
    }


    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public void openDataBase() throws SQLException, IOException {
        String fullDbPath = dbPath + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(fullDbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //altering of table  and any deletion logic goes here

    }

    public boolean insertProfile(String userId, String password, String firstName, String lastName, String mobile, String email, String role, String location, String complaints) {
        ContentValues cv = new ContentValues();
        cv.put("userId", userId);
        cv.put("password", password);
        cv.put("firstName", firstName);
        cv.put("lastName", lastName);
        cv.put("mobile", mobile);
        cv.put("email", email);
        cv.put("role", role);
        cv.put("locations", location);
        cv.put("complaints",complaints);
        return db.insert(PROFILE_INFO_TABLE, null, cv) != 0;
    }



    public Cursor getProfile() {
        return db.rawQuery("select * from " + PROFILE_INFO_TABLE, null);
    }

    public boolean isProfileExist(String value) {
        return (db.rawQuery(String.format("select * from %s where userId='%s' or mobile='%s'", PROFILE_INFO_TABLE, value, value), null)).getCount() != 0;
    }

    public void clearProfile() {
        db.delete(PROFILE_INFO_TABLE, null, null);
    }



    public Cursor getpatientsImage(String serverPatientId){
//SELECT profilePic FROM Patients where serverPatientId=6757656757
        //db.rawQuery(String.format("select profilePic from ",PATIENT_TABLE," where serverPatientId='?'",id));
        return db.rawQuery(String.format("select * from %s where serverPatientId='%s'", PATIENT_TABLE, serverPatientId), null);
        //return db.rawQuery("select * from " + PATIENT_TABLE, null);
    }

    public void clearPatients() {
        db.delete(PATIENT_TABLE, null, null);
    }

    public boolean insertPatientDetails(Patient patient) {
        ContentValues cv = new ContentValues();
        if (!isPatientExist(patient.getPatientServerId())) {
            cv.put("firstName", patient.getFirstName());
            cv.put("lastName", patient.getLastName());
            cv.put("age", patient.getAge());
            cv.put("mobile", patient.getPhoneNo());
            cv.put("email", patient.getEmailId());
//            Log.d("database","patientgender"+patient.getGender());
            cv.put("gender", patient.getGender());
            cv.put("address1", patient.getAddress1());
            cv.put("address2", patient.getAddress2());
            cv.put("town", patient.getTown());
            cv.put("state", patient.getState());
            cv.put("profilePic", patient.getPhoto());
            cv.put("serverPatientId", patient.getPatientServerId());

            return db.insert(PATIENT_TABLE, null, cv) != 0;
        } else {
            cv.put("poc", patient.getPoc());
            cv.put("lastUpdatedTime",patient.getLastupdated());
            cv.put("valid",patient.getValid_date());
            cv.put("renewal",patient.getRenewal_date());

            return db.update(PATIENT_TABLE, cv, "serverPatientId='" + patient.getPatientServerId() + "'", null) != 0;
        }
    }

    public boolean updatePatient(Patient patient,String serverid){
        ContentValues cv = new ContentValues();
        cv.put("firstName", patient.getFirstName());
        cv.put("lastName", patient.getLastName());
        cv.put("age", patient.getAge());
        cv.put("mobile", patient.getPhoneNo());
        cv.put("email", patient.getEmailId());

//        Log.d("database","patientgender"+patient.getGender());
        cv.put("gender", patient.getGender());
        cv.put("address1", patient.getAddress1());
        cv.put("address2", patient.getAddress2());
        cv.put("town", patient.getTown());
        cv.put("state", patient.getState());
        cv.put("profilePic", patient.getPhoto());
        cv.put("serverPatientId", patient.getPatientServerId());
        cv.put("lastUpdatedTime",patient.getLastupdated());
        cv.put("valid",patient.getValid_date());
        cv.put("renewal",patient.getRenewal_date());


        return db.update(PATIENT_TABLE, cv, "serverPatientId='" + serverid+ "'", null) != 0;
    }

    public boolean isPatientExist(String serverPatientId) {
        return db.rawQuery(String.format("select * from %s where serverPatientId='%s'", PATIENT_TABLE, serverPatientId), null).getCount() != 0;
    }


    public Cursor getstatePatient(String serverPatientId){

       return db.rawQuery("select  state,town  from " + PATIENT_TABLE + " where serverPatientId="+serverPatientId,null);

    }

    /**
     * If id is 0 return all patients
     */
    public ArrayList<Patient> getPatientDetails(int id) {
        ArrayList<Patient> patientsList = new ArrayList<>();
        Cursor cursor;
        if (id == 0) {
            cursor = db.rawQuery("SELECT * FROM " + PATIENT_TABLE, null);
            Log.e("id", "zero");
        } else {
            cursor = db.rawQuery("SELECT * FROM " + PATIENT_TABLE + " where id='?'", new String[]{id + ""});
            Log.e("id", "Not zero");
        }
        while (cursor.moveToNext()) {
            patientsList.add(new Patient(cursor.getString(cursor.getColumnIndex("firstName")), cursor.getString(cursor.getColumnIndex("lastName")),
                    cursor.getString(cursor.getColumnIndex("age")), cursor.getString(cursor.getColumnIndex("mobile")),
                    cursor.getString(cursor.getColumnIndex("email")), cursor.getString(cursor.getColumnIndex("address1")), cursor.getString(cursor.getColumnIndex("address2")),
                    cursor.getString(cursor.getColumnIndex("town")), cursor.getString(cursor.getColumnIndex("state")), cursor.getString(cursor.getColumnIndex("gender")),
                    cursor.getBlob(cursor.getColumnIndex("profilePic")), cursor.getString(cursor.getColumnIndex("serverPatientId")),cursor.getString(cursor.getColumnIndex("lastUpdatedTime")),cursor.getString(cursor.getColumnIndex("valid")),cursor.getString(cursor.getColumnIndex("renewal"))).withPoc(cursor.getString(cursor.getColumnIndex("poc"))));
        }
        return patientsList;
    }


    public ArrayList<Patient> searchPatientDetails(String value) {
        ArrayList<Patient> patientsList = new ArrayList<>();
        Cursor cursor;

        if (value.trim().length() != 0)
            cursor = db.rawQuery("SELECT * FROM " + PATIENT_TABLE + " where mobile like '" + value + "%'  or firstName like '" + value + "%' or lastName like '" + value + "%' or serverPatientId='" + value +"%'",null);
            //  cursor = db.rawQuery( "select * from " +PATIENT_TABLE+ "where firstName = (value like '%" + value + "%')// OR (firstName = '" + value + "') OR (lastName = '" + value + "')",null);
        else
            cursor = db.rawQuery("SELECT * FROM " + PATIENT_TABLE, null);

        while (cursor.moveToNext()) {
            patientsList.add(new Patient(cursor.getString(cursor.getColumnIndex("firstName")), cursor.getString(cursor.getColumnIndex("lastName")),
                    cursor.getString(cursor.getColumnIndex("age")), cursor.getString(cursor.getColumnIndex("mobile")),
                    cursor.getString(cursor.getColumnIndex("email")), cursor.getString(cursor.getColumnIndex("address1")), cursor.getString(cursor.getColumnIndex("address2")),
                    cursor.getString(cursor.getColumnIndex("town")), cursor.getString(cursor.getColumnIndex("state")), cursor.getString(cursor.getColumnIndex("gender")),
                    cursor.getBlob(cursor.getColumnIndex("profilePic")), cursor.getString(cursor.getColumnIndex("serverPatientId")),cursor.getString(cursor.getColumnIndex("lastUpdatedTime")),cursor.getString(cursor.getColumnIndex("valid")),cursor.getString(cursor.getColumnIndex("renewal"))).withPoc(cursor.getString(cursor.getColumnIndex("poc"))));
        }
        return patientsList;
    }


//    public JSONObject getProfileDetails() {
//        ArrayList<Profile> profileList = new ArrayList<>();
//        JSONObject jsonObject=new JSONObject();
//        Cursor cursor;
//
//            cursor = db.rawQuery("SELECT * FROM " + PROFILE_INFO_TABLE,null);
//
//        while (cursor.moveToNext()) {
//            profileList.add(new Profile(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("userId")), cursor.getString(cursor.getColumnIndex("password")),
//                    cursor.getString(cursor.getColumnIndex("firstName")), cursor.getString(cursor.getColumnIndex("lastName")),
//                    cursor.getString(cursor.getColumnIndex("mobile")), cursor.getString(cursor.getColumnIndex("email")), cursor.getString(cursor.getColumnIndex("role")),
//                    cursor.getString(cursor.getColumnIndex("locations"))));
//            Log.d("dbhelper","getpatientgender===dbpatient=="+profileList.toString());
//        }
//        try {
//            jsonObject.put("firstname",profileList.get(0).getFirstName());
//            jsonObject.put("email",profileList.get(0).getEmail());
//            jsonObject.put("userid",profileList.get(0).getUserId());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }




}
