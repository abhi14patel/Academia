package com.serosoft.academiassu.Modules.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction.DisciplinaryActionListActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DocumentsActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers.FeePayersActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.MedicalConditionActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.ParentsDetailActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.PersonalInformationActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class ProfileInformationFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private CardView cardPersonalDetails,cardAddress,cardParentsDetails,cardMedicalCondition,cardDisciplinaryAction,cardDocuments,cardFeePayer;
    private TextView tvPersonalDetails1,tvAddress1,tvParentsDetails1,tvMedicalCondition1,tvDocuments1,tvFeePayer1,tvDisciplinaryAction1;

    SharedPrefrenceManager sharedPrefrenceManager;
    TranslationManager translationManager;
    private SuperStateView superStateView;

    ArrayList<Integer> list = new ArrayList<>();

    private final String TAG = ProfileInformationFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.profile_information_fragment, container, false);
        ProjectUtils.showLog(TAG,"onCreateView Start");

        mContext = getActivity();

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        translationManager = new TranslationManager(mContext);

        Initialize(v);

        if(Flags.DISEABLE_PERMISSION_FOR_TAIPEI){

            cardPersonalDetails.setVisibility(View.VISIBLE);
            cardDocuments.setVisibility(View.VISIBLE);
            cardMedicalCondition.setVisibility(View.VISIBLE);
            cardParentsDetails.setVisibility(View.VISIBLE);
            cardDisciplinaryAction.setVisibility(View.VISIBLE);
            cardFeePayer.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.GONE);

        }else{
            permissionSetup();
        }

        return v;
    }

    private void permissionSetup() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        cardPersonalDetails.setVisibility(View.GONE);
        cardDocuments.setVisibility(View.GONE);
        cardMedicalCondition.setVisibility(View.GONE);
        cardParentsDetails.setVisibility(View.GONE);
        cardDisciplinaryAction.setVisibility(View.GONE);
        cardFeePayer.setVisibility(View.GONE);
        superStateView.setVisibility(View.VISIBLE);

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_PROFILE_PERSONAL_DETAILS_VIEW:
                case Permissions.STUDENT_PERMISSION_PROFILE_PERSONAL_DETAILS_VIEW:
                    cardPersonalDetails.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_PERMISSION_PROFILE_DOCUMENTS_VIEW:
                case Permissions.STUDENT_PERMISSION_PROFILE_DOCUMENTS_VIEW:
                    cardDocuments.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_MEDICAL_CONDITION_VIEW:
                case Permissions.STUDENT_MEDICAL_CONDITION_VIEW:
                    cardMedicalCondition.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_PERMISSION_PROFILE_PARENT_DETAILS_VIEW:
                case Permissions.STUDENT_PERMISSION_PROFILE_PARENT_DETAILS_VIEW:
                    cardParentsDetails.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_DISCIPLINARY_ACTION_VIEW:
                case Permissions.STUDENT_DISCIPLINARY_ACTION_VIEW:
                    cardDisciplinaryAction.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_FEE_PAYER_DETAILS_VIEW:
                case Permissions.STUDENT_FEE_PAYER_DETAILS_VIEW:
                    cardFeePayer.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void Initialize(View v) {
        cardPersonalDetails = v.findViewById(R.id.cardPersonalDetails);
        cardAddress = v.findViewById(R.id.cardAddress);
        cardParentsDetails = v.findViewById(R.id.cardParentsDetails);
        cardMedicalCondition = v.findViewById(R.id.cardMedicalCondition);
        cardDisciplinaryAction = v.findViewById(R.id.cardDisciplinaryAction);
        cardDocuments = v.findViewById(R.id.cardDocuments);
        cardFeePayer = v.findViewById(R.id.cardFeePayer);
        superStateView = v.findViewById(R.id.superStateView);

        tvPersonalDetails1 = v.findViewById(R.id.tvPersonalDetails1);
        tvAddress1 = v.findViewById(R.id.tvAddress1);
        tvParentsDetails1 = v.findViewById(R.id.tvParentsDetails1);
        tvMedicalCondition1 = v.findViewById(R.id.tvMedicalCondition1);
        tvDocuments1 = v.findViewById(R.id.tvDocuments1);
        tvDisciplinaryAction1 = v.findViewById(R.id.tvDisciplinaryAction1);
        tvFeePayer1 = v.findViewById(R.id.tvFeePayer1);

        cardPersonalDetails.setOnClickListener(this);
        cardAddress.setOnClickListener(this);
        cardParentsDetails.setOnClickListener(this);
        cardMedicalCondition.setOnClickListener(this);
        cardDisciplinaryAction.setOnClickListener(this);
        cardDocuments.setOnClickListener(this);
        cardFeePayer.setOnClickListener(this);

        tvPersonalDetails1.setText(translationManager.PERSONAL_DETAILS_KEY);
        tvParentsDetails1.setText(translationManager.PARENTS_DETAILS_KEY);
        tvMedicalCondition1.setText(translationManager.MEDICAL_CONDITIONS_KEY);
        tvDocuments1.setText(translationManager.DOCUMENTS_KEY);
        tvDisciplinaryAction1.setText(translationManager.DISCIPLINARY_ACTION_KEY);
        tvFeePayer1.setText(translationManager.FEE_PAYER_DETAILS_KEY);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.cardPersonalDetails:{

                ProjectUtils.preventTwoClick(cardPersonalDetails);
                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardAddress:{

            }break;

            case R.id.cardParentsDetails:{

                ProjectUtils.preventTwoClick(cardParentsDetails);
                Intent intent = new Intent(getActivity(), ParentsDetailActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardMedicalCondition:{

                ProjectUtils.preventTwoClick(cardMedicalCondition);
                Intent intent = new Intent(getActivity(), MedicalConditionActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardDisciplinaryAction:{

                ProjectUtils.preventTwoClick(cardDisciplinaryAction);
                Intent intent = new Intent(getActivity(), DisciplinaryActionListActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardDocuments:{

                ProjectUtils.preventTwoClick(cardDocuments);
                Intent intent = new Intent(getActivity(), DocumentsActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardFeePayer:{

                ProjectUtils.preventTwoClick(cardFeePayer);
                Intent intent = new Intent(getActivity(), FeePayersActivity.class);
                startActivity(intent);
            }break;
        }
    }
}
