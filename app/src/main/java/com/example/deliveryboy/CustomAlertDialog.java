package com.example.deliveryboy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

public class CustomAlertDialog {

    private Context context;
    private String message;
    private int layout;
    private boolean SetCancellationOnTouchOutside;


    public CustomAlertDialog(Context context, String message) {
        this.context = context;
        this.message = message;
        this.layout = R.layout.progress_dialog;
        SetCancellationOnTouchOutside = false;

    }

    private AlertDialog getAlertDialog(int layout , String message , Boolean SetCancellationOnTouchOutside){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layout , null);
        builder.setView(v);
        TextView textView = v.findViewById(R.id.text_progress_bar);
        textView.setText(message);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(SetCancellationOnTouchOutside);
        return dialog;

    }

    public AlertDialog showAlertDialog(){

        AlertDialog dialog = getAlertDialog(layout , message ,SetCancellationOnTouchOutside);
        dialog.show();
        return dialog;
    }
    public AlertDialog endAlertDialog(){

        AlertDialog dialog = getAlertDialog(layout , message ,SetCancellationOnTouchOutside);
        dialog.dismiss();
        return dialog;
    }

}
