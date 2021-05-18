package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Orders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference fireBaseRealtimeDatabase;
    private OrderDetails orderDetails;
    private List<OrderDetails> orderList;
    private OrderViewAdapter orderViewAdapter;
    private List<String> orderKeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.ordersView);
        recyclerView.setLayoutManager(layoutManager);

        orderList = new ArrayList<>();
        orderKeys = new ArrayList<>();

        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");


        fireBaseRealtimeDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                OrderDetails orderDetails = snapshot.getValue(OrderDetails.class);
                orderList.add(orderDetails);
                orderKeys.add(snapshot.getKey());
               // rearrangeOrderAccordingToTime(orderList);
                orderViewAdapter = new OrderViewAdapter(getApplicationContext(), orderList);
                recyclerView.setAdapter(orderViewAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                OrderDetails changedOrderDetails = snapshot.getValue(OrderDetails.class);
                int pos = orderKeys.indexOf(snapshot.getKey());
                orderList.remove(pos);
                orderList.add(changedOrderDetails);
                orderViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                int pos = orderKeys.indexOf(snapshot.getKey());
                orderKeys.remove(pos);
                orderList.remove(pos);
                recyclerView.removeViewAt(pos);
                orderViewAdapter.notifyItemRemoved(pos);
                orderViewAdapter.notifyItemRangeChanged(pos, orderList.size());

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


    /*private void rearrangeOrderAccordingToTime(List<OrderDetails> order){

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(Calendar.getInstance().getTime());
        String Thour = time.substring(0 , 2);
        String Tminute = time.substring(3,5);
        String Tsec = time.substring(6);

        if (order.size() > 1){




            for (int i = 0 ; i < order.size() ; i++){

                String hour = order.get(i).getTime().substring(0 , 2);
                String minute = order.get(i).getTime().substring(3,5);
                String sec = order.get(i).getTime().substring(6);


            }


        }



    }

    private boolean timeCompare(String time1 , String time2){

        boolean result = false;

        int hour1 = Integer.parseInt(time1.substring(0 , 2));
        int minute1 = Integer.parseInt(time1.substring(3 , 5));
        int sec1 =Integer.parseInt(time1.substring(6));

        int hour2 = Integer.parseInt(time2.substring(0 , 2));
        int minute2 = Integer.parseInt(time2.substring(3 , 5));
        int sec2 = Integer.parseInt(time2.substring(6));

        if (hour1 == hour2){

            if (minute1 == minute2){

                if (sec1 == sec2){

                }

            }

        }


        return
    }/*


