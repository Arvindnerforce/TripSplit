package com.netforceinfotech.tripsplit.group.groupchat;

/**
 * Created by John on 8/29/2016.
 */
public class MyData {
    String key, id, image_url, message, name;
    Long timestamp;

    public MyData(String key, String id, String image_url, String message, String name, Long timestamp) {
        this.key = key;
        this.id = id;
        this.image_url = image_url;
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
    }

    @Override

    public boolean equals(Object obj) {
        if (!(obj instanceof MyData)) {
            return false;
        }

        MyData that = (MyData) obj;
        return this.key.equals(that.key);
    }
}
