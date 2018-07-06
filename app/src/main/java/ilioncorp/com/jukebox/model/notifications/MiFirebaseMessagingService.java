package ilioncorp.com.jukebox.model.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.SessionVO;
import ilioncorp.com.jukebox.view.activity.BarActivity;
import ilioncorp.com.jukebox.view.activity.ChatActivity;
import ilioncorp.com.jukebox.view.activity.MainActivity;


public class MiFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.e("Recibido de :",from);
        if(remoteMessage.getNotification() != null && remoteMessage.getData().size()>0){
            Log.e("body",remoteMessage.getNotification().getBody());
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData().size()>0){
            Log.e("DATA",remoteMessage.getData()+"");
            showNotificationChat(remoteMessage);
        }
    }

    private void showNotificationChat(RemoteMessage remoteMessage) {
            String sessionUserImage = remoteMessage.getData().get("sessionUserImage");
            String sessionUserToken = remoteMessage.getData().get("sessionUserToken");
            String sessionUserName = remoteMessage.getData().get("sessionUserName");
            String sessionState = remoteMessage.getData().get("sessionState");
            String sessionUserId = remoteMessage.getData().get("sessionUserId");
            String sessionUserBar = remoteMessage.getData().get("sessionUserBar");
            String sessionDateStart = remoteMessage.getData().get("sessionDateStart");

        SessionVO session = new SessionVO();
        session.setSessionUserImage(sessionUserImage);
        session.setSessionUserToken(sessionUserToken);
        session.setSessionUserName(sessionUserName);
        session.setSessionState(sessionState);
        session.setSessionUserId(sessionUserId);
        session.setSessionUserBar(sessionUserBar);
        session.setSessionDateStart(sessionDateStart);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userSession",session);
        intent.putExtra("idBar",session.getSessionUserBar());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        buildNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),pendingIntent);
    }

    private void showNotification(String title, String body) {
        Intent intent = null;

        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        buildNotification(title,body,pendingIntent);
    }

    private void buildNotification(String title, String body,PendingIntent pendingIntent) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"")
                .setLargeIcon(getBitmap(Color.BLUE))
                .setSmallIcon(R.drawable.jukebox24px)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private Bitmap getBitmap(int color) {
        Drawable drawable = ResourcesCompat.getDrawable(this.getResources(), R.drawable.jukebox2, null);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        //set round background on lolipop+ and square before
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {*/
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(color);
            canvas.drawCircle(canvas.getHeight()/2, canvas.getHeight()/2, canvas.getHeight()/2, p);
        /*}
        else {
            canvas.drawColor(color);
        }*/
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

