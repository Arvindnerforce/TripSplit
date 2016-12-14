package com.netforceinfotech.tripsplit.group.mygroup;

/**
 * Created by John on 8/29/2016.
 */
public class MyData {

    public String imageUrl, groupName, senderName, message, time, group_id;

    public MyData(String imageUrl, String groupName, String senderName, String message, String time, String group_id) {
        this.imageUrl = imageUrl;
        this.groupName = groupName;
        this.senderName = senderName;
        this.message = message;
        this.time = time;
        this.group_id = group_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyData)) {
            return false;
        }

        MyData that = (MyData) obj;
        return this.group_id.equals(that.group_id);
    }
}
