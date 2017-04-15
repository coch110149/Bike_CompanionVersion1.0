package cochrane.bike_companion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import cochrane.bike_companion.Utilities.Utils;

import static cochrane.bike_companion.R.id.fab;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                                                 R.string.navigation_drawer_open,
                                                                 R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(getIntent().hasExtra(Utils.RIDES))
        {
            MenuItem item = navigationView.getMenu().getItem(1);
            onNavigationItemSelected(item);
        }else if(savedInstanceState == null)
        {
            MenuItem item = navigationView.getMenu().getItem(0);
            onNavigationItemSelected(item);
        }
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else
        {
            super.onBackPressed();
        }
    }


    @SuppressWarnings ( "StatementWithEmptyBody" )
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment = null;
        FragmentManager manager = getSupportFragmentManager();
        mFab.show();
        switch(item.getItemId())
        {
        case R.id.nav_home:
            fragment = HomeFragment.newInstance();
            mFab.hide();
            break;
        case R.id.nav_history:
            fragment = ManagementFragment.newInstance(Utils.RIDES);
            mFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    Toast.makeText(getApplicationContext(), "history", Toast.LENGTH_SHORT).show();
                    // TODO: 14/04/2017 implement adding a new activity
                }
            });
            break;
        case R.id.nav_bikes:
            fragment = ManagementFragment.newInstance(Utils.BIKES);
            mFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    startActivity(new Intent(MainActivity.this, EditBikeActivity.class));
                }
            });
            break;
        case R.id.nav_components:
            fragment = ManagementFragment.newInstance(Utils.COMPONENTS);
            mFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    Toast.makeText(getApplicationContext(), "compon", Toast.LENGTH_SHORT).show();
                }
            });
            break;
        case R.id.nav_groups:
            fragment = ManagementFragment.newInstance(Utils.GROUPS);
            mFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    startActivity(new Intent(MainActivity.this, EditGroupActivity.class));
                }
            });
            break;
        }
        if(fragment != null)
        {
            manager.beginTransaction().replace(
                    R.id.frag_view, fragment, fragment.getTag()).commit();
        }
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(item.getTitle());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
