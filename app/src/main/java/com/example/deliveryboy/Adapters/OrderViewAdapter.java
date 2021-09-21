package com.example.deliveryboy.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliveryboy.Orders;
import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.example.deliveryboy.R;
import com.example.deliveryboy.SetDeliveryBoyInterface;
import com.example.deliveryboy.orderDetailsDisplay;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.orderHolder> {


    private Context context;
    private List<OrderDetails> orderList;
    private List<String> orderKeys;
    private SetDeliveryBoyInterface setDeliveryBoyInterface;

    public OrderViewAdapter(Context context, List<OrderDetails> orderList, List<String> orderKeys , SetDeliveryBoyInterface setDeliveryBoyInterface) {
        this.context = context;
        this.orderList = orderList;
        this.orderKeys = orderKeys;
        this.setDeliveryBoyInterface = setDeliveryBoyInterface;

    }

    @NonNull
    @Override
    public OrderViewAdapter.orderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_display_view , parent , false);
        orderHolder orderHolder = new orderHolder(view);
        return orderHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewAdapter.orderHolder holder, int position) {

        holder.orderName.setText(orderList.get(position).getName());
        holder.orderNumber.setText(orderList.get(position).getPhoneNumber());
        holder.orderPicked.setVisibility(orderList.get(position).getPickupIndex().equals("yes") ? View.VISIBLE : View.INVISIBLE);
        holder.orderPlaced.setVisibility(orderList.get(position).getPlacedIndex().equals("yes") ? View.VISIBLE: View.INVISIBLE);
        holder.orderDelivered.setVisibility(orderList.get(position).getDeliveryIndex().equals("yes") ? View.VISIBLE :View.INVISIBLE);
        try {
            if (orderList.get(position).getAssigned()==null){
                holder.assign.setChecked(false);
                holder.assign.setClickable(true);
            }
            else{
                holder.assign.setChecked(true);
                holder.assign.setClickable(false);
            }
        }catch (NullPointerException e){}

        holder.date.setText(orderList.get(position).getTime()+" "+orderList.get(position).getDate());
        holder.orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openOrderDetailsDisplay = new Intent(context , orderDetailsDisplay.class);
                openOrderDetailsDisplay.putExtra("OrderDetails" , orderList.get(holder.getAdapterPosition()));
                openOrderDetailsDisplay.putExtra("OrderKey" ,  orderKeys.get(holder.getAdapterPosition()));
                openOrderDetailsDisplay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(openOrderDetailsDisplay);
            }
        });
        holder.orderNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+orderList.get(position).getPhoneNumber()+"\""));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Uri gmmIntentUri = Uri.parse("geo:"+orderList.get(position).getLatitude()+","+orderList.get(position).getLongitude()+"z=5");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(mapIntent);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+orderList.get(position).getLatitude()+","+orderList.get(position).getLongitude()+"\""));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        if (holder.assign.isClickable()) {
            {
                holder.assign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(holder.assign, orderKeys.get(holder.getAdapterPosition()));
                    }
                });
            }
        }

    }


    private void showDialog(CheckBox assign , String key){

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
        alertDialogBuilder.setTitle("Are you sure to assign to this order");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (setDeliveryBoyInterface.checkDeliveryBoyDetails()){
                   setDeliveryBoyInterface.setDeliveryBoy(key , assign);
                }else{
                    assign.setChecked(false);
                }
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                assign.setChecked(false);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class orderHolder extends RecyclerView.ViewHolder {

        private TextView orderName , orderPicked ,orderPlaced , orderDelivered;
        private MaterialButton openMaps,orderNumber;
        private CardView orderCardView;
        private MaterialTextView date;
        private CheckBox assign;
        public orderHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderNumber = itemView.findViewById(R.id.orderPhoneNumber);
            orderPicked = itemView.findViewById(R.id.orderPicked);
            orderPlaced = itemView.findViewById(R.id.orderPlaced);
            orderDelivered = itemView.findViewById(R.id.orderDelivered);
            openMaps = itemView.findViewById(R.id.openLocation);
            orderCardView = itemView.findViewById(R.id.orderCardView);
            assign = itemView.findViewById(R.id.assign);
            date=itemView.findViewById(R.id.date);
        }
    }
}
