package com.waterfly.user.data.local.db;

import androidx.lifecycle.LiveData;
import com.waterfly.user.data.local.db.database.Database;
import com.waterfly.user.data.network.model.UserDetails;

import java.util.List;

public class AppDbHelper implements DbHelper {


    private static AppDbHelper sInstance;


    public static synchronized AppDbHelper getInstance() {
        if (sInstance == null) {
            sInstance = new AppDbHelper();
        }
        return sInstance;
    }


    // Get the task list and set the task list for the adapter


    @Override
    public LiveData<List<UserDetails>> getAllUsers() {
        return Database.getInstance().logDao().getAll();
    }

    @Override
    public void saveUserDetails(List<UserDetails> userDetailsList) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Database.getInstance().logDao().insertAll(userDetailsList);

            }
        };
        thread.start();


    }


}
