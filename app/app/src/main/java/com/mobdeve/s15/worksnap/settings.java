package com.mobdeve.s15.worksnap;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView viewUserIdButton;
    private TextView changeUserDetailsButton;
    private TextView changePasswordButton;
    private TextView signOutButton;

    public settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static settings newInstance(String param1, String param2) {
        settings fragment = new settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        viewUserIdButton = view.findViewById(R.id.btn_view_user_id);
        viewUserIdButton.setOnClickListener(v -> {
            Fragment fragment = new ViewUserIdFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutt, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        changeUserDetailsButton = view.findViewById(R.id.btn_change_user_details);
        changeUserDetailsButton.setOnClickListener(v -> {
            Fragment fragment = new EditDetailsFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutt, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        changePasswordButton = view.findViewById(R.id.btn_change_password);
        changePasswordButton.setOnClickListener(v -> {
            Fragment fragment = new ChangePasswordFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutt, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        changePasswordButton = view.findViewById(R.id.btn_change_password);
        changePasswordButton.setOnClickListener(v -> {
            Fragment fragment = new ChangePasswordFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutt, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        signOutButton = view.findViewById(R.id.btn_signout);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "Signed out successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }

}