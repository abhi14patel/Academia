package com.serosoft.academiassu.Modules.UserProfile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

public class PastHistoryFragment extends Fragment implements View.OnClickListener{

    private Context mContext;
    private RelativeLayout rlEducationDetails,rlWorkExperience,rlExtraCurricular,rlIdentitiesDocuments,rlDisciplinaryActions;

    private final String TAG = PastHistoryFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.past_history_fragment, container, false);
        ProjectUtils.showLog(TAG,"onCreateView Start");

        mContext = getActivity();

        Initialize(v);

        return v;
    }

    private void Initialize(View v) {

        rlEducationDetails = v.findViewById(R.id.rlEducationDetails);
        rlWorkExperience = v.findViewById(R.id.rlWorkExperience);
        rlExtraCurricular = v.findViewById(R.id.rlExtraCurricular);
        rlIdentitiesDocuments = v.findViewById(R.id.rlIdentitiesDocuments);
        rlDisciplinaryActions = v.findViewById(R.id.rlDisciplinaryActions);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.rlEducationDetails:{

            }break;

            case R.id.rlWorkExperience:{

            }break;

            case R.id.rlExtraCurricular:{

            }break;

            case R.id.rlIdentitiesDocuments:{

            }break;

            case R.id.rlDisciplinaryActions:{

            }break;
        }
    }
}
