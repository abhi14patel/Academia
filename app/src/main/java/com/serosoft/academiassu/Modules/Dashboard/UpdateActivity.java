package com.serosoft.academiassu.Modules.Dashboard;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateActivity extends AppCompatActivity
{
    Context mContext;
    private TextView textViewForUpdateDetail;
    private TextView textViewForUpdateTitle;
    private TextView textViewForFeatures;

    private Button buttonForUpdateNow;
    private Button buttonForLater;
    private ImageButton closeButton;

    Boolean isForced;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mContext = UpdateActivity.this;

        Initialize();

        try
        {
            String str = getIntent().getStringExtra("json");
            JSONObject jsonObject = new JSONObject(str);

            String latestVersion = jsonObject.optString("latestVersion");
            textViewForUpdateTitle.setText("New Version " + latestVersion);
            textViewForUpdateDetail.setText(getApplicationName(this) + " " + latestVersion + " is now available for download!");

            JSONArray array = jsonObject.optJSONArray("mobileFeatureDetails");

            String textForFeatures = "";
            for (int i = 0 ; i < array.length() ; ++ i)
            {
                JSONObject jsonObject1 = array.optJSONObject(i);
                String featureTitle = jsonObject1.optString("featureName");
                String featureDetail = jsonObject1.optString("featureDetail");

                textForFeatures = textForFeatures + "<p>" + featureDetail + "</p><br>";
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                textViewForFeatures.setText(Html.fromHtml(textForFeatures, Html.FROM_HTML_MODE_COMPACT));
            }
            else
            {
                textViewForFeatures.setText(Html.fromHtml(textForFeatures));
            }

            isForced = jsonObject.optBoolean("isForceUpdate");

            if (isForced)
            {
                buttonForLater.setVisibility(View.GONE);
                closeButton.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void Initialize()
    {
        textViewForUpdateTitle = findViewById(R.id.textViewForUpdateTitle);
        textViewForUpdateDetail = findViewById(R.id.textViewForUpdateDetail);
        textViewForFeatures = findViewById(R.id.textViewForFeatures);
        closeButton = findViewById(R.id.closeButton);
        buttonForLater = findViewById(R.id.buttonForLater);
        buttonForUpdateNow = findViewById(R.id.buttonForUpdateNow);

        textViewForFeatures.setMovementMethod(new ScrollingMovementMethod());

        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        buttonForLater.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        buttonForUpdateNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(Flags.IS_APP_LIVE){
                    Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                    }
                }else{
                    ProjectUtils.openBrowser(mContext, BaseURL.BASE_URL);
                }
            }
        });
    }

    public static String getApplicationName(Context context)
    {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    @Override
    public void onBackPressed()
    {
        if (!isForced)
        {
            super.onBackPressed();
        }
    }
}

