package com.example.usuario.pruebaretrofit.activities.FragmentsPerfil;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.activities.PerfilActivity;
import com.example.usuario.pruebaretrofit.adapter.ListaUsuariosAdapter;
import com.example.usuario.pruebaretrofit.model.Usuario2;
import com.example.usuario.pruebaretrofit.service.RestClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = RankingFragment.class.getSimpleName();
    private RecyclerView recyclerView = null;
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://147.83.7.206:8088/1O-survival/game/";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Agafar el nom d'usuari
        PerfilActivity pf = (PerfilActivity)getActivity();
        Usuario2 player = pf.getInfoUser();

        String userName = player.getNombre();
        connectApiService(userName);

        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inflate the layout for this fragment
        return rootView;
    }

    public void connectApiService(String userName){

        final ProgressDialog progDialog = new ProgressDialog(getActivity());
        progDialog.setIndeterminate(true);              // Tipus de progressbar
        progDialog.setTitle("1O - Database");
        progDialog.setMessage("Realizando la búsqueda...");
        progDialog.show();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        RestClient restClient = retrofit.create(RestClient.class);


        Call<List<Usuario2>> call = restClient.getListaUsuarios(userName);
        call.enqueue(new Callback<List<Usuario2>>() {
            @Override
            public void onResponse(Call<List<Usuario2>> call, Response<List<Usuario2>> response) {
                List<Usuario2> listaUsuarios = (List<Usuario2>) response.body();

                recyclerView.setAdapter(new ListaUsuariosAdapter(listaUsuarios, R.layout.list_usuarios_item, getContext()));

                Log.d(TAG, "Number of movies received: " + listaUsuarios.size());
                stopProgDialog(progDialog);
            }

            @Override
            public void onFailure(Call<List<Usuario2>> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });

    }

    private void stopProgDialog(final ProgressDialog progDialog){
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                progDialog.dismiss();       // Tanca progressbar
                t.cancel();                 // Cancela el timer
            }
        }, 2000);                    // Delay de 2s per mostrar la informació
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
