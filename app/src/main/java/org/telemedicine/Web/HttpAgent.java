package org.telemedicine.Web;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by bhavana on 8/5/16.
 */
public class HttpAgent extends AsyncTask<Data, Integer, Data> {

    private Context thisContext;

    private JsonResult mJsonResult;
    private ProgressDialog dialog;
    final String basicAuth = "Basic " + Base64.encodeToString("testuser1:hari135".getBytes(), Base64.NO_WRAP);

    public HttpAgent(Context mActivity, ProgressDialog _dialog) {
        this.thisContext = mActivity;
        this.dialog = _dialog;
        if (thisContext instanceof JsonResult)
            mJsonResult = (JsonResult) thisContext;
    }

    public HttpAgent(Context mActivity) {
        this.thisContext = mActivity;

        if (thisContext instanceof JsonResult)
            mJsonResult = (JsonResult) thisContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.dialog == null) {
            dialog = new ProgressDialog(thisContext, R.style.ProgressBar);
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            dialog.setMessage("Please wait....");
            dialog.show();
        } else {
            this.dialog.show();
        }
    }

    public HttpAgent(Context context, JsonResult mJsonResult) {
        this.thisContext = context;
        this.mJsonResult = mJsonResult;
    }

    public void executeWs(Data... params) {
        for (Data data : params) {
            HttpAgent mAsyncTaskData = new HttpAgent(thisContext,
                    mJsonResult);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mAsyncTaskData.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, data);
            } else {
                mAsyncTaskData.executeWs(data);
            }
        }
    }

    @Override
    protected Data doInBackground(Data... params) {
        for (Data data : params) {
            if (isOnline(thisContext)) {
                if (data.isRawPayload())
                    return postPayload(data);
                else
                    return postData(data);
            } else {
                data.setNetwork_status(false);
                return data;
            }
        }
        return null;
    }

    public Data postData(Data mData) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = null;
//        if (!mData.isSSLSecured())
//            httpclient = new DefaultHttpClient();
//        else
//            httpclient = getHttpsClient(new DefaultHttpClient());
        if (mData.getBaseUrl().contains("http://"))
            httpclient = new DefaultHttpClient();
        else if (mData.getBaseUrl().contains("https://"))
            httpclient = getHttpsClient(new DefaultHttpClient());
        HttpResponse response = null;
        try {
            if (mData.isGET()) {
                HttpGet httppost = new HttpGet(mData.getBaseUrl());
                Log.e("URL", mData.getBaseUrl());
                if (mData.isSecured()) {
                    Log.e("Secured", "Url" + mData.getBaseUrl());
                    httppost.setHeader("Authorization", basicAuth);

                }
                response = httpclient.execute(httppost);
            } else {
                Log.e("post URL", mData.getBaseUrl());
                HttpPost httpPost = new HttpPost(mData.getBaseUrl());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                JSONObject mJsonObject = mData.getParams();
                if (mJsonObject != null) {
                    Iterator<String> iter = mJsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            nameValuePairs.add(new BasicNameValuePair(key,
                                    mJsonObject.getString(key)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //Content-type=application/x-www-form-urlencoded
                //Accept=text/plain
                httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httpPost.setHeader("Accept", "text/plain");

                // Making HTTP Request
                response = httpclient.execute(httpPost);
            }
            Object JsonResult = EntityUtils.toString(response.getEntity());
            Log.e("response", "res------" + JsonResult);
            if (JsonResult instanceof JSONArray) {
                mData.setRespoanse((JSONArray) JsonResult);
            } else {
                JSONArray respoanse = new JSONArray("[" + JsonResult.toString()
                        + "]");
                mData.setRespoanse(respoanse);

            }
        } catch (Exception e) {
            mData.setNetwork_status(false);

            e.printStackTrace();
        }
        return mData;

    }

    @Override
    protected void onPostExecute(Data result) {
        super.onPostExecute(result);
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
        } catch (Exception e) {

        }

        if (result != null && result.isNetwork_status()) {
            try {
                if (mJsonResult != null)
                    mJsonResult.getData(result.getRespoanse(),
                            result.getUrlId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(thisContext, "Unable to reach server,Please try again later", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static HttpClient getHttpsClient(HttpClient client) {
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    public static Data postPayload(Data mData) {
        StringBuffer jsonString = null;
        try {
            URL url = new URL(mData.getBaseUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(mData.getPayload());
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mData.setRespoanse(new JSONArray().put(jsonString != null ? jsonString.toString() : ""));
        return mData;
    }

}
