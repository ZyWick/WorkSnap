package com.mobdeve.s15.worksnap;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllEmployee#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AllEmployee extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<EmployeeData> EmployeeDataList;
    private EmployeeAdapter EmployeeAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllEmployee.
     */
    // TODO: Rename and change types and number of parameters
    public static AllEmployee newInstance(String param1, String param2) {
        AllEmployee fragment = new AllEmployee();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AllEmployee() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_employee, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.allEmployeeRecycler);
        recyclerView.setHasFixedSize(true);

        // Set LayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        // Initialize and populate EmployeeDataList
        EmployeeDataList = new ArrayList<>();
        for (int i = 0; i < 46; i++) {
            EmployeeDataList.add(new EmployeeData("employee " + i));
        }

        // Initialize Adapter and set it to RecyclerView
        EmployeeAdapter = new EmployeeAdapter(EmployeeDataList, (MainActivity) getActivity());
        recyclerView.setAdapter(EmployeeAdapter);

        return view;
    }
}