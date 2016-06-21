package se.liu.student.frejo105.beerapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.liu.student.frejo105.beerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PubListFragment extends Fragment {


    public PubListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pub_list, container, false);
    }

}
