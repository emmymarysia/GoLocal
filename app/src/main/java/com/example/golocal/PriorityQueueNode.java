package com.example.golocal;

import android.util.Log;

import com.example.golocal.models.GuideDataModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class PriorityQueueNode {
    private GuideDataModel guideDataModel;
    private int priority;
    private final String KEY_LOCATION = "location";
    private final String KEY_FRIENDS = "friends";

    public PriorityQueueNode(GuideDataModel guideDataModel) {
        this.guideDataModel = guideDataModel;
        assignPriority();
    }

    private void assignPriority() {
        // TODO: add guide creation to the priority function
        this.priority = 0;
        String guideLocation = this.guideDataModel.getLocation();
        ParseUser guideAuthor = this.guideDataModel.getAuthor();
        String guideAuthorUsername = guideAuthor.getUsername();
        Date createdAt = this.guideDataModel.getCreatedAt();
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userLocation = currentUser.getString(KEY_LOCATION);
        List<ParseUser> friends = currentUser.getList(KEY_FRIENDS);
        if (friends != null) {
            for (ParseUser friend: friends) {
                String friendUsername = "";
                try {
                    friendUsername = friend.fetchIfNeeded().getString("username");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (guideAuthorUsername.equals(friendUsername)) {
                    priority += 2;
                }
            }
        }
        if (userLocation.equals(guideLocation)) {
            priority += 2;
        }
    }

    public int getPriority() {
        return this.priority;
    }

    public GuideDataModel getGuideDataModel() {
        return this.guideDataModel;
    }
}
