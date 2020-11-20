package com.serosoft.academiassu.Modules.Fees.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder>{

    private Context context;
    private List<JSONObject> receiptList;
    private TranslationManager translationManager;
    private FeeManager feeManager;
    private OnItemClickListener mItemClickListener;

    private String currencySymbol = "";
    private DbHelper dbHelper;

    public ReceiptAdapter(Context context, List<JSONObject> receiptList) {
        this.context = context;
        this.receiptList = receiptList;

        feeManager = new FeeManager(context);
        dbHelper = new DbHelper(context);
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fees_receipt_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       JSONObject jsonObject = receiptList.get(position);

       String feeHead = ProjectUtils.getCorrectedString(feeManager.getReceiptNumber(jsonObject));

       if (!feeHead.equalsIgnoreCase("")) {
           holder.tvFeeHead.setVisibility(View.VISIBLE);
           holder.tvFeeHead.setText(translationManager.RECEIPT_NUMBER_KEY+" " + feeHead);
       }
       else{
           holder.tvFeeHead.setVisibility(View.GONE);
       }

       String feeDate = ProjectUtils.getCorrectedString(feeManager.getReceiptDate(jsonObject));

       if (!feeDate.equalsIgnoreCase("")) {
           holder.tvDueDate.setVisibility(View.VISIBLE);
           holder.tvDueDate.setText(translationManager.RECEIPT_DATE_KEY+" : " + feeDate);
       }
       else{
           holder.tvDueDate.setVisibility(View.GONE);
       }

        Double feeAmount = feeManager.getReceiptAmount(jsonObject);

        if (feeAmount != null) {
            //Here get currency according to currencycode
            String id = String.valueOf(feeManager.getCurrencyId(receiptList.get(position)));
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

            Double receiptAmount = feeManager.getReceiptAmount(receiptList.get(position));
            holder.tvFeeAmount.setVisibility(View.VISIBLE);
            holder.tvFeeAmount.setText(currencySymbol + receiptAmount);
        }
        else{
            holder.tvFeeAmount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return (null != receiptList ? receiptList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvFeeHead,tvFeeAmount,tvDueDate;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvFeeHead = itemView.findViewById(R.id.tvFeeHead);
            tvFeeAmount = itemView.findViewById(R.id.tvFeeAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);

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
