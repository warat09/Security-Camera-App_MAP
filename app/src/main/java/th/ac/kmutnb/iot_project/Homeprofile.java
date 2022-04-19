package th.ac.kmutnb.iot_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Homeprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homeprofile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private String Email;
    private String Username;
    private String Phone;
    public Homeprofile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homenotification.
     */
    // TODO: Rename and change types and number of parameters
    public static Homeprofile newInstance(String param1, String param2) {
        Homeprofile fragment = new Homeprofile();
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
        // Inflate the layout for this fragment
        this.Username  = getArguments().getString("User_ID");
        this.Email = getArguments().getString("User_Email");
        this.Phone = getArguments().getString("User_Phone");
        view = inflater.inflate(R.layout.fragment_homeprofile, container, false);
        TextView txUsername = view.findViewById(R.id.Profile_Username);
        txUsername.setText("Username : "+this.Username);
        TextView txEmail = view.findViewById(R.id.Profile_Email);
        txEmail.setText("Email : "+this.Email);
        TextView txPhone = view.findViewById(R.id.Profile_Phone);
        txPhone.setText("Phone : "+this.Phone);
        return view;
    }
}