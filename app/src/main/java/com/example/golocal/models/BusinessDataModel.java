package com.example.golocal.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Business")
public class BusinessDataModel extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FOURSQUAREID = "foursquareID";
    private final String TAG = "BusinessDataModel";

    public String getName() {
        try {
            return fetchIfNeeded().getString("name");
        } catch (ParseException e) {
            Log.e(TAG, "Couldn't get name", e);
        }
        return "";
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getFoursquareId() {
        return getString(KEY_FOURSQUAREID);
    }

    public void setFoursquareId(String id) {
        put(KEY_FOURSQUAREID, id);
    }
}
