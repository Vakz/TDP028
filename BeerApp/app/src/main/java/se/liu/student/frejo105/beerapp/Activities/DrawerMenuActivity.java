package se.liu.student.frejo105.beerapp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import se.liu.student.frejo105.beerapp.R;

public abstract class DrawerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected boolean inLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
