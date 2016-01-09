package se.liu.student.frejo105.beerapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import se.liu.student.frejo105.beerapp.Fragments.PubDetailsFragment;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.R;

public class PubDisplayActivity extends DrawerMenuActivity {

    public static final String PUB_KEY = PubDetailsFragment.PUB_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_display);

        Bundle b = getIntent().getExtras();
        if (!b.containsKey(PUB_KEY)) throw new RuntimeException(getString(R.string.no_item_error));
        PubDetailsFragment pdf = new PubDetailsFragment();
        pdf.setArguments(b);
        FragmentTransaction t =  getSupportFragmentManager().beginTransaction();
        t.replace(R.id.pub_info_panel, pdf).commit();
    }

}
