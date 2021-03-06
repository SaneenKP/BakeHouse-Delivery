 package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.deliveryboy.PojoClasses.DeliveryBoyDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private CustomAlertDialog customAlertDialog;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        customAlertDialog = new CustomAlertDialog(MainActivity.this ,
                getApplicationContext().getResources().
                        getString(R.string.AnonymousSignInStatus));
    }

    private void AnonymousSignIn(){

        DatabaseReference deliveryBoyDatabase = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.DeliveryBoyNode));
        alertDialog = customAlertDialog.showAlertDialog();
         auth.signInAnonymously().
                 addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        String userID = task.getResult().getUser().getUid();
                        DeliveryBoyDetails deliveryBoyDetails = new DeliveryBoyDetails();
                        deliveryBoyDetails.setName("temp");
                        deliveryBoyDetails.setNumber("0000000000");
                        deliveryBoyDatabase.child(userID).setValue(deliveryBoyDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext() , ""+userID , Toast.LENGTH_LONG).show();
                                    Intent openOrders = new Intent(MainActivity.this , Orders.class);
                                    alertDialog.dismiss();
                                    startActivity(openOrders);
                                }else{
                                    Toast.makeText(getApplicationContext() , "Failed To upload Data" , Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext() , "Anonymous Authentication Failed" , Toast.LENGTH_LONG).show();
                    }
             }
         });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            alertDialog = customAlertDialog.showAlertDialog();
            Intent openOrders = new Intent(MainActivity.this , Orders.class);
            startActivity(openOrders);
            finish();
        }
        else
        {
            AnonymousSignIn();
        }
    }


}