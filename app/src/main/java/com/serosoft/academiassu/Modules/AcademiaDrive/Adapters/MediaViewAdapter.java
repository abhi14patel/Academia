package com.serosoft.academiassu.Modules.AcademiaDrive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFile_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Networking.KEYS;

import java.util.ArrayList;

public class MediaViewAdapter extends PagerAdapter
{
    Context context;
    ArrayList<DriveFile_Dto> imageList;
    LayoutInflater mLayoutInflater;

    public MediaViewAdapter(Context context,ArrayList<DriveFile_Dto> imageList)
    {
        this.context=context;
        this.imageList = imageList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View itemView = mLayoutInflater.inflate(R.layout.drive_media_view_image, container, false);

        PhotoView photoImageView = itemView.findViewById(R.id.photoImageView);

        DriveFile_Dto list = imageList.get(position);

        String imageURL = list.getPath();
        imageURL = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + imageURL;

        if(!imageURL.equalsIgnoreCase(""))
        {
            Glide.with(context).load(imageURL).into(photoImageView);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        ((ViewPager) container).removeView((View) object);
    }
}
