package com.serosoft.academiassu.Modules.AcademiaDrive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.serosoft.academiassu.BuildConfig;
import com.serosoft.academiassu.Modules.AcademiaDrive.Adapters.MediaViewAdapter;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFile_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

public class MediaViewActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private Context mContext;
    ArrayList<DriveFile_Dto> imageList = new ArrayList<>();
    int selectedImage = 0;
    private AppCompatImageView ivClose,ivDownload;
    MediaViewAdapter mediaViewAdapter;
    private InkPageIndicator inkPageIndicator;
    Uri fileUri = null;

    private static final String TAG = MediaViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_media_view_activity);
        ProjectUtils.showLog(TAG,"onCreate start");

        mContext = MediaViewActivity.this;

        if (getIntent().hasExtra("list"))
        {
            imageList = (ArrayList<DriveFile_Dto>) getIntent().getSerializableExtra("list");
        }
        if (getIntent().hasExtra("position"))
        {
            selectedImage = getIntent().getIntExtra("position",0);
        }

        Initialize();
    }

    private void Initialize() {

        viewPager = findViewById(R.id.viewPager);
        inkPageIndicator =  findViewById(R.id.indicator);
        ivClose =  findViewById(R.id.ivClose);
        ivDownload =  findViewById(R.id.ivDownload);

        ivClose.setOnClickListener(this);
        ivDownload.setOnClickListener(this);

        mediaViewAdapter = new MediaViewAdapter(mContext,imageList);
        viewPager.setAdapter(mediaViewAdapter);

        inkPageIndicator.setViewPager(viewPager);
        inkPageIndicator.onPageSelected(selectedImage);

        viewPager.setCurrentItem(selectedImage);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {}

            @Override
            public void onPageSelected(int position)
            {
                selectedImage = position;
                inkPageIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {}
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id){
            case R.id.ivDownload : {

                int currentIndex = viewPager.getCurrentItem();
                DriveFile_Dto list = imageList.get(currentIndex);
                String stringForURL = list.getPath();
                stringForURL = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + stringForURL;

                shareItem2(stringForURL);

            }break;

            case R.id.ivClose : {
                onBackPressed();
            }break;
        }
    }

    private void shareItem2(String stringForURL) {

        Picasso.with(mContext).load(stringForURL).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");

                File file = ProjectUtils.getLocalBitmapUri(bitmap, mContext);

                //Here check android version file
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    fileUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                }
                else
                {
                    fileUri = Uri.fromFile(file);
                }

                i.putExtra(Intent.EXTRA_STREAM, fileUri);
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
