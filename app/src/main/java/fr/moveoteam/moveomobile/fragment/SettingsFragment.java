package fr.moveoteam.moveomobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import fr.moveoteam.moveomobile.R;

/**
 * Created by Sylvain on 31/05/15.
 */
public class SettingsFragment extends Fragment {

    EditText password;
    EditText newPassword;
    EditText checkNewPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings,container,false);
        return view;
    }
}
