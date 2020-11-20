package com.serosoft.academiassu.Modules.AcademiaDrive.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class DriveFolder_Dto implements Serializable {

    private String albumName;
    private String description;
    private int id;
    long createdDate;
    private ArrayList<NameValue_Dto> nameValueList;

    public DriveFolder_Dto(String albumName, String description, int id, long createdDate, ArrayList<NameValue_Dto> nameValueList) {
        this.albumName = albumName;
        this.description = description;
        this.id = id;
        this.createdDate = createdDate;
        this.nameValueList = nameValueList;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public ArrayList<NameValue_Dto> getNameValueList() {
        return nameValueList;
    }

    public static Comparator<DriveFolder_Dto> sortByName = new Comparator<DriveFolder_Dto>()
    {
        @Override
        public int compare(DriveFolder_Dto m1, DriveFolder_Dto m2)
        {
            String s1 = m1.getAlbumName();
            String s2 = m2.getAlbumName();

            return s1.compareToIgnoreCase(s2);
        }
    };

    public static Comparator<DriveFolder_Dto> sortByDate = new Comparator<DriveFolder_Dto>()
    {
        @Override
        public int compare(DriveFolder_Dto m1, DriveFolder_Dto m2)
        {
            Long l1 = m1.getCreatedDate();
            Long l2 = m2.getCreatedDate();

            return l2.compareTo(l1);
        }
    };
}
