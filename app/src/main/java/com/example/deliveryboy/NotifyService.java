package com.example.deliveryboy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.deliveryboy.App.CHANNEL_ID;

public class NotifyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DatabaseReference orderDatabase = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode));
        Query latestOrder = orderDatabase.orderByKey().limitToLast(1);

        latestOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,  String previousChildName) {

                String time = snapshot.child("time").getValue(String.class);
                String date = snapshot.child("date").getValue(String.class);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    Date latestOrderTime = sdf.parse(date + " " + time);
                    Date latestUserOnlineTime = sdf.parse(new SharedPreferenceConfig(getApplicationContext()).readLatestOnlineDate() + " " + new SharedPreferenceConfig(getApplicationContext()).readLatestOnlineTime());
                    if (latestOrderTime.after(latestUserOnlineTime) || latestOrderTime.equals(latestUserOnlineTime)){
                        startNotificationChannel();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });



        return START_REDELIVER_INTENT;
    }

    public void startNotificationChannel(){
        Intent notificationIntent = new Intent(this,Orders.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , notificationIntent , 0);
        Log.d("Tag","Message");
        Notification notification = new NotificationCompat.Builder(this , CHANNEL_ID)
                .setContentTitle("Kepler Notification")
                .setContentText("New Order Placed")
                .setSmallIcon(R.drawable.ic_baseline_restaurant_24)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1 , notification);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
