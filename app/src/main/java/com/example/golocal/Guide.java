package com.example.golocal;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class Guide extends ParseObject {
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LIKEDBY = "likedBy";
    public static final String KEY_BUSINESSLIST = "businessList";

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

    public List<Business> getBusinessList() {
        return getList(KEY_BUSINESSLIST);
    }

    public void setBusinessList(List<Business> businessList) {
        put(KEY_BUSINESSLIST, businessList);
    }
}
