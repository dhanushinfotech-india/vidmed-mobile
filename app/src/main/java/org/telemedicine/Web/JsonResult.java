package org.telemedicine.Web;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by bhavana on 8/5/16.
 */
public interface JsonResult {
    public void getData(JSONArray response, int urlID) throws JSONException;

}
