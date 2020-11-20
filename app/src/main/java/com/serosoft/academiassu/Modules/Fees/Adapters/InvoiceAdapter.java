package com.serosoft.academiassu.Modules.Fees.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Manager.FeeManager;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.SqliteDB.DbHelper;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on September 01 2020.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    private Context context;
    private List<JSONObject> invoiceList;
    private FeeManager feeManager;
    public boolean showCheckBoxes;
    public List<Integer> indicesOfSelectedItems;

    private TranslationManager translationManager;
    private OnItemClickListener mItemClickListener;

    View view;
    String amountStatus = "";
    String currencySymbol = "";
    DbHelper dbHelper;

    boolean allowPartialPaymentThroughMobileApp = false;

    public InvoiceAdapter(Context context, List<JSONObject> invoiceList) {

        this.context = context;
        this.invoiceList = invoiceList;

        translationManager = new TranslationManager(context);
        feeManager = new FeeManager(context);
        dbHelper = new DbHelper(context);
        this.showCheckBoxes = false;
    }

    public InvoiceAdapter(Context context, List<JSONObject> invoiceList, boolean showCheckBoxes, List<Integer> indicesOfSelectedItems) {

        this.context = context;
        this.invoiceList = invoiceList;

        translationManager = new TranslationManager(context);
        feeManager = new FeeManager(context);
        dbHelper = new DbHelper(context);
        this.showCheckBoxes = showCheckBoxes;
        this.indicesOfSelectedItems = indicesOfSelectedItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fees_invoice_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        JSONObject jsonObject = invoiceList.get(position);

        String invoiceTitle = ProjectUtils.getCorrectedString(feeManager.getFeeHead(jsonObject));
        if (!invoiceTitle.equalsIgnoreCase("")) {
            holder.tvInvoiceTitle.setText(translationManager.BILL_NUMBER_KEY+" "+ invoiceTitle);
        }

        String billDate = ProjectUtils.getCorrectedString(feeManager.getFeeDate(jsonObject));
        if (!billDate.equalsIgnoreCase("")) {
            holder.tvBillDate.setText(translationManager.BILL_DATE_KEY+" "+ billDate);
        }

        String dueDate = ProjectUtils.getCorrectedString(feeManager.getFeeDueDate(jsonObject));
        if (!dueDate.equalsIgnoreCase("")) {
            holder.tvDueDate.setText(translationManager.DUE_DATE_KEY+" "+ dueDate);
        }

        //Here get currency according to currencycode
        String id = String.valueOf(feeManager.getCurrencyId(jsonObject));
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

        Double balanceAmount = feeManager.getBalanceAmount(jsonObject);
        Double totalAmount = feeManager.getTotalAmount(jsonObject);

        holder.tvBalanceAmount.setText(currencySymbol + balanceAmount);
        holder.tvTotalAmount.setText(currencySymbol + totalAmount);

        String status = feeManager.getFeesStatus(jsonObject);
        if(status.equalsIgnoreCase("Paid")){

            amountStatus = translationManager.PAID_KEY;
        }else if(status.equalsIgnoreCase("Fully Pending")){

            amountStatus = translationManager.FULLY_PENDING_KEY;
        }else if(status.equalsIgnoreCase("Partially Settled")){

            amountStatus = translationManager.PARTIALLY_SETTLED_KEY;
        }
        holder.tvInvoiceStatus.setText(amountStatus);
        holder.tvInvoiceStatus.setTextColor(feeManager.getFeesStatusColor(jsonObject));

        if (this.showCheckBoxes) {


            boolean isPaymentEnable = checkCurrencyId(position, invoiceList);

            if(isPaymentEnable){

                if (indicesOfSelectedItems.contains(position)) {

                    allowPartialPaymentThroughMobileApp = jsonObject.optBoolean("allowPartialPaymentThroughMobileApp");
                    if(allowPartialPaymentThroughMobileApp){
                        holder.ivEdit.setVisibility(View.VISIBLE);
                    }else{
                        holder.ivEdit.setVisibility(View.GONE);
                    }

                    View finalConvertView = view;

                    holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(mItemClickListener != null)
                            {
                                mItemClickListener.onItemClick(finalConvertView,position);
                            }
                        }
                    });

                } else {
                    holder.rlParent.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    holder.ivEdit.setVisibility(View.GONE);
                }
            }else {
                holder.rlParent.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                holder.ivEdit.setVisibility(View.GONE);
            }

        } else {
            holder.ivEdit.setVisibility(View.GONE);
        }
    }

    private boolean checkCurrencyId(int i, List<JSONObject> feeList) {

        boolean isPaymentEnable = false;

        String id = String.valueOf(feeManager.getCurrencyId(feeList.get(i)));
        ArrayList<CurrencyDto> currencyList = dbHelper.getAllData();

        if (currencyList != null && currencyList.size() > 0) {
            for (CurrencyDto currencyDto : currencyList) {
                if (currencyDto.getId().contains(id)) {
                    String curCode = currencyDto.getCurrencyCode();

                    if (curCode.equalsIgnoreCase(Consts.CURRENCY_CODE)) {
                        isPaymentEnable = true;
                    } else {
                        isPaymentEnable = false;
                    }
                }
            }
        }
        return isPaymentEnable;
    }

    @Override
    public int getItemCount() {

        return (null != invoiceList ? invoiceList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private RelativeLayout rlParent;
        private ImageView ivEdit;
        private TextView tvInvoiceTitle,tvInvoiceStatus,tvBillDate,tvDueDate;
        private TextView tvTotalAmount,tvBalanceAmount;
        private TextView tvTotalAmount1,tvBalanceAmount1;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            rlParent = itemView.findViewById(R.id.rlParent);
            ivEdit = itemView.findViewById(R.id.ivEdit);

            tvInvoiceTitle = itemView.findViewById(R.id.tvInvoiceTitle);
            tvInvoiceStatus = itemView.findViewById(R.id.tvInvoiceStatus);
            tvBillDate = itemView.findViewById(R.id.tvBillDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);

            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalanceAmount);

            tvTotalAmount1 = itemView.findViewById(R.id.tvTotalAmount1);
            tvBalanceAmount1 = itemView.findViewById(R.id.tvBalanceAmount1);

            tvTotalAmount1.setText(translationManager.TOTAL_AMOUNT_KEY);
            tvBalanceAmount1.setText(translationManager.BALANCE_AMOUNT_KEY);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if(mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }
}
