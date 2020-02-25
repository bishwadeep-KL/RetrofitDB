package com.example.retrofitdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {


    @Insert
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("select * from contact_table order by name asc")
    LiveData<List<Contact>> getAllContact();
}
