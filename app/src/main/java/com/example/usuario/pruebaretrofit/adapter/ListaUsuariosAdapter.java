package com.example.usuario.pruebaretrofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.model.Usuario2;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.ListaUsuariosViewHolder> {

    private final int rowLayout;
    private List<Usuario2> listaUsuarios;
    private int puntuacion;
    private Context context;


    public ListaUsuariosAdapter(List<Usuario2> movies, int rowLayout, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    //A view holder inner class where we get reference to the views in the layout using their ID

    public static class ListaUsuariosViewHolder extends RecyclerView.ViewHolder {
        LinearLayout listaUsuariosLayout;
        TextView nombreUser;
        TextView puntuacion;


        public ListaUsuariosViewHolder(View v) {
            super(v);
            listaUsuariosLayout = (LinearLayout) v.findViewById(R.id.listaUsuarios_layout);
            nombreUser = (TextView) v.findViewById(R.id.nombreUser);
            puntuacion = (TextView) v.findViewById(R.id.puntuacion);

        }
    }


    @Override
    public ListaUsuariosAdapter.ListaUsuariosViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ListaUsuariosViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ListaUsuariosViewHolder holder, final int position) {
        holder.nombreUser.setText(listaUsuarios.get(position).getNombre());
        holder.puntuacion.setText(listaUsuarios.get(position).getPuntFinal());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }
}
