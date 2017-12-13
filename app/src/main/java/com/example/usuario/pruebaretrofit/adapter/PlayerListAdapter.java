package com.example.usuario.pruebaretrofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.model.Usuario;

import java.util.List;

/**
 * Created by usuario on 01/12/2017.
 */
/*
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayersViewHolder>{


    private List<Usuario> players;
    private int rowLayout;
    private Context context;

    public PlayerListAdapter(List<Usuario> pl, int rowLayout, Context context) {
        this.players = pl;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public PlayersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayersViewHolder holder, int position) {

        Log.i("players", players.toString());
        for (Usuario p: players) {
            Log.i("players", p.getNombre());
        }


        if (players.size()!=0) {
            holder.name.setText(players.get(position).getNombre());

            holder.vida.setText(String.valueOf(players.get(position).getVida()));
            holder.votos.setText(String.valueOf(players.get(position).getVotos()));
            holder.seguidores.setText(String.valueOf(players.get(position).getSeguidores()));
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    //A view holder inner class where we get reference to the views in the layout using their ID
    public static class PlayersViewHolder extends RecyclerView.ViewHolder {
        LinearLayout playerLayout;
        TextView name;
        TextView vida;
        TextView votos;
        TextView seguidores;


        public PlayersViewHolder (View v) {
            super(v);
            playerLayout = (LinearLayout) v.findViewById(R.id.players_layout);
            name = (TextView) v.findViewById(R.id.name);
            vida = (TextView) v.findViewById(R.id.vida);
            votos = (TextView) v.findViewById(R.id.votos);
            seguidores = (TextView) v.findViewById(R.id.seguidores);
        }
    }
}*/
