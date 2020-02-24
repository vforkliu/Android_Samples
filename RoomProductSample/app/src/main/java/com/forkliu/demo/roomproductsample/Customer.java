package com.forkliu.demo.roomproductsample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers")
public class Customer {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "customerId")
    private int id;
    @ColumnInfo(name = "customerName")
    private String name;
    private String address;

    public Customer(String name, String address) {
        // this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
