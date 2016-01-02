package se.liu.student.frejo105.beerapp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

public abstract class DrawerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        final DrawerLayout container = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_drawer_menu, null);
        FrameLayout content = (FrameLayout)findViewById(R.id.contentframe);
        getLayoutInflater().inflate(layoutResID, content, true);
        super.setContentView(container);
        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this, container, toolbar, R.string.drawer_toggle_open, R.string.drawer_toggle_close);
        container.setDrawerListener(toggler);
    }
}
