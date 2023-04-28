package mx.linkom.caseta_los_cabos;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "TOKEN";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Global.TOKEN = FirebaseInstanceId.getInstance().getToken();
    }

}