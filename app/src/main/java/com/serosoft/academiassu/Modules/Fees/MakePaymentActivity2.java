package com.serosoft.academiassu.Modules.Fees;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cipolat.superstateview.SuperStateView;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Manager.FeeManager;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;
import com.serosoft.academiassu.Modules.Fees.Adapters.BillAdapter;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakePaymentActivity2 extends BaseActivity implements PaytmPaymentTransactionCallback {

    Context mContext;
    public Toolbar toolbar;

    private FeeManager feeManager;

    private List<JSONObject> dueFeeList;
    private List<JSONObject> selectedFeeList;

    private SuperStateView superStateView;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    BillAdapter billAdapter;

    private LinearLayout linearLayoutCheckbox;
    private Button btnPayNow;
    private TextView tvForPayableAmount,tvAmountToPay;
    private RelativeLayout rlFeeGroup,rlBottom;
    private CheckBox cbForSelectAll;

    private double totalFeeAmount = 0;
    private double totalPayableAmount = 0;

    private boolean allDuesCleared;

    private int paymentID;
    private String BANKTXNID;
    private String CHECKSUMHASH;

    String env = "";
    private String mid = "";
    private String channelID = "";
    private String website = "";
    private String industryType = "";
    private String callbackURL = "";

    int position = 0;
    String currencySymbol = "";
    String status = "";

    private List<Integer> indicesOfDueInvoicesSelectedForPayments;

    private final String TAG = MakePaymentActivity2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_make_payment2);
        ProjectUtils.showLog(TAG,"onCreateView Start");

        mContext = MakePaymentActivity2.this;

        Initialize();

        feeManager = new FeeManager(this);

        populateContents();
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle("MAKE PAYMENT");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorFees, toolbar, this); }

        superStateView = findViewById(R.id.superStateView);
        recyclerView = findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        rlFeeGroup = findViewById(R.id.rlFeeGroup);
        btnPayNow = findViewById(R.id.btnPayNow);
        tvAmountToPay = findViewById(R.id.tvAmountToPay);
        tvForPayableAmount = findViewById(R.id.tvForPayableAmount);
        tvForPayableAmount.setText("0");

        linearLayoutCheckbox = findViewById(R.id.linearLayoutCheckbox);
        linearLayoutCheckbox.setVisibility(View.GONE);
        cbForSelectAll = findViewById(R.id.cbForSelectAll);
        rlBottom = findViewById(R.id.rlBottom);
        rlBottom.setVisibility(View.GONE);

        tvAmountToPay.setText(translationManager.AMOUNTTOPAY_KEY);
        btnPayNow.setText(translationManager.PAYNOW_KEY);
    }

    private void callEvents() {

        //Here check all bills status and do action
        if (!allDuesCleared) {

            rlBottom.setVisibility(View.VISIBLE);
            linearLayoutCheckbox.setVisibility(View.VISIBLE);
            cbForSelectAll.setChecked(false);

        } else {

            rlBottom.setVisibility(View.GONE);
            linearLayoutCheckbox.setVisibility(View.GONE);
        }

        totalPayableAmount = 0;
        tvForPayableAmount.setText(String.valueOf(totalPayableAmount));

        if (totalPayableAmount > 0.0) {

            btnPayClick(true);
        } else {

            btnPayClick(false);
        }

        cbForSelectAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (((CheckBox) v).isChecked())
                {
                    setDueInvoicesInAdapter(dueFeeList, true);
                } else {
                    setDueInvoicesInAdapter(dueFeeList, false);
                }
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (totalPayableAmount > 0.0)
                {
                    ProjectUtils.preventTwoClick(btnPayNow);
                    displayTAndCPopUp();
                }
                else
                {
                    ProjectUtils.showLong(mContext,getString(R.string.please_select_at_least_one_invoice));
                }
            }
        });
    }

    public void btnPayClick(Boolean isEnable){

        if (isEnable) {
            btnPayNow.setBackground(getResources().getDrawable(R.drawable.rounded_corner_pay_button_active));
            btnPayNow.setTextColor(getResources().getColor(R.color.colorWhite));
            btnPayNow.setEnabled(true);
        } else {
            btnPayNow.setBackground(getResources().getDrawable(R.drawable.rounded_corner_pay_button_inactive));
            btnPayNow.setTextColor(getResources().getColor(R.color.colorBlack));
            btnPayNow.setEnabled(false);
        }
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(mContext, this, KEYS.SWITCH_PENDING_BILLS).execute();

        } else {

            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void setAdapter()
    {
        if (dueFeeList != null && dueFeeList.size() != 0)
        {
            rlFeeGroup.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.GONE);

            indicesOfDueInvoicesSelectedForPayments = new ArrayList<>();
            selectedFeeList = new ArrayList<>();

            populateAllDuesCleared();

            callEvents();

            setDueInvoicesInAdapter(dueFeeList);

            populatePaymentGatewayDetails();
        }
        else
        {
            hideProgressDialog();
            rlFeeGroup.setVisibility(View.GONE);
            superStateView.setVisibility(View.VISIBLE);
            superStateView.setSubTitleText("No Bill Found!");
        }
    }

    private void populatePaymentGatewayDetails() {

        if (Flags.ENABLE_PAYTM_PRODUCTION) {
            env = "LIVE";
        } else {
            env = "STAGING";
        }
        if (ConnectionDetector.isConnectingToInternet(mContext)) {
            //showProgressDialog(this);
            new OptimizedServerCallAsyncTask(mContext,
                    this, KEYS.SWITCH_PAYMENT_GATEWAY).execute(env);
        } else {
            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    public void displayTAndCPopUp()
    {
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogForPayTMTermsAndConditions))
                .setTitle(R.string.payTM_terms_and_conditions_title)
                .setMessage(getResources().getString(R.string.payTM_terms_and_conditions_contents))
                .setPositiveButton("Agree", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callAPIWithPermission();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(12);
        if(Build.VERSION.SDK_INT > 25)
        {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else if (Build.VERSION.SDK_INT > 16)
        {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    private void callAPIWithPermission() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            String payloadString = String.valueOf(selectedFeeList);
            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(mContext,
                    this, KEYS.SWITCH_PAYMENT_ID).execute(String.valueOf(totalPayableAmount), payloadString);
        }else {
            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void setDueInvoicesInAdapter(List<JSONObject> dueFeeList) {

        billAdapter = new BillAdapter(mContext, dueFeeList);
        recyclerView.setAdapter(billAdapter);

        billAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                status = feeManager.getFeesStatus(dueFeeList.get(pos));

                //Here get currency according to currencycode
                currencySymbol = getCurrencySymbol(pos, dueFeeList);

                boolean isPaymentEnable = checkCurrencyId(pos, dueFeeList);

                if(isPaymentEnable){

                    if (indicesOfDueInvoicesSelectedForPayments.contains(pos))
                    {
                        indicesOfDueInvoicesSelectedForPayments.remove(Integer.valueOf(pos));
                        ImageView iv = view.findViewById(R.id.ivCheckBox);
                        iv.setImageResource(R.drawable.unchecked);
                        JSONObject jsonObject = dueFeeList.get(pos);

                        //Here hide EditOption
                        showEditOption(view, currencySymbol, status, false, jsonObject, new NewJSon() {
                            @Override
                            public void getNewJSON(JSONObject jsonObject1) {

                                selectedFeeList.remove(jsonObject1);

                                updateGrandTotal(selectedFeeList);

                                RelativeLayout rlParent = view.findViewById(R.id.rlParent);
                                rlParent.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                                billAdapter.indicesOfSelectedItems = indicesOfDueInvoicesSelectedForPayments;
                            }
                        });
                    }
                    else
                    {
                        indicesOfDueInvoicesSelectedForPayments.add(Integer.valueOf(pos));
                        ImageView iv = view.findViewById(R.id.ivCheckBox);
                        iv.setImageResource(R.drawable.checked);
                        JSONObject jsonObject = dueFeeList.get(pos);

                        //Here add selected row
                        selectedFeeList.add(jsonObject);

                        //Here show EditOption
                        showEditOption(view, currencySymbol, status, true, jsonObject, new NewJSon() {
                            @Override
                            public void getNewJSON(JSONObject jsonObject1) {

                                position = selectedFeeList.indexOf(jsonObject1);

                                if(jsonObject1.has("partAmount")){

                                    //Here updated selected row
                                    selectedFeeList.set(position,jsonObject1);
                                }

                                updateGrandTotal(selectedFeeList);

                                RelativeLayout rlParent = view.findViewById(R.id.rlParent);
                                rlParent.setBackgroundColor(getResources().getColor(R.color.colorBlueTransparent));

                                billAdapter.indicesOfSelectedItems = indicesOfDueInvoicesSelectedForPayments;
                            }
                        });
                    }

                }else{
                    ProjectUtils.showLong(mContext,getString(R.string.you_cannot_payment));
                }
            }
        });
    }

    private void setDueInvoicesInAdapter(List<JSONObject> dueFeeList, Boolean selectAll) {

        indicesOfDueInvoicesSelectedForPayments = new ArrayList<>();
        selectedFeeList = new ArrayList<>();
        totalPayableAmount = 0;

        if (selectAll)
        {
            for (int i = 0 ; i < dueFeeList.size() ; ++i)
            {
                indicesOfDueInvoicesSelectedForPayments.add(i);

                boolean isPaymentEnable = checkCurrencyId(i, dueFeeList);

                if(isPaymentEnable){
                    JSONObject jsonObject = dueFeeList.get(i);
                    double balanceAmount = jsonObject.optDouble("balanceAmount");
                    totalPayableAmount = totalPayableAmount + balanceAmount;
                    selectedFeeList.add(jsonObject);
                }
            }

            btnPayClick(true);

            billAdapter = new BillAdapter(mContext, dueFeeList, indicesOfDueInvoicesSelectedForPayments);
            billAdapter.indicesOfSelectedItems = indicesOfDueInvoicesSelectedForPayments;

            billAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int pos) {

                    status = feeManager.getFeesStatus(dueFeeList.get(pos));

                    //Here get currency according to currencycode
                    currencySymbol = getCurrencySymbol(pos, dueFeeList);

                    JSONObject jsonObject = dueFeeList.get(pos);

                    //Here show EditOption
                    showEditOption2(view, currencySymbol, status, true,jsonObject, new NewJSon() {
                        @Override
                        public void getNewJSON(JSONObject jsonObject1) {

                            position = selectedFeeList.indexOf(jsonObject1);

                            if(jsonObject1.has("partAmount")){

                                //Here updated selected row
                                selectedFeeList.set(position,jsonObject1);
                            }

                            updateGrandTotal(selectedFeeList);
                        }
                    });
                }
            });

        } else {

            btnPayClick(false);

            billAdapter = new BillAdapter(mContext, dueFeeList, indicesOfDueInvoicesSelectedForPayments);
        }

        tvForPayableAmount.setText(String.valueOf(totalPayableAmount));
        recyclerView.setAdapter(billAdapter);

        billAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                status = feeManager.getFeesStatus(dueFeeList.get(pos));

                //Here get currency according to currencycode
                currencySymbol = getCurrencySymbol(pos, dueFeeList);

                boolean isPaymentEnable = checkCurrencyId(pos, dueFeeList);

                if(isPaymentEnable){

                    if (indicesOfDueInvoicesSelectedForPayments.contains(pos))
                    {
                        indicesOfDueInvoicesSelectedForPayments.remove(Integer.valueOf(pos));
                        ImageView iv = view.findViewById(R.id.ivCheckBox);
                        iv.setImageResource(R.drawable.unchecked);
                        JSONObject jsonObject = dueFeeList.get(pos);

                        //Here hide EditOption
                        showEditOption(view, currencySymbol, status, false, jsonObject, new NewJSon() {
                            @Override
                            public void getNewJSON(JSONObject jsonObject1) {

                                selectedFeeList.remove(jsonObject1);

                                //If Select All Checkbox checked and remove any bill from list
                                if(cbForSelectAll.isChecked()){
                                    cbForSelectAll.setChecked(false);
                                }

                                updateGrandTotal(selectedFeeList);

                                RelativeLayout rlParent = view.findViewById(R.id.rlParent);
                                rlParent.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                                billAdapter.indicesOfSelectedItems = indicesOfDueInvoicesSelectedForPayments;
                            }
                        });

                    }
                    else
                    {
                        indicesOfDueInvoicesSelectedForPayments.add(Integer.valueOf(pos));
                        ImageView iv = view.findViewById(R.id.ivCheckBox);
                        iv.setImageResource(R.drawable.checked);
                        JSONObject jsonObject = dueFeeList.get(pos);

                        //Here add selected row
                        selectedFeeList.add(jsonObject);

                        //Here show EditOption
                        showEditOption(view, currencySymbol, status, true, jsonObject, new NewJSon() {
                            @Override
                            public void getNewJSON(JSONObject jsonObject1) {

                                position = selectedFeeList.indexOf(jsonObject1);

                                if(jsonObject1.has("partAmount")){

                                    //Here updated selected row
                                    selectedFeeList.set(position,jsonObject1);
                                }

                                updateGrandTotal(selectedFeeList);

                                RelativeLayout rlParent = view.findViewById(R.id.rlParent);
                                rlParent.setBackgroundColor(getResources().getColor(R.color.colorBlueTransparent));

                                billAdapter.indicesOfSelectedItems = indicesOfDueInvoicesSelectedForPayments;
                            }
                        });
                    }
                }else{
                    ProjectUtils.showLong(mContext,getString(R.string.you_cannot_payment));
                }
            }
        });
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

    private String getCurrencySymbol(int i, List<JSONObject> feeList) {

        String currency = "";
        String id = String.valueOf(feeManager.getCurrencyId(feeList.get(i)));
        ArrayList<CurrencyDto> currencyList = dbHelper.getAllData();

        if(currencyList != null && currencyList.size() > 0){
            for(CurrencyDto currencyDto : currencyList){
                if(currencyDto.getId().contains(id)){
                    String curCode = currencyDto.getCurrencyCode();
                    currency = BaseActivity.Utils.getCurrencySymbol(curCode);
                }
            }
        }
        else{
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
            currency = sharedPrefrenceManager.getCurrencySymbolFromKey();
        }

        return currency;
    }

    private void updateGrandTotal(List<JSONObject> feeList){

        double grandTotal = 0.0;
        double totalPayable = 0.0;

        try {
            for(int i = 0 ; i < feeList.size() ; i++){

                JSONObject jsonObject = feeList.get(i);

                if(jsonObject.has("partAmount")){

                    double partAmount = jsonObject.optDouble("partAmount");
                    totalPayable = totalPayable + partAmount;

                }else{

                    double balanceAmount = jsonObject.optDouble("balanceAmount");

                    totalPayable = totalPayable + balanceAmount;
                }

                double balanceAmount = jsonObject.optDouble("balanceAmount");
                grandTotal = grandTotal + balanceAmount;
            }

            tvForPayableAmount.setText(String.valueOf(totalPayable));

            if (totalPayable > 0.0) {

                btnPayClick(true);
            } else {

                btnPayClick(false);
            }

            totalPayableAmount = totalPayable;

        }catch (Exception ex){
            ex.printStackTrace();
            ProjectUtils.showLog(TAG,""+ex.getMessage());
        }
    }

    public interface NewJSon{
        void getNewJSON(JSONObject jsonObject);
    }

    private void showEditOption(View view, String currencySymbol, String status, boolean isShow, JSONObject jsonObject, NewJSon newJSon){

        boolean allowPartialPaymentThroughMobileApp = false;

        ImageView ivEdit = view.findViewById(R.id.ivEdit);
        TextView tvDivider = view.findViewById(R.id.tvDivider);
        TextView tvPartAmount = view.findViewById(R.id.tvPartAmount);

        if(isShow){

            //Pre entered part amount
            if(jsonObject.has("partAmount")){

                double partialAmount = jsonObject.optDouble("partAmount");

                tvDivider.setVisibility(View.VISIBLE);
                tvPartAmount.setVisibility(View.VISIBLE);
                tvPartAmount.setText(currencySymbol + partialAmount);
                ivEdit.setVisibility(View.VISIBLE);
            }

            if(status.equalsIgnoreCase("Fully Pending") || status.equalsIgnoreCase("Partially Settled")){

                allowPartialPaymentThroughMobileApp = jsonObject.optBoolean("allowPartialPaymentThroughMobileApp");

                if(allowPartialPaymentThroughMobileApp){

                    ivEdit.setVisibility(View.VISIBLE);

                    ivEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showPartAmountDialog(currencySymbol, jsonObject, new NewJSon() {
                                @Override
                                public void getNewJSON(JSONObject jsonObject1) {

                                    newJSon.getNewJSON(jsonObject1);

                                    if(jsonObject1.has("partAmount")){

                                        double partialAmount = jsonObject1.optDouble("partAmount");

                                        tvDivider.setVisibility(View.VISIBLE);
                                        tvPartAmount.setVisibility(View.VISIBLE);
                                        tvPartAmount.setText(currencySymbol + partialAmount);
                                        ivEdit.setVisibility(View.VISIBLE);
                                    }else{
                                        tvDivider.setVisibility(View.GONE);
                                        tvPartAmount.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    });

                }else{
                    ivEdit.setVisibility(View.GONE);
                }
            }
        }else{

            tvDivider.setVisibility(View.GONE);
            tvPartAmount.setVisibility(View.GONE);
            ivEdit.setVisibility(View.GONE);
        }

        newJSon.getNewJSON(jsonObject);
    }

    private void showEditOption2(View view, String currencySymbol, String status, boolean isShow, JSONObject jsonObject, NewJSon newJSon){

        ImageView ivEdit = view.findViewById(R.id.ivEdit);
        TextView tvDivider = view.findViewById(R.id.tvDivider);
        TextView tvPartAmount = view.findViewById(R.id.tvPartAmount);

        if(isShow){

            //Pre entered part amount
            if(jsonObject.has("partAmount")){

                double partialAmount = jsonObject.optDouble("partAmount");

                tvDivider.setVisibility(View.VISIBLE);
                tvPartAmount.setVisibility(View.VISIBLE);
                tvPartAmount.setText(currencySymbol + partialAmount);
                ivEdit.setVisibility(View.VISIBLE);
            }

            if(status.equalsIgnoreCase("Fully Pending") || status.equalsIgnoreCase("Partially Settled")){

                showPartAmountDialog(currencySymbol, jsonObject, new NewJSon() {
                    @Override
                    public void getNewJSON(JSONObject jsonObject1) {

                        newJSon.getNewJSON(jsonObject1);

                        if(jsonObject1.has("partAmount")){

                            double partialAmount = jsonObject1.optDouble("partAmount");

                            tvDivider.setVisibility(View.VISIBLE);
                            tvPartAmount.setVisibility(View.VISIBLE);
                            tvPartAmount.setText(currencySymbol + partialAmount);
                            ivEdit.setVisibility(View.VISIBLE);
                        }else{
                            tvDivider.setVisibility(View.GONE);
                            tvPartAmount.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }else{

            tvDivider.setVisibility(View.GONE);
            tvPartAmount.setVisibility(View.GONE);
            ivEdit.setVisibility(View.GONE);
        }

        newJSon.getNewJSON(jsonObject);
    }

    private void showPartAmountDialog(String currencySymbol,JSONObject jsonObject, NewJSon newJson1) {

        Dialog alertDialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.fees_part_amount_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        AppCompatImageView ivClose = convertView.findViewById(R.id.ivClose);

        TextView tvTotalAmount1 = convertView.findViewById(R.id.tvTotalAmount1);
        TextView tvBalanceAmount1 = convertView.findViewById(R.id.tvBalanceAmount1);
        TextView tvEnterAmount1 = convertView.findViewById(R.id.tvEnterAmount1);
        TextView tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
        TextView tvBalanceAmount = convertView.findViewById(R.id.tvBalanceAmount);
        EditText etAmount = convertView.findViewById(R.id.etAmount);
        Button btnDone = convertView.findViewById(R.id.btnDone);

        titleTextView.setText("EDIT AMOUNT");
        tvTotalAmount1.setText(translationManager.TOTAL_AMOUNT_KEY);
        tvBalanceAmount1.setText(translationManager.BALANCE_AMOUNT_KEY);

        if(jsonObject.has("partAmount")){

            double partialAmount = jsonObject.optDouble("partAmount");
            etAmount.setText(String.valueOf(partialAmount));
        }

        double balanceAmount = jsonObject.optDouble("balanceAmount");
        double totalAmount = jsonObject.optDouble("totalAmount");

        tvTotalAmount.setText(currencySymbol + totalAmount);
        tvBalanceAmount.setText(currencySymbol + balanceAmount);

        ivClose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.hideKeyboard2(mContext,btnDone);

                if(etAmount.getText().toString().length() != 0){

                    double partAmount = Double.parseDouble(etAmount.getText().toString().trim());

                    //if partamount less than balance amount
                    if(partAmount <= balanceAmount) {

                        alertDialog.dismiss();

                        double remainBalance = balanceAmount - partAmount;

                        try {
                            jsonObject.put("partAmount", partAmount);
                            jsonObject.put("remainBalance", remainBalance);
                            newJson1.getNewJSON(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{

                        ProjectUtils.showDialog(mContext, "", getString(R.string.amount_entered_cannot_be_greater_than_the_balance_amount), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        },false);
                    }

                }else{

                    alertDialog.dismiss();

                    jsonObject.remove("partAmount");
                    jsonObject.remove("remainBalance");
                    newJson1.getNewJSON(jsonObject);
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
    }

    private void populateAllDuesCleared()
    {
        totalFeeAmount = 0;
        for (int i = 0; i < dueFeeList.size(); i++)
        {
            totalFeeAmount += feeManager.getBalanceAmount(dueFeeList.get(i));
        }
        if (totalFeeAmount > 0.0)
        {
            allDuesCleared = false;
        } else {
            allDuesCleared = true;
        }
    }

    public void initiatePayTMTransaction()
    {
        // BEGIN TRANSACTION:
        if (Flags.ENABLE_PAYTM_PRODUCTION)
        {
            PaytmPGService Service = PaytmPGService.getProductionService();
            HashMap<String, String> paramMap = new HashMap<String,String>();
            paramMap.put( "MID" , getMID());
            paramMap.put( "ORDER_ID" , getORDER_ID());
            paramMap.put( "CUST_ID" , getCUST_ID());
            paramMap.put( "INDUSTRY_TYPE_ID" , getINDUSTRY_TYPE_ID());
            paramMap.put( "CHANNEL_ID" , getCHANNEL_ID());
            paramMap.put( "TXN_AMOUNT" , getTXN_AMOUNT());
            paramMap.put( "WEBSITE" , getWEBSITE());
            paramMap.put( "CALLBACK_URL", getCALLBACK_URL());
            paramMap.put( "CHECKSUMHASH" , CHECKSUMHASH);
            PaytmOrder Order = new PaytmOrder(paramMap);
            Service.initialize(Order, null);
            Service.startPaymentTransaction(mContext, true, false, this);
        } else {
            PaytmPGService Service = PaytmPGService.getStagingService();
            HashMap<String, String> paramMap = new HashMap<String,String>();
            paramMap.put( "MID" , getMID());
            paramMap.put( "ORDER_ID" , getORDER_ID());
            paramMap.put( "CUST_ID" , getCUST_ID());
            paramMap.put( "INDUSTRY_TYPE_ID" , getINDUSTRY_TYPE_ID());
            paramMap.put( "CHANNEL_ID" , getCHANNEL_ID());
            paramMap.put( "TXN_AMOUNT" , getTXN_AMOUNT());
            paramMap.put( "WEBSITE" , getWEBSITE());
            paramMap.put( "CALLBACK_URL", getCALLBACK_URL());
            paramMap.put( "CHECKSUMHASH" , CHECKSUMHASH);
            PaytmOrder Order = new PaytmOrder(paramMap);
            Service.initialize(Order, null);
            Service.startPaymentTransaction(mContext, true, false, this);
        }
    }

    public String getMID() {
        return mid;
    }

    public String getORDER_ID() {
        return "SerosoftOrder" + String.valueOf(paymentID);
    }

    public String getCUST_ID() {
        return "SerosoftCustomer" + String.valueOf(sharedPrefrenceManager.getUserIDFromKey());
    }

    public String getCHANNEL_ID() {
        return channelID;
    }

    public String getTXN_AMOUNT() {
        return String.valueOf(totalPayableAmount);
    }

    public String getWEBSITE() {
        return website;
    }

    public String getINDUSTRY_TYPE_ID() {
        return industryType;
    }

    public String getCALLBACK_URL() {
        return callbackURL + getORDER_ID();
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        /*Display the message as below */
        String transactionStatus = inResponse.getString("RESPCODE");
        String transactionMessage = inResponse.getString("RESPMSG");

        if (transactionStatus.equals("01")) {
            //SUCCESS
            BANKTXNID = inResponse.getString("BANKTXNID");
            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(mContext,
                    this, KEYS.SWITCH_TRANSACTION_ID_UPDATE).execute(String.valueOf(paymentID), String.valueOf(BANKTXNID));
        } else {
            ProjectUtils.showLong(mContext,transactionMessage);
        }
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        /*Display the error message as below */
        Toast.makeText(mContext, "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        /*Display the message as below */
        Toast.makeText(mContext, "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage)  {
        /*Display the message as below */
        Toast.makeText(mContext, "Authentication failed: Server error " + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl)  {
        /*Display the message as below */
        Toast.makeText(mContext, "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction(){
        /*Display the message as below */
        Toast.makeText(mContext, "Transaction cancelled" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(mContext, "Transaction cancelled" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {

        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        switch (callFor) {

            case KEYS.SWITCH_PENDING_BILLS:

                hideProgressDialog();
                dueFeeList = new ArrayList<>();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever")) {
                        arr = responseObject.getJSONArray("whatever");
                    } else {
                        arr = responseObject.getJSONArray("rows");
                    }

                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            dueFeeList.add(obj);
                        }
                    }

                    setAdapter();

                } catch (Exception e) {
                    e.printStackTrace();

                    setAdapter();
                    hideProgressDialog();
                }
                break;

            case KEYS.SWITCH_PAYMENT_GATEWAY:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult);

                    if (jsonObject.has("environment")) {

                        mid = jsonObject.optString("mid");
                        channelID = jsonObject.optString("channelID");
                        website = jsonObject.optString("website");
                        industryType = jsonObject.optString("industryType");
                        callbackURL = jsonObject.optString("callbackURL");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_PAYMENT_ID:
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("firebaseID")) {
                        paymentID = responseObject.optInt("firebaseID");
                        new OptimizedServerCallAsyncTask(mContext,
                                this, KEYS.SWITCH_PAYTM_GET_CHECKSUMHASH).
                                execute(
                                        getMID(),
                                        getORDER_ID(),
                                        getCUST_ID(),
                                        getINDUSTRY_TYPE_ID(),
                                        getCHANNEL_ID(),
                                        getTXN_AMOUNT(),
                                        getWEBSITE(),
                                        getCALLBACK_URL(),
                                        env
                                );
                    } else {
                        hideProgressDialog();
                        ProjectUtils.showLong(mContext,getString(R.string.unexpected_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                }
                break;

            case KEYS.SWITCH_PAYTM_GET_CHECKSUMHASH:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("CHECKSUMHASH")) {
                        CHECKSUMHASH = responseObject.optString("CHECKSUMHASH");
                        initiatePayTMTransaction();
                    } else {
                        ProjectUtils.showLong(mContext,getString(R.string.unexpected_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                }
                break;

            case KEYS.SWITCH_TRANSACTION_ID_UPDATE:
                try {
                    new OptimizedServerCallAsyncTask(mContext,
                            this, KEYS.SWITCH_TRANSACTION_UPDATE).execute(String.valueOf(paymentID));

                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                }
                break;

            case KEYS.SWITCH_TRANSACTION_UPDATE:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult); //1255230
                    if (responseObject.has("firebaseID"))
                    {

                        ProjectUtils.showLong(mContext,"Transaction successful!");
                        Intent intent = new Intent(mContext, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        ProjectUtils.showLong(mContext,getString(R.string.unexpected_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                }
                break;

            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                getNotifications();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}