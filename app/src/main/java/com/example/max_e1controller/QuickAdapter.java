package com.example.max_e1controller;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class QuickAdapter<T> extends RecyclerView.Adapter<QuickAdapter.ViewHolder>
{
    private ArrayList<T> datum;
    public QuickAdapter(ArrayList<T> data)
    {
        datum = data;
    }
    public T getData(int position)
    {
        return datum.get(position);
    }
    public abstract int getLayoutId(int viewType);
    public abstract void convert(ViewHolder viewHolder, T data, int position);

      static class ViewHolder extends RecyclerView.ViewHolder
      {
          private SparseArray<View> views;
          private View convertView;

          private ViewHolder(View v)
          {
              super(v);
              convertView = v;
              views = new SparseArray<>();
          }



          public static ViewHolder get(ViewGroup parent, int layoutId)
          {
              View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
              return new ViewHolder(convertView);
          }

          public <T extends View> T getView(int id)
          {
              View v = views.get(id);
              if(v == null)
              {
                  v = convertView.findViewById(id);
                  views.put(id, v);
              }
              return (T)v;
          }

          public void setText(int id, String value)
          {
              TextView view = getView(id);
              view.setText(value);
          }
      }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
         return ViewHolder.get(parent, getLayoutId(viewType));
    }

    @Override
    public int getItemCount()
    {
        return datum.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        convert(holder, datum.get(position), position);
    }
}
