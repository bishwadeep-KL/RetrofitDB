package com.example.retrofitdb;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    private static Context activity;

    private static ContactDatabase instance;
    public abstract ContactDao contactDao();

    public static synchronized ContactDatabase getInstance(Context context){
        activity = context.getApplicationContext();
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),ContactDatabase.class, "contact_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
    return  instance;
    }

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAyncTask(instance).execute();
        }
    };

    public static class PopulateDbAyncTask extends AsyncTask<Void, Void, Void>{

        private ContactDao contactDao;
        private PopulateDbAyncTask(ContactDatabase db){
            contactDao = db.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.insert(new Contact("Bishwadeep","123456789"));
            contactDao.insert(new Contact("XYZ","918237473"));
            fillWithStartingData(activity);

            return null;
        }
    }

    private static  void fillWithStartingData(Context context){
        ContactDao dao=getInstance(context).contactDao();

        JSONArray contacts = loadJSONArray(context);
        try{
            for(int i=0;i<contacts.length();i++){
                JSONObject contact  = contacts.getJSONObject(i);
                String contactName = contact.getString("name");
                String contactNumber = contact.getString("phone");
                dao.insert(new Contact(contactName, contactNumber));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static JSONArray loadJSONArray(Context context){
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(R.raw.contact_list);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try{
            while((line=reader.readLine())!=null){
                builder.append(line);
            }

            JSONObject jsonObject = new JSONObject(builder.toString());
            return jsonObject.getJSONArray("contacts");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
