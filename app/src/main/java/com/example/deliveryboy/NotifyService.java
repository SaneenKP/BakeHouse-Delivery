package com.example.deliveryboy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotifyService extends Service {
    private DatabaseReference fireBaseRealtimeDatabase;

    public NotifyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
    }

    @Override
    public IBinder onBind(Intent intent) {
        fireBaseRealtimeDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Toast.makeText(getApplicationContext(),"Data updated",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
}