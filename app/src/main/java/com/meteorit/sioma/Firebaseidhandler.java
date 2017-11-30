package com.meteorit.sioma;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Minami on 17/11/2017.
 */

public class Firebaseidhandler extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Config.token=FirebaseInstanceId.getInstance().getToken();
    }
}
