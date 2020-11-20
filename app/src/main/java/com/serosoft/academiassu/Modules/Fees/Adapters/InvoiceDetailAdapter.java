package com.serosoft.academiassu.Modules.Fees.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;
import com.serosoft.academiassu.Modules.Fees.InvoiceDetails_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.SqliteDB.DbHelper;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on September 02 2020.
 */

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.MyViewHolder> {

    String currencySymbol = "";
    String fee_head_name = "";
    DbHelper dbHelper;

    private Context context;
    private ArrayList<InvoiceDetails_Dto> feeList;
    private TranslationManager translationManager;

    public InvoiceDetailAdapter(Context context, ArrayList<InvoiceDetails_Dto> feeList) {
        this.context = context;
        this.feeList = feeList;

        dbHelper = new DbHelper(context);
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fees_invoice_detail_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        InvoiceDetails_Dto list = feeList.get(position);

        String groupingFeeHeadName = ProjectUtils.getCorrectedString(list.getGroupingFeeHeadName());
        String fee_head = list.getFee_head();

        if(!groupingFeeHeadName.equalsIgnoreCase("")){
            fee_head_name = groupingFeeHeadName;
        }else{
            fee_head_name = fee_head;
        }

        holder.tvInvoiceTitle.setText(fee_head_name);

        //Here get currency according to currencycode
        String id = String.valueOf(list.getCurrencyId());
        ArrayList<CurrencyDto> currencyList = dbHelper.getAllData();

        if(currencyList != null && currencyList.size() > 0){
            for(CurrencyDto currencyDto : currencyList){
                if(currencyDto.getId().contains(id)){
                    String curCode = currencyDto.getCurrencyCode();
                    currencySymbol = BaseActivity.Utils.getCurrencySymbol(curCode);
                }
            }
        }
        else{
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            currencySymbol = sharedPrefrenceManager.getCurrencySymbolFromKey();
        }

        Double balanceAmount = list.getBalanceAmount();
        Double discountAmount = list.getDiscountAmount();

        if(!discountAmount.isNaN()){
           discountAmount = discountAmount;
        }else{
            discountAmount = 0.0;
        }

        Double totalAmount = list.getTotalAmount();
        Double adjustedAmount = list.getAdjustedAmount();
        Double billableAmount = list.getBillableAmount();

        holder.tvBalanceAmount.setText(currencySymbol + String.valueOf(balanceAmount));
        holder.tvDiscountAmount.setText(currencySymbol + String.valueOf(discountAmount));
        holder.tvTotalAmount.setText(currencySymbol + String.valueOf(totalAmount));
        holder.tvSettledAmount.setText(currencySymbol + String.valueOf(adjustedAmount));

        if (adjustedAmount.equals(billableAmount)) {
            //Fully Paid
            holder.tvInvoiceStatus.setText(translationManager.AMOUNT_SETTLED_KEY);
            holder.tvInvoiceStatus.setBackgroundResource(R.drawable.background_for_paid);

        } else if (adjustedAmount.equals(0.0)) {
            //Not Paid
            holder.tvInvoiceStatus.setText(translationManager.FULLY_PENDING_KEY);
            holder.tvInvoiceStatus.setBackgroundResource(R.drawable.background_for_not_paid);

        } else {
            //Partially Paid
            holder.tvInvoiceStatus.setText(translationManager.PARTIALLY_PAID_KEY);
            holder.tvInvoiceStatus.setBackgroundResource(R.drawable.background_for_partially_paid);
        }
    }

    @Override
    public int getItemCount() {

        return (null != feeList ? feeList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvInvoiceTitle,tvInvoiceStatus,tvDiscountAmount,tvSettledAmount,tvTotalAmount,tvBalanceAmount;
        private TextView tvDiscountAmount1,tvSettledAmount1,tvTotalAmount1,tvBalanceAmount1;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvInvoiceTitle = itemView.findViewById(R.id.tvInvoiceTitle);
            tvInvoiceStatus = itemView.findViewById(R.id.tvInvoiceStatus);
            tvDiscountAmount = itemView.findViewById(R.id.tvDiscountAmount);
            tvSettledAmount = itemView.findViewById(R.id.tvSettledAmount);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalanceAmount);

            tvDiscountAmount1 = itemView.findViewById(R.id.tvDiscountAmount1);
            tvSettledAmount1 = itemView.findViewById(R.id.tvSettledAmount1);
            tvTotalAmount1 = itemView.findViewById(R.id.tvTotalAmount1);
            tvBalanceAmount1 = itemView.findViewById(R.id.tvBalanceAmount1);

            tvDiscountAmount1.setText(translationManager.DISCOUNT_AMOUNT_KEY);
            tvSettledAmount1.setText(translationManager.AMOUNT_SETTLED_KEY);
            tvTotalAmount1.setText(translationManager.TOTAL_AMOUNT_KEY);
            tvBalanceAmount1.setText(translationManager.BALANCE_AMOUNT_KEY);
        }
    }
}
