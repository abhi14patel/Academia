package com.serosoft.academiassu.Utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PDFActivity2 extends AppCompatActivity
{
    Context mContext;
    private Toolbar toolbar;
    PDFView pdfView;
    String filePath;
    String screnName;
    String folderName;
    String idForDocument;
    int color ;

    ArrayList<Integer> list = new ArrayList<>();
    int PDF_VIEW = 0;

    SharedPrefrenceManager sharedPrefrenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf2);

        mContext = PDFActivity2.this;

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);

        filePath = getIntent().getStringExtra("filePath");
        folderName = getIntent().getStringExtra("folderName");
        idForDocument = getIntent().getStringExtra("idForDocument");
        screnName = getIntent().getStringExtra("screenName");
        color = getIntent().getIntExtra("headerColor",R.color.colorFees);

        initView();

        permissionSetup();

        if(PDF_VIEW!=0){
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            }
        }

        try
        {
            decryptFromPath(filePath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ProjectUtils.showLog("TAG",""+e.getMessage());
        }
    }

    private void permissionSetup() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        PDF_VIEW = 0;

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_PDF_DOWNLOAD:
                case Permissions.STUDENT_PERMISSION_PDF_DOWNLOAD:
                    PDF_VIEW = 1;
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(PDF_VIEW != 0){
            getMenuInflater().inflate(R.menu.pdf_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.pdf_share_button)
        {
            try
            {
                decryptFromPathAndDownload(filePath,folderName);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    void initView()
    {
        pdfView = findViewById(R.id.pdfView);
        toolbar = findViewById(R.id.group_toolbar);
        toolbar.setTitle(getResources().getString(R.string.fees).toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setBackgroundColor(ContextCompat.getColor(this, color));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }

        toolbar.setTitle(screnName.toUpperCase());
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    void decryptFromPath(String path) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
        FileInputStream fis = new FileInputStream(path);

        SecretKeySpec sks = new SecretKeySpec(KEYS.ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        pdfView.fromStream(cis)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(false)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(0)
                .load();
    }

    void decryptFromPathAndDownload(String path,String folderName) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        FileInputStream fis = new FileInputStream(path);

        SecretKeySpec sks = new SecretKeySpec(KEYS.ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        String userName = sharedPrefrenceManager.getUserCodeFromKey();

        boolean formatString = userName.contains("/");
        if(formatString)
        {
            String[] arr = userName.split("/");
            userName = arr[1];
        }

        //Here create a folder where bitmap image saved
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + Consts.ACADEMIA + File.separator + Consts.MEDIA + File.separator + folderName);
        // Create the storage directory if it does not exist
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        String fileName = userName + "_" + screnName+ "_" + idForDocument + ".pdf";
        File mediaFile = new File(dir.getAbsoluteFile() + File.separator + fileName);

        FileOutputStream fileOutput = new FileOutputStream(mediaFile);
        int b;
        byte[] d = new byte[8];
        while((b = cis.read(d)) != -1)
        {
            fileOutput.write(d, 0, b);
        }
        fileOutput.flush();
        fileOutput.close();
        cis.close();

        String message = "Document downloaded to Internal" + mediaFile;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
