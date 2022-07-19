package com.example.golocal.models;

import com.example.golocal.models.BusinessDataModel;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Guide")
public class GuideDataModel extends ParseObject {

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LIKEDBY = "likedBy";
    public static final String KEY_BUSINESSLIST = "businessList";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION = "location";

    public GuideDataModel() {}

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public List<ParseUser> getLikedBy() {
        return getList(KEY_LIKEDBY);
    }

    public void setLikedBy(List<ParseUser> likedBy) {
        put(KEY_LIKEDBY, likedBy);
    }

    public List<BusinessDataModel> getBusinessList() {
        return getList(KEY_BUSINESSLIST);
    }

    public void setBusinessList(List<BusinessDataModel> businessDataModelList) {
        put(KEY_BUSINESSLIST, businessDataModelList);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getLocation() {
        return getString(KEY_DESCRIPTION);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION, location);
    }
}
