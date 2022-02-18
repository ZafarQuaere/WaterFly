package com.waterfly.user.data.local.db;

import androidx.lifecycle.LiveData;
import com.waterfly.user.data.network.model.UserDetails;
import java.util.List;


public interface DbHelper {

    LiveData<List<UserDetails>> getAllUsers();

     void saveUserDetails(List<UserDetails> userDetailsList);

}
