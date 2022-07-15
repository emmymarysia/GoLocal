package com.example.golocal;

import com.example.golocal.models.GuideDataModel;
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
        // compare user location, if it's written by a friend, and if it was published recently
        this.priority = 0;
        String guideLocation = this.guideDataModel.getLocation();
        ParseUser guideAuthor = this.guideDataModel.getAuthor();
        String guideAuthorUsername = guideAuthor.getUsername();
        Date createdAt = this.guideDataModel.getCreatedAt();
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userLocation = currentUser.getString(KEY_LOCATION);
        List<ParseUser> friends = currentUser.getList(KEY_FRIENDS);
        for (ParseUser friend: friends) {
            if (guideAuthorUsername.equals(friend.getUsername())) {
                priority += 2;
            }
        }
        if (userLocation.equals(guideLocation)) {
            priority += 2;
        }
    }

    public int getPriority() {
        return this.priority;
    }
}
