package org.telemedicine.Web;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by bhavana on 8/5/16.
 */
public class Data {


    private String baseUrl;
    private int urlId;
    private boolean isCacheEnable;
    private boolean showProgres;
    private JSONObject params;
    private boolean network_status = true;
    private boolean isGET;
    private boolean isSSLSecured = false;
    private boolean isSecured = false;
    private JSONArray respoanse;
    private boolean isRawPayload=false;
    private String payload;

    public String getPayload() {
        return payload;
    }

    public Data setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public Data setGET(boolean GET) {
        isGET = GET;
        return this;
    }

    public boolean isRawPayload() {
        return isRawPayload;
    }

    public Data setRawPayload(boolean rawPayload) {
        isRawPayload = rawPayload;
        return this;
    }

    public boolean isSecured() {
        return isSecured;
    }

    public void setSecured(boolean secured) {
        isSecured = secured;
    }

    public boolean isSSLSecured() {
        return isSSLSecured;
    }

    public void setSSLSecured(boolean SSLSecured) {
        isSSLSecured = SSLSecured;
    }

    public String getBaseUrl() {

        return baseUrl;
    }

    public boolean isNetwork_status() {
        return network_status;
    }

    public void setNetwork_status(boolean network_status) {
        this.network_status = network_status;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getUrlId() {
        return urlId;
    }

    public void setUrlId(int urlId) {
        this.urlId = urlId;
    }

    public boolean isCacheEnable() {
        return isCacheEnable;
    }

    public void setCacheEnable(boolean isCacheEnable) {
        this.isCacheEnable = isCacheEnable;
    }

    public boolean isShowProgres() {
        return showProgres;
    }

    public void setShowProgres(boolean showProgres) {
        this.showProgres = showProgres;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public JSONArray getRespoanse() {
        return respoanse;
    }

    public void setRespoanse(JSONArray respoanse) {
        this.respoanse = respoanse;
    }

    public boolean isGET() {
        return isGET;
    }

    public void setIsGET(boolean isGET) {
        this.isGET = isGET;
    }


}


