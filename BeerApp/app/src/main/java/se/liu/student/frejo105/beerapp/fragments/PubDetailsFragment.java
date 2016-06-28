package se.liu.student.frejo105.beerapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.model.Pub;

/**
 * A simple {@link Fragment} subclass.
 */
public class PubDetailsFragment extends Fragment {

    public static final String PUB_PARAM = "PUB_PARAM";
    Pub pub;

    public PubDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_pub_details, container, false);
        pub = getArguments().getParcelable(PUB_PARAM);
        setupPubInfo(v);

        return v;
    }

    private void setupPubInfo (final View v) {
        ((TextView)v.findViewById(R.id.pub_details_name)).setText(pub.name);
        ((TextView)v.findViewById(R.id.pub_details_description)).setText(pub.description);
    }
}
