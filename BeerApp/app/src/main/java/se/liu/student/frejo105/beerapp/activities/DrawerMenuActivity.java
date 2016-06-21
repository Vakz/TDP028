package se.liu.student.frejo105.beerapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.model.Point;
import se.liu.student.frejo105.beerapp.utility.LocationCallback;

/**
 * Created by vakz on 2016-06-20.
 */
public abstract class DrawerMenuActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient locationApi;
    private List<LocationCallback> locationListener = new ArrayList<>();
    private static final int LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up location services here, so it can be used in any other activity that needs it
        if (locationApi == null) {
            locationApi = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView sw = (SearchView)menu.findItem(R.id.search_view).getActionView();
        sw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public void doSearch(String query) {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        Bundle b = new Bundle();
        b.putString(SearchActivity.QUERY_KEY, query);
        searchIntent.putExtra(SearchActivity.QUERY_KEY, query);
        startActivity(searchIntent);
        //finish();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        final DrawerLayout container = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_drawer_menu, null);
        FrameLayout content = (FrameLayout)container.findViewById(R.id.contentframe);
        getLayoutInflater().inflate(layoutResID, content, true);
        super.setContentView(container);
        setupToolbar(container);
    }

    private void setupToolbar(DrawerLayout container) {
        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this, container, toolbar, R.string.drawer_toggle_open, R.string.drawer_toggle_close);
        container.setDrawerListener(toggler);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);
        }
        else {
            doActualLocationRead();
        }
    }

    private void doActualLocationRead() {
        Point location;
        location = new Point(LocationServices.FusedLocationApi.getLastLocation(locationApi));
        notifyLocationListeners(location);
        locationApi.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case LOCATION_PERMISSION:
                    for (int permission : grantResults) {
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            doActualLocationRead();
                            break;
                        }
                    }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        notifyLocationListeners(null);
        locationApi.disconnect();
    }

    private void notifyLocationListeners(Point p) {
        for (LocationCallback lc : locationListener) {
            lc.onDone(p);
        }
        locationListener.clear();
        locationApi.disconnect();
    }


    protected void getLocation(LocationCallback lc) {
        locationListener.add(lc);
        if(locationListener.size() == 1) locationApi.connect();
    }

}
