package br.ufrj.caronae.frags;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import br.ufrj.caronae.App;
import br.ufrj.caronae.R;
import br.ufrj.caronae.User;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFrag extends Fragment {

    @Bind(R.id.name_et)
    EditText name_et;
    @Bind(R.id.profile_et)
    EditText profile_et;
    @Bind(R.id.course_et)
    EditText course_et;
    @Bind(R.id.unit_et)
    EditText unit_et;
    @Bind(R.id.zone_et)
    EditText zone_et;
    @Bind(R.id.neighborhood_et)
    EditText neighborhood_et;
    @Bind(R.id.phoneNumber_et)
    EditText phoneNumber_et;
    @Bind(R.id.email_et)
    EditText email_et;
    @Bind(R.id.carOwner_cb)
    CheckBox carOwner_cb;
    @Bind(R.id.carModel_et)
    EditText carModel_et;
    @Bind(R.id.carColor_et)
    EditText carColor_et;
    @Bind(R.id.carPlate_et)
    EditText carPlate_et;

    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        User user = App.getUser();
        if (user != null) {
            name_et.setText(user.getName());
            profile_et.setText(user.getProfile());
            course_et.setText(user.getCourse());
            unit_et.setText(user.getUnit());
            zone_et.setText(user.getZone());
            neighborhood_et.setText(user.getNeighborhood());
            phoneNumber_et.setText(user.getPhoneNumber());
            email_et.setText(user.getEmail());
            carOwner_cb.setChecked(user.isCarOwner());
            carModel_et.setText(user.getCarModel());
            carColor_et.setText(user.getCarColor());
            carPlate_et.setText(user.getCarPlate());
        }

        return view;
    }

    @OnClick(R.id.save_bt)
    public void saveBt() {
        User editedUser = new User();
        editedUser.setName(name_et.getText().toString());
        editedUser.setProfile(profile_et.getText().toString());
        editedUser.setCourse(course_et.getText().toString());
        editedUser.setUnit(unit_et.getText().toString());
        editedUser.setZone(zone_et.getText().toString());
        editedUser.setNeighborhood(neighborhood_et.getText().toString());
        editedUser.setPhoneNumber(phoneNumber_et.getText().toString());
        editedUser.setEmail(email_et.getText().toString());
        editedUser.setCarOwner(carOwner_cb.isChecked());
        editedUser.setCarModel(carModel_et.getText().toString());
        editedUser.setCarColor(carColor_et.getText().toString());
        editedUser.setCarPlate(carPlate_et.getText().toString());

        User user = App.getUser();
        if (!user.equals(editedUser)) {
            new saveUserAsync().execute(editedUser);
        }
    }

    private class saveUserAsync extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... paramUser) {
            User user = App.getUser();
            user.setUser(paramUser[0]);
            user.save();

            return null;
        }
    }
}
