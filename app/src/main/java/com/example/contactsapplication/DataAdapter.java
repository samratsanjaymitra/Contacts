package com.example.contactsapplication;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by samrat on 15/1/20.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    ArrayList<ContactsPOJO> al_contacts;
    IDataAdapterCallBack iDataAdapterCallBack;

    DataAdapter(Context context,ArrayList<ContactsPOJO> al_contacts,IDataAdapterCallBack iDataAdapterCallBack) {
        this.context = context;
        this.al_contacts=al_contacts;
        this.iDataAdapterCallBack=iDataAdapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_data, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(al_contacts.size()!=0){
            final ContactsPOJO pojo = al_contacts.get(position);

            holder.tv_name.setText("Name: " + pojo.getName());
            holder.tv_contact.setText(pojo.getPhone());
            Picasso.with(context).load(pojo.getImage()).into(holder.iv_image);
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iDataAdapterCallBack.clickedDataName(pojo.getName());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return al_contacts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_name, tv_contact;
        Button btn_delete;


        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_contact);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
        }


    }
    public interface IDataAdapterCallBack
    {
        void clickedDataName(String name);
    }
}
