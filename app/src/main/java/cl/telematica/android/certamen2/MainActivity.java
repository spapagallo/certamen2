package cl.telematica.android.certamen2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cl.telematica.android.certamen2.fragments.InputFragment;
import cl.telematica.android.certamen2.fragments.ListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Abrimos el primer fragment por default
        navigationView.getMenu().getItem(0).setChecked(true);
        switchContent(InputFragment.newInstance(), null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_input_joke) {
            // Aqui se abre el primer fragmento
            switchContent(InputFragment.newInstance(), null);
        } else if (id == R.id.nav_list_joke) {
            // Aqui se abre el fragmento de las listas
            switchContent(ListFragment.newInstance(), null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(Fragment fragment, String addBackStack) {
        switchContent(fragment, addBackStack, -1, -1);
    }

    /**
     * Switch given fragment on default content holder and add to back stack.
     * Use given an animationIn and animationOut for fragment transaction
     *
     * @param fragment     Fragment to be switched up
     * @param addBackStack Back stack tag
     * @param animationIn  In animation for transaction
     * @param animationOut Out animation for transaction
     */
    public void switchContent(Fragment fragment, String addBackStack, int animationIn, int animationOut) {
        try {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (animationIn > 0 && animationOut > 0)
                fragmentTransaction.setCustomAnimations(animationIn, animationOut);

            if (addBackStack != null)
                fragmentTransaction.addToBackStack(addBackStack);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
