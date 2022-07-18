package com.example.golocal.models;

import com.parse.ParseUser;

import java.util.List;

public class UserDataModel extends ParseUser {
    public static final String KEY_FRIENDS = "friends";

    public List<ParseUser> getFriends() {
        return getList(KEY_FRIENDS);
    }
}
