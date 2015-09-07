package com.example.helloandroid;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "channel_category")
public class ChannelCatMap implements TableBase {

    @DatabaseField(generatedId = true)
    int mapId;
    @DatabaseField(uniqueCombo = true)
    int channelId;
    @DatabaseField(uniqueCombo = true)
    int catId;

    //@DatabaseField(foreign = true, foreignAutoRefresh = true)
    //Channel channel;

    public ChannelCatMap() {
    }

    public ChannelCatMap(int channelId, int catId) {
        this.channelId = channelId;
        this.catId = catId;
    }

    public ChannelCatMap(int mapId, int channelId, int catId) {
        this.mapId = mapId;
        this.channelId = channelId;
        this.catId = catId;
    }
}
