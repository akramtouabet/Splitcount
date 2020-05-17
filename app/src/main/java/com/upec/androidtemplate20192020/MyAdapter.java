package com.upec.androidtemplate20192020;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;
import java.util.ArrayList;
import java.util.LinkedList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<SplitCount> splitcountsList;


    public MyAdapter(ArrayList<SplitCount> splitcountsList){
            this.splitcountsList = splitcountsList;
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder holder, int position) {
        SplitCount currentSplitCount = splitcountsList.get(position);
        holder.display(currentSplitCount);
    }

    @Override
    public int getItemCount() {
        return splitcountsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName, mDescription;
        private int id;
        private String sp_code;

        public MyViewHolder(final View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.name);
            mDescription = (TextView) itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SplitcountManagerActivity.class);
                    intent.putExtra("name", mName.getText());
                    intent.putExtra("description", mDescription.getText());
                    intent.putExtra("sid", id);
                    intent.putExtra("sp_code", sp_code);
                    view.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(view.getContext(), BalanceActivity.class);
                    intent.putExtra("sid", id);
                    view.getContext().startActivity(intent);
                    return false;
                }
            });

        }
            public void display(SplitCount splitcount) {
                mName.setText(splitcount.getmName());
                mDescription.setText(splitcount.getmDescription());
                id = splitcount.getmSid();
                sp_code = splitcount.getmCode();
            }
    }
}
