package com.example.max_e1controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView textview;
        public ViewHolder(View v)
        {
            super(v);
            textview =  v.findViewById(R.id.bt_dev_name);
        }
    }
    private ArrayList<String> btDevName;
    private ArrayList<String> btDevAddress;
    Activity activity;
    public RecyclerViewAdapter(ArrayList<String> devName, ArrayList<String> devAddress, Activity act)
    {
        btDevName = devName;
        btDevAddress = devAddress;
        activity = act;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.paired_device_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount()
    {
        return btDevName.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Integer pos = position;
       holder.textview.setText(btDevName.get(position));
       holder.itemView.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
                Toast.makeText(activity,btDevName.get(pos),Toast.LENGTH_SHORT).show();
           }
       });
    }
}
