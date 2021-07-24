package com.example.deliveryboy;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.deliveryboy.PojoClasses.DeliveryBoyDetails;
import com.example.deliveryboy.R;

import java.util.HashMap;

public class SharedPreferenceConfig {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreferenceConfig(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.DeliveryBoyPreference),Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void writeNotificationStatus(boolean status){
        editor.putBoolean("notification" , status);
        editor.commit();
    }

    public void writeDeliveryBoyDetails(DeliveryBoyDetails deliveryBoyDetails){

        editor.putString("name" , deliveryBoyDetails.getName());
        editor.putString("number" , deliveryBoyDetails.getNumber());
        editor.commit();

    }

    public String readDeliveryBoyName(){
        return sharedPreferences.getString("name" , "");
    }

    public String readDeliveryBoyNumber(){
        return sharedPreferences.getString("number" , "");
    }

    public boolean readNotificationStatus(){
        return sharedPreferences.getBoolean("notification",false);
    }

    public void clearPreferences(){
        editor.clear();
        editor.commit();
    }

}
