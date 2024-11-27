package com.mobdeve.s15.worksnap;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private static final String TAG = "taggg";

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

    FirebaseFirestore db;
    CollectionReference usersRef ;

    public interface OnEmployeeIDsFetchedListener {
        void onSuccess(ArrayList<String> employeeeeeeeIDs);

        void onFailure(Exception e);
    }

    public void getEmployeeIDs(String userID, OnEmployeeIDsFetchedListener listener) {
        DocumentReference docRef = usersRef.document(userID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                            // Safely retrieve the list of employee IDs
                            List<String> employees = (List<String>) document.getData().get("employees");
                            if (employees != null) {
                                listener.onSuccess(new ArrayList<>(employees));
                            } else {
                                Log.d(TAG, "Employees list is null");
                                listener.onSuccess(new ArrayList<>()); // Return an empty list if field is null
                            }
                        } catch (ClassCastException e) {
                            Log.d(TAG, "Employees field is not of the expected type");
                            listener.onFailure(e);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                        listener.onFailure(new Exception("Document does not exist"));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    listener.onFailure(task.getException());
                }
            }
        });
    }

    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
            uid = getArguments().getString("uid");

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(MyFirestoreReferences.USERS_COLLECTION);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_employee, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.allEmployeeRecycler);
        recyclerView.setHasFixedSize(true);

        // Set LayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        // Initialize the list of Employee Data
        ArrayList<EmployeeData> EmployeeDataList = new ArrayList<>();

        getEmployeeIDs(uid, new OnEmployeeIDsFetchedListener() {
            @Override
            public void onSuccess(ArrayList<String> employeeIDs) {
                if (!employeeIDs.isEmpty()) {
                    // Query to fetch employee data based on IDs
                    Query employeeDataQuery = usersRef.whereIn(FieldPath.documentId(), employeeIDs);

                    employeeDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
//                                ArrayList<EmployeeData> EmployeeDataListss = new ArrayList<>();
//                                for (QueryDocumentSnapshot document : task.getResult()){
//                                    Log.e(TAG, "Edasdasdas21312dasd" );
//                                    Log.e(TAG, "Edasdasd" + document);}
////                                    EmployeeDataListss.add(document.toObject(EmployeeData.class));}
//
//                                Log.e(TAG, "Employee Name: " + EmployeeDataListss);
//                                EmployeeAdapter employeeAdapter = new EmployeeAdapter(EmployeeDataListss, (MainActivity) getActivity());
//                                recyclerView.setAdapter(employeeAdapter);
//
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        // Safely retrieve the "name" and "position" fields
                                        String name = document.getString("username");
                                        String pic = document.getString("profilePhoto");

                                        // Add employee data to the list
                                        EmployeeDataList.add(new EmployeeData(name, pic, document.getId()));
//                                        Log.e(TAG, "Employee Name: " + name + ", Position: " + document);
                                    }

                                    // Initialize the adapter after data is fetched
                                    EmployeeAdapter employeeAdapter = new EmployeeAdapter(EmployeeDataList, (MainActivity) getActivity(), getParentFragmentManager());
                                    recyclerView.setAdapter(employeeAdapter);
                                } else {
                                    Log.d(TAG, "No documents found");
                                }
                            } else {
                                Log.e(TAG, "Error fetching documents", task.getException());
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error fetching employee IDs", e);
            }
        });

        return view;
    }
}
