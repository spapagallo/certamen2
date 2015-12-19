package cl.telematica.android.certamen2.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.telematica.android.certamen2.R;
import cl.telematica.android.certamen2.UIAdapter;
import cl.telematica.android.certamen2.connection.HttpServerConnection;
import cl.telematica.android.certamen2.modelo.ObjetoValue;

public class ListFragment extends Fragment {

    public ListFragment(Context context) {
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    /**
     * New instance of ListFragment
     *
     * @return new instance of ListFragment
     */
    public static ListFragment newInstance(Context context) {
        ListFragment fragment = new ListFragment(context);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mainView = inflater.inflate(R.layout.fragment_list, null);

        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);


        AsyncTask< Void, Void, String > task = new AsyncTask<Void, Void, String>(){

            @Override
            protected void onPreExecute(){
            }
            @Override
            protected String doInBackground(Void... params) {
                String resultado = new HttpServerConnection().connectToServer(
                        "http://api.icndb.com/jokes/random/2",
                        1500); // parametros (url, timeout)
                return resultado;
            }
            @Override
            protected void onPostExecute(String result){
                if (result != null)
                {
                    System.out.println(result);

                    // specify an adapter (see also next example)
                    mAdapter = new UIAdapter(getLista(result));
                    mRecyclerView.setAdapter(mAdapter);

                }
            }
        };

        task.execute();

        return mainView;
    }

    private List<ObjetoValue> getLista(String result)
    {
        String tipo;

        List<ObjetoValue> listaObjetos = new ArrayList<ObjetoValue>();
        JSONObject objetoGlobal = null;
        try {
            objetoGlobal = new JSONObject(result);
            tipo = objetoGlobal.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            JSONArray lista = null;
            assert objetoGlobal != null;
            lista = objetoGlobal.getJSONArray("value");
            int size = lista.length();

            for(int i=0; i<size; i++)
            {
                ObjetoValue objetoValue = new ObjetoValue();
                JSONObject objeto = lista.getJSONObject(i); // obtengo el objeto i de la lista que entrega

                objetoValue.setId(objeto.getInt("id"));
                objetoValue.setJoke(objeto.getString("joke"));
                objetoValue.setCategories((List<String>) objeto.getJSONArray("categories"));

                listaObjetos.add(objetoValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            return listaObjetos;
        }

        return listaObjetos;
    }
}
