package com.upec.androidtemplate20192020;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.upec.androidtemplate20192020.dbRequest.dbRequest;

import java.util.ArrayList;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {
    private ArrayList<Expense> expensesList;


    public ExpensesAdapter(ArrayList<Expense> expensesList){
        this.expensesList = expensesList;
    }

    @Override
    public ExpensesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.expense_list_cell, parent, false);
        return new ExpensesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ExpensesViewHolder holder, int position) {
        Expense currentExpense = expensesList.get(position);
        holder.display(currentExpense);
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        private Expense mCurrentExpense;
        private final TextView mTitle, mAmount, mDate, mPayingUser;
        private RequestQueue queue;
        private dbRequest request;
        private SessionManager sessionManager;

        public ExpensesViewHolder(final View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.activity_list_expense_title_tv);
            mAmount = (TextView) itemView.findViewById(R.id.activity_list_expense_amount_tv);
            mDate = (TextView) itemView.findViewById(R.id.activity_list_expense_date_tv);
            mPayingUser = (TextView) itemView.findViewById(R.id.activity_list_expense_payinguser_tv);
            mPayingUser.setTypeface(null, Typeface.BOLD);

            sessionManager = new SessionManager(itemView.getContext());
            queue = VolleySingleton.getInstance(itemView.getContext()).getRequestQueue();
            request = new dbRequest(itemView.getContext(), queue);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete expenditure.")
                            .setMessage("Are you sure you want to delete this expenditure ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    request.deleteExpenses(mCurrentExpense.getEid(), sessionManager, new dbRequest.DeleteExpenseCallback() {
                                        @Override
                                        public void onSuccess(String message) {
                                            Intent intent = new Intent(itemView.getContext(), MainUserActivity.class);
                                            intent.putExtra("DELETESUCCESS", message);
                                            itemView.getContext().startActivity(intent);
                                        }

                                        @Override
                                        public void onError(String message) {
                                            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                    return false;
                }
            });
        }
        public void display(Expense expense) {
            mCurrentExpense= expense;
            mTitle.setText(mCurrentExpense.getmTitle());
            mAmount.setText(mCurrentExpense.getmAmount()+"€");
            mDate.setText(mCurrentExpense.getDate());
            mPayingUser.setText("Paid by : "+mCurrentExpense.getmPayingUser());
        }
    }
}
