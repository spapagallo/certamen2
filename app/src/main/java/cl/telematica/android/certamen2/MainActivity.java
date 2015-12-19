package cl.telematica.android.certamen2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.telematica.android.certamen2.connection.HttpServerConnection;
import cl.telematica.android.certamen2.fragments.InputFragment;
import cl.telematica.android.certamen2.fragments.ListFragment;
import cl.telematica.android.certamen2.modelo.ObjetoValue;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText name, lastName;
    String nombre, apellido, url;

    TextView urlView;
    TextView textFromWeb;

    RecyclerView.ViewHolder holder;

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
            switchContent(ListFragment.newInstance(getApplicationContext()), null);
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

    public void setURL(View view) {

        name=(EditText)findViewById(R.id.editTextName);
        lastName=(EditText)findViewById(R.id.editTextLastName);

        nombre=name.getText().toString();
        apellido=lastName.getText().toString();

        url = "http://api.icndb.com/jokes/random?fistName="+nombre+"&lastName="+apellido;

        urlView=(TextView)findViewById(R.id.textURL);
        urlView.setText(url);


        AsyncTask< Void, Void, String > task = new AsyncTask<Void, Void, String>(){

            @Override
            protected void onPreExecute(){
            }
            @Override
            protected String doInBackground(Void... params) {
                String resultado = new HttpServerConnection()
                        .connectToServer(url,1500); // parametros (url, timeout)
                return resultado;
            }
            String tipo;
            int id;
            String joke;
            List<String> categories;

            @Override
            protected void onPostExecute(String result){
                if (result != null)
                {
                    System.out.println(result);

                    // specify an adapter (see also next example)
                    JSONObject objeto = null;
                    try {
                        objeto = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject objetoValue = null;

                        try {
                            assert objeto != null;
                            tipo = objeto.getString("type");
                            objetoValue = objeto.getJSONObject("value");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            assert objetoValue != null;
                            id=objetoValue.getInt("id");
                            joke=objetoValue.getString("joke");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    textFromWeb=(TextView)findViewById(R.id.textFromWeb);
                    textFromWeb.setText(joke);
                }
            }
        };

        task.execute();
    }
}
