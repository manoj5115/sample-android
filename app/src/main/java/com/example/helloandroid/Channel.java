package com.example.helloandroid;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "channel")
public class Channel implements TableBase{

    @DatabaseField(generatedId = true)
    int channelId;

    @DatabaseField(index = true)
    String channelName;

    public Channel() {
    }

    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public Channel(int channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }
}
