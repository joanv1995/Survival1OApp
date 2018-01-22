package com.example.usuario.pruebaretrofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.model.Ranking2;

import java.util.List;

public class ListaPartidasUser extends RecyclerView.Adapter<ListaPartidasUser.ListaUsuariosViewHolder> {

    private final int rowLayout;
    private List<Ranking2> partidas;
    private Context context;


    public ListaPartidasUser(List<Ranking2> partidas, int rowLayout, Context context) {
        this.partidas = partidas;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    //A view holder inner class where we get reference to the views in the layout using their ID

    public static class ListaUsuariosViewHolder extends RecyclerView.ViewHolder {
        LinearLayout partidas;
        TextView id;
        TextView seguidores;
        TextView votos;
        TextView punt;


        public ListaUsuariosViewHolder(View v) {
            super(v);
            partidas = (LinearLayout) v.findViewById(R.id.listaPartidas_layout);
            id = (TextView) v.findViewById(R.id.idmapa);
            seguidores = (TextView) v.findViewById(R.id.seguidores);
            votos = (TextView) v.findViewById(R.id.votos);
            punt = (TextView) v.findViewById(R.id.punt);

        }
    }


    @Override
    public ListaPartidasUser.ListaUsuariosViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ListaUsuariosViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ListaUsuariosViewHolder holder, final int position) {
        String level = null;
        if(partidas.get(position).getIdMapa().equals("mapa1")){level="Nivel 1";}
        if(partidas.get(position).getIdMapa().equals("mapa2")){level="Nivel 2";}
        if(partidas.get(position).getIdMapa().equals("mapa3")){level="Nivel 3";}
        if(partidas.get(position).getIdMapa().equals("mapa4")){level="Nivel 4";}
        if(partidas.get(position).getIdMapa().equals("mapa5")){level="Nivel 5";}

        holder.id.setText(level);
        holder.seguidores.setText(String.valueOf(partidas.get(position).getSeguidores()));
        holder.votos.setText(String.valueOf(partidas.get(position).getVotos()));
        holder.punt.setText(String.valueOf(partidas.get(position).getPuntuaciontot()));
    }

    @Override
    public int getItemCount() {
        return partidas.size();
    }
}
