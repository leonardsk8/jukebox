package ilioncorp.com.jukebox.model.notifications;

import android.os.Environment;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import ilioncorp.com.jukebox.model.dto.SessionVO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientNotificationViaFCMServerHelper {


    private static String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private final String AUTH_KEY = "AAAAkqmrD7A:APA91bE12xZ3Vk6wh6-yPHl8J3rKUG7d2fjwu2CN4KJJe-s-DNs2-qOztqrsQTdroihH0AzXerWkfCVzpIpBAHvE9NAwVWa98r6FKZV2mwNc6aLcYcXf-XND0XRaxapnYAzmV0DpQek4";;
    final MediaType mediaType = MediaType.parse("application/json");
    private String FCM_URL = "https://fcm.googleapis.com/v1/projects/dispositivo1-202101/messages:send";

    SessionVO session;
    String title;
    String body;

    public ClientNotificationViaFCMServerHelper(SessionVO session, String title, String body) {
        this.session = session;
        this.title = title;
        this.body = body;
    }

    public void sendMessage(){
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(mediaType, getValidJsonBody(body,title).toString());
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json; UTF-8")
                    .addHeader("Authorization","Bearer " + getAccessToken())
                    .url(FCM_URL)
                    .post(requestBody)
                    .build();


            new Thread(()->{
                Response response = null;
                try {
                    response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.e("Message has been sent",response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static String getAccessToken() throws IOException {
        String ruta = Environment.getExternalStorageDirectory() + "/google-services.json";
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream(ruta))
                .createScoped(Arrays.asList(SCOPE));
        googleCredential.refreshToken();
        String token = googleCredential.getAccessToken();
        return token;
    }

    private JSONObject getValidJsonBody(String msg,String title) throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put("to", session.getSessionUserToken());

        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put("sessionUserImage", session.getSessionUserImage());
        jsonObjectData.put("sessionUserToken", session.getSessionUserToken());
        jsonObjectData.put("sessionUserName", session.getSessionUserName());
        jsonObjectData.put("sessionState", session.getSessionState());
        jsonObjectData.put("sessionUserId", session.getSessionUserId());
        jsonObjectData.put("sessionUserBar", session.getSessionUserBar());
        jsonObjectData.put("sessionDateStart", session.getSessionDateStart());
        jsonObjectBody.put("data", jsonObjectData);

        JSONObject notification = new JSONObject();
        notification.put("body", msg);
        notification.put("title", title);

        jsonObjectBody.put("notification", notification);

        Log.e("JSON BUILD",jsonObjectBody+"");
        return jsonObjectBody;

    }

}
