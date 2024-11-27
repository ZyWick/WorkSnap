package com.mobdeve.s15.worksnap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link check_employees#newInstance} factory method to
 * create an instance of this fragment.
 */
public class checkEmployees extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "taggg";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<DayAttendanceData> DayAttendanceList = new ArrayList<DayAttendanceData>();
    private DayAttendanceAdapter DayAttendanceAdapter;

    public checkEmployees() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment check_employees.
     */
    // TODO: Rename and change types and number of parameters
    public static checkEmployees newInstance(String param1, String param2) {
        checkEmployees fragment = new checkEmployees();
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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection(MyFirestoreReferences.USERS_COLLECTION);

    public interface OnEmployeeIDsFetchedListener {
        void onSuccess(ArrayList<String> employeeeeeeeIDs);

        void onFailure(Exception e);
    }

    public void getEmployeeIDs(String userID, AllEmployee.OnEmployeeIDsFetchedListener listener) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_employees, container, false);
        view.findViewById(R.id.selectDate).setOnClickListener(this::selectTheDate);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.dayAttendanceRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getEmployeeIDs("T0l1ar5QuLbT8c42wWwp3dTCiay2", new AllEmployee.OnEmployeeIDsFetchedListener() {
            @Override
            public void onSuccess(ArrayList<String> employeeIDs) {
                if (!employeeIDs.isEmpty()) {
                    // Query to fetch employee data based on IDs
                    Query uncheckedPhotosQuery = usersRef.whereIn(FieldPath.documentId(), employeeIDs);

                    uncheckedPhotosQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {

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


        DayAttendanceList = new ArrayList<DayAttendanceData>();
        for(int i = 0; i < 20; i++) {
            ArrayList<AttendancePhotoData> AttendancePhotoDataList = new ArrayList<AttendancePhotoData>();
            int randomNumPhotos = (int) (Math.random() * 12 + 1);
            for(int j = 0; j < randomNumPhotos; j++) {
                AttendancePhotoDataList.add(new AttendancePhotoData());
            }
            DayAttendanceList.add(new DayAttendanceData(Timestamp.now(), AttendancePhotoDataList));
        }

        DayAttendanceAdapter = new DayAttendanceAdapter(DayAttendanceList, (MainActivity) getActivity(),  getParentFragmentManager());
        recyclerView.setAdapter(DayAttendanceAdapter);

        return view;
    }

    public void selectTheDate (View v) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getParentFragmentManager(), "datePicker");
    }
}


