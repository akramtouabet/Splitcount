package com.upec.androidtemplate20192020;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtViewHolder>{
private String usernameFrom, usernameFor, debt;
private ArrayList<Balance> balances;

        public DebtAdapter(ArrayList<Balance>balances){
            this.balances = balances;
        }

    @Override
    public DebtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.balance_list_cell, parent, false);
        return new DebtViewHolder(view);
    }

        @Override
        public void onBindViewHolder(final DebtViewHolder holder, int position) {
            Balance mCurrentBalance = balances.get(position);
            holder.display(mCurrentBalance);
        }

        @Override
        public int getItemCount() {
            return balances.size();
        }


        public static class DebtViewHolder extends RecyclerView.ViewHolder {

            private final TextView mFrom, mOwnTo, mDebt, mFor;

            public DebtViewHolder(final View itemView) {
                super(itemView);
                mFrom = (TextView) itemView.findViewById(R.id.activity_from_tv);
                mOwnTo = (TextView) itemView.findViewById(R.id.activity_ownto_tv);
                mDebt = (TextView) itemView.findViewById(R.id.activity_debt_tv);
                mFor = (TextView) itemView.findViewById(R.id.activity_for_tv);
            }

            public void display(Balance currentBalance) {
                mFrom.setText(currentBalance.getFrom());
                mFor.setText(currentBalance.getTo());
                mDebt.setText(currentBalance.getAmount()+" €");
            }
        }
}
