package com.serosoft.academiassu.Modules.AcademiaDrive.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.RecyclerItemClickListener;
import com.serosoft.academiassu.Modules.AcademiaDrive.Adapters.DriveDocumentAdapter;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFile_Dto;
import com.serosoft.academiassu.Modules.Event.Fragments.EventCurrentFragment;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.io.File;
import java.util.ArrayList;

public class DriveDocumentFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private SuperStateView superStateView;

    BaseActivity baseActivity;
    ArrayList<DriveFile_Dto> documentList = new ArrayList<>();
    DriveDocumentAdapter driveDocumentAdapter;
    GridLayoutManager gridLayoutManager;

    String fileURL = "";
    String documentName = "";
    String fileType = "";
    String pathName = "";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = EventCurrentFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drive_file_fragment, container, false);
        ProjectUtils.showLog(TAG,"onCreateView start");

        mContext = getActivity();
        baseActivity = new BaseActivity();

        Initialize(v);

        checkEmpty(false);

        Bundle bundle = getArguments();
        if(bundle != null){
            documentList = new ArrayList<>();
            documentList = (ArrayList<DriveFile_Dto>) bundle.getSerializable("DocumentList");
            ProjectUtils.showLog(TAG,""+documentList.size());
        }

        if(documentList != null && documentList.size() > 0){

            checkEmpty(false);

            driveDocumentAdapter = new DriveDocumentAdapter(mContext,documentList);
            recyclerView.setAdapter(driveDocumentAdapter);
            driveDocumentAdapter.notifyDataSetChanged();

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    DriveFile_Dto list = documentList.get(position);

                    downloadDocument(list);
                }

                @Override
                public void onItemDoubleClick(View view, int position) {}

                @Override
                public void onItemLongClick(View view, int position) {}
            }));
        }
        else{

            checkEmpty(true);
        }

        return v;
    }

    private void downloadDocument(DriveFile_Dto list) {

        documentName = ProjectUtils.getCorrectedString(list.getImageName());
        fileType = ProjectUtils.getCorrectedString(list.getFileType());
        pathName = ProjectUtils.getCorrectedString(list.getPath());

        fileURL = pathName;
        fileURL = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + pathName;

        if (ProjectUtils.hasPermissionInManifest2(DriveDocumentFragment.this,PERMISSION_CODE,permission))
        {
            downloadTask(fileURL,Consts.ACADEMIA_DRIVE,documentName,fileType);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasAllPermissions = false;

        switch (requestCode)
        {
            case PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    hasAllPermissions = true;
                }
                else
                {
                    hasAllPermissions = false;

                    ProjectUtils.showDialog(mContext, getString(R.string.app_name), getString(R.string.permission_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (ProjectUtils.hasPermissionInManifest2(DriveDocumentFragment.this,PERMISSION_CODE,permission))
                            {
                                downloadTask(fileURL,Consts.ACADEMIA_DRIVE,documentName,fileType);
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },false);
                }

                if(hasAllPermissions) downloadTask(fileURL,Consts.ACADEMIA_DRIVE,documentName,fileType);

                break;
        }
    }

    private void downloadTask(String fileURL,String screnName, String documentName,String fileType) {

        //Here create folder
        String filePath = Environment.getExternalStorageDirectory() + File.separator + Consts.ACADEMIA + File.separator + Consts.MEDIA + File.separator + screnName;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //Here create file
        String fileName = File.separator +documentName +"."+ fileType;

        baseActivity.showProgressDialog(mContext);

        AndroidNetworking.download(fileURL,filePath,fileName)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        baseActivity.hideProgressDialog();

                        String path = filePath+fileName;
                        String message = "Document downloaded to Internal Storage" + path;
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                        File file = new File(path);
                        baseActivity.openFile(file,mContext);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        baseActivity.hideProgressDialog();
                    }
                });
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

    private void Initialize(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);
        superStateView = v.findViewById(R.id.superStateView);

        gridLayoutManager = new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }
}
