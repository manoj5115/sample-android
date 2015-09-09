package com.example.helloandroid;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "category")
public class Category implements TableBase{

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    int catId;

    @DatabaseField(index = true)
    String catName;

    public Category() {
    }

    public Category(String catName) {
        this.catName = catName;
    }

    public Category(int catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }
}
