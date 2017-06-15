package com.capstone.jobapplication.jobbridge;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;



/**
 * Created by Moon on 2017-06-13.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //폰정보 가저오기 그리고 폰번호정보가 없는 경우(유심없는폰)의 처리 방법
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phone = "";
        //유심카드가 있는지를 체크해서 분기함
        if (tMgr.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            //the phone has a sim card
            phone = tMgr.getLine1Number();
        } else {
            //베가 R3는 와이파이전용폰이라서 전화번호 대신 다른것을 집어 넣음
            phone = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        Log.d(TAG, "This Phone number:" + phone);

        sendRegistrationToServer(refreshedToken, phone);


    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token, String phone) {
        // TODO: Implement this method to send token to your app server.

        //자신의 앱서버를 활용한 기능을 추가할 수 있다.
        //이 코드는 토큰을 자신의 앱서버 DB추가해서 푸시알림을 별로도 할 수 있도록 하기 위한 목적이다.
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("Phone", phone)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://yourServer/fcm/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}