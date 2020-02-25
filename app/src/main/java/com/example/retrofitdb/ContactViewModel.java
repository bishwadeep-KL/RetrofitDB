package com.example.retrofitdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    ContactRepository contactRepository;
    LiveData<List<Contact>> allContacts;
    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        allContacts  = contactRepository.getAllContacts();
    }

    public void insert(Contact contact){
        contactRepository.insert(contact);
    }
    public void delete(Contact contact){
        contactRepository.delete(contact);
    }

    public LiveData<List<Contact>> getAllContacts(){
        return allContacts;
    }
}
