package com.serosoft.academiassu.Modules.Exam.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.HallTicketActivity;
import com.serosoft.academiassu.Modules.Exam.MarksheetActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class ExamDocFragment extends Fragment implements View.OnClickListener{

    private Context mContext;
    private CardView cardMarksheet,cardHallTicket;
    private TextView tvMarksheet,tvHallTicket;
    private LinearLayout layoutEmpty;
    private TextView tvErrorDetails;

    TranslationManager translationManager;

    SharedPrefrenceManager sharedPrefrenceManager;
    ArrayList<Integer> list = new ArrayList<>();

    private final String TAG = ExamDocFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.exam_doc_fragment, container, false);
        ProjectUtils.showLog(TAG,"onCreateView");

        mContext = getActivity();

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        translationManager = new TranslationManager(mContext);

        Initialize(v);

        if(Flags.DISEABLE_PERMISSION_FOR_TAIPEI){
            cardMarksheet.setVisibility(View.VISIBLE);
            cardHallTicket.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }else {
            permissionSetup();
        }

        return v;
    }

    private void Initialize(View v) {

        cardMarksheet = v.findViewById(R.id.cardMarksheet);
        cardHallTicket = v.findViewById(R.id.cardHallTicket);
        tvMarksheet = v.findViewById(R.id.tvMarksheet);
        tvHallTicket = v.findViewById(R.id.tvHallTicket);
        layoutEmpty = v.findViewById(R.id.layoutEmpty);
        tvErrorDetails = v.findViewById(R.id.tvErrorDetails);

        tvErrorDetails.setText("No Records Found");
        tvMarksheet.setText(translationManager.MARKSHEET_KEY);
        tvHallTicket.setText(translationManager.HALLTICKET_KEY);

        cardMarksheet.setOnClickListener(this);
        cardHallTicket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.cardMarksheet:{
                ProjectUtils.preventTwoClick(cardMarksheet);
                Intent intent = new Intent(mContext, MarksheetActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardHallTicket:{
                ProjectUtils.preventTwoClick(cardHallTicket);
                Intent intent = new Intent(mContext, HallTicketActivity.class);
                startActivity(intent);
            }break;
        }
    }

    private void permissionSetup() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        cardHallTicket.setVisibility(View.GONE);
        cardMarksheet.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_EXAMINATION_MARKSHEET_VIEW:
                case Permissions.STUDENT_EXAMINATION_MARKSHEET_VIEW:
                    cardMarksheet.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_EXAMINATION_HALLTICKET_VIEW:
                case Permissions.STUDENT_EXAMINATION_HALLTICKET_VIEW:
                    cardHallTicket.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
