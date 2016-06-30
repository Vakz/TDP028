package se.liu.student.frejo105.beerapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.adapters.PubListAdapter;
import se.liu.student.frejo105.beerapp.api.model.Pub;

/**
 * A simple {@link Fragment} subclass.
 */
public class PubListFragment extends Fragment {

    ItemSelectedInterface mListener;
    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListener.onClick((Pub)parent.getAdapter().getItem(position));
        }
    };
    PubListAdapter items;


    public PubListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pub_list, container, false);
    }

    public void setPubList(List<Pub> list) {
        items.setPubList(list);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        items = new PubListAdapter(getContext(), new ArrayList<Pub>());
        ListView lw = ((ListView)view.findViewById(R.id.pub_list));
        lw.setAdapter(items);
        lw.setOnItemClickListener(itemSelect);
        super.onViewCreated(view, savedInstanceState);
    }

    public interface ItemSelectedInterface {
        void onClick(Pub pub);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mListener = (ItemSelectedInterface) context;
        }
    }
}