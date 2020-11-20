package com.serosoft.academiassu.Modules.AcademiaDrive;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.RecyclerItemClickListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.AcademiaDrive.Adapters.DriveFolderAdapter;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFolder_Dto;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.NameValue_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Abhishek on March 31 2020.
 */

public class DriveMainActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;
    private AppCompatImageView ivSort;
    private TextView tvItemCount;

    private RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    DriveFolderAdapter driveFolderAdapter;
    ArrayList<DriveFolder_Dto> driveFolderList;
    ArrayList<NameValue_Dto> nameValueList;

    private final String TAG = DriveMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_main_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = DriveMainActivity.this;

        Initialize();

        populateContents();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.academia_drive), bundle);
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_ACADEMIA_DRIVE_FOLDER).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        superStateView = findViewById(R.id.superStateView);
        recyclerView = findViewById(R.id.recyclerView);
        ivSort = findViewById(R.id.ivSort);
        tvItemCount = findViewById(R.id.tvItemCount);

        tvItemCount.setText("0 Items");

        toolbar.setTitle(translationManager.ACADEMIA_DRIVE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorDrive, toolbar, this); }

        gridLayoutManager = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(mContext, ivSort);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.sort_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        switch (id){

                            case R.id.sortName:{
                                //Here sort arraylist by name
                                if(driveFolderList != null && driveFolderList.size() > 0){
                                    Collections.sort(driveFolderList, DriveFolder_Dto.sortByName);
                                    driveFolderAdapter.notifyDataSetChanged();
                                }
                            }break;

                            case R.id.sortDate:{
                                //Here sort arraylist by date
                                if(driveFolderList != null && driveFolderList.size() > 0){
                                    Collections.sort(driveFolderList, DriveFolder_Dto.sortByDate);
                                    driveFolderAdapter.notifyDataSetChanged();
                                }
                            }break;
                        }
                        return true;
                    }
                });

                //Here show icons
                insertMenuItemIcons(mContext, popup);

                popup.show();//showing popup menu
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && ivSort.getVisibility() == View.VISIBLE) {
                    ivSort.setVisibility(View.INVISIBLE);
                } else if (dy < 0 && ivSort.getVisibility() != View.VISIBLE) {
                    ivSort.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Moves icons from the PopupMenu's MenuItems' icon fields into the menu title as a Spannable with the icon and title text.
     */
    public void insertMenuItemIcons(Context context, PopupMenu popupMenu) {
        Menu menu = popupMenu.getMenu();
        if (hasIcon(menu)) {
            for (int i = 0; i < menu.size(); i++) {
                insertMenuItemIcon(context, menu.getItem(i));
            }
        }
    }

    /**
     * @return true if the menu has at least one MenuItem with an icon.
     */
    private boolean hasIcon(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getIcon() != null) return true;
        }
        return false;
    }

    /**
     * Converts the given MenuItem's title into a Spannable containing both its icon and title.
     */
    private void insertMenuItemIcon(Context context, MenuItem menuItem) {

        Drawable icon = menuItem.getIcon();

        // If there's no icon, we insert a transparent one to keep the title aligned with the items
        // which do have icons.
        if (icon == null) icon = new ColorDrawable(Color.TRANSPARENT);

        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.menu_item_icon_size);
        icon.setBounds(0, 0, iconSize, iconSize);
        ImageSpan imageSpan = new ImageSpan(icon);

        // Add a space placeholder for the icon, before the title.
        SpannableStringBuilder ssb = new SpannableStringBuilder("       " + menuItem.getTitle());

        // Replace the space placeholder with the icon.
        ssb.setSpan(imageSpan, 1, 2, 0);
        menuItem.setTitle(ssb);
        // Set the icon to null just in case, on some weird devices, they've customized Android to display
        // the icon in the menu... we don't want two icons to appear.
        menuItem.setIcon(null);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }

        switch (callFor) {
            case KEYS.SWITCH_ACADEMIA_DRIVE_FOLDER:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    if (responseObject.has("whatever"))
                    {
                        arr = responseObject.getJSONArray("whatever");
                    } else {
                        arr = responseObject.getJSONArray("rows");
                    }

                    if (arr != null && arr.length() > 0) {

                        checkEmpty(false);
                        driveFolderList = new ArrayList<>();

                        for(int i = 0 ; i< arr.length() ; i++) {

                            JSONObject jsonObject1 = arr.optJSONObject(i);

                            int id = jsonObject1.optInt("id");
                            String albumName = jsonObject1.optString("albumName");
                            String description = jsonObject1.optString("description");
                            long createdDate = jsonObject1.optLong("createdDate");

                            JSONArray wsNameValue = jsonObject1.optJSONArray("wsNameValue");
                            nameValueList = new ArrayList<>();

                            if(wsNameValue != null && wsNameValue.length() > 0){

                                for(int j = 0 ; j < wsNameValue.length() ; j++){
                                    JSONObject jsonObject2 = wsNameValue.optJSONObject(j);

                                    String value = jsonObject2.optString("value");
                                    String code = jsonObject2.optString("code");

                                    NameValue_Dto nameValue_dto = new NameValue_Dto(value,code);
                                    nameValueList.add(nameValue_dto);
                                }
                            }

                            DriveFolder_Dto driveFolder_dto = new DriveFolder_Dto(albumName,description,id,createdDate,nameValueList);
                            driveFolderList.add(driveFolder_dto);
                        }

                        int count = driveFolderList.size();
                        if(count==1){
                            tvItemCount.setText(""+count+" Item");
                        }else if(count > 1){
                            tvItemCount.setText(""+count+" Items");
                        }else{
                            tvItemCount.setText("0 Items");
                        }

                        driveFolderAdapter = new DriveFolderAdapter(mContext,driveFolderList);
                        recyclerView.setAdapter(driveFolderAdapter);
                        driveFolderAdapter.notifyDataSetChanged();

                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,
                                new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                DriveFolder_Dto list = driveFolderList.get(position);

                                Intent intent = new Intent(mContext,DriveTabActivity.class);
                                intent.putExtra("folder",list);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemDoubleClick(View view, int position) {}

                            @Override
                            public void onItemLongClick(View view, int position) {

                                DriveFolder_Dto list = driveFolderList.get(position);

                                showDetailsDialog(list);
                            }
                        }));
                    } else {
                        checkEmpty(true);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    checkEmpty(true);
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

    private void showDetailsDialog(DriveFolder_Dto list) {

        Dialog alertDialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.academia_drive_detail, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        AppCompatImageView ivClose = (AppCompatImageView) convertView.findViewById(R.id.ivClose);
        TextView tvFolderName = (TextView) convertView.findViewById(R.id.tvFolderName);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvPostedBy = (TextView) convertView.findViewById(R.id.tvPostedBy);

        String folderName = ProjectUtils.getCorrectedString(list.getAlbumName());
        String description = ProjectUtils.getCorrectedString(list.getDescription());

        ArrayList<NameValue_Dto> namelist = list.getNameValueList();
        String postedBy = ProjectUtils.getCorrectedString(namelist.get(0).getValue());

        tvFolderName.setText(folderName);
        tvDescription.setText(description);
        tvPostedBy.setText(postedBy);

        ivClose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                onBackPressed();
                return true;
            case R.id.refresh:
                getNotifications();
                populateContents();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
