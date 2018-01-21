package com.example.usuario.pruebaretrofit.activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.activities.FragmentsPerfil.PerfilFragment;
import com.example.usuario.pruebaretrofit.activities.FragmentsPerfil.PlayFragment;
import com.example.usuario.pruebaretrofit.activities.FragmentsPerfil.RankingFragment;
import com.example.usuario.pruebaretrofit.model.Usuario2;

public class PerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PerfilFragment.OnFragmentInteractionListener, RankingFragment.OnFragmentInteractionListener, PlayFragment.OnFragmentInteractionListener{
    private Usuario2 player;
    public void onFragmentInteraction(Uri uri){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //View view =  R.layout.activity_perfil;
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
            }

    @Override
    protected void onStart() {
        super.onStart();
        Intent in = getIntent();
        player = (Usuario2)in.getSerializableExtra("jugador");

        final NavigationView navigation = (NavigationView)findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(this);
        View headerView = navigation.inflateHeaderView(R.layout.nav_header_perfil);

        TextView nombreUsu = (TextView) headerView.findViewById(R.id.nombreUsuario);
        TextView email = (TextView) headerView.findViewById(R.id.mail);
        nombreUsu.setText(player.getNombre());
        email.setText(player.getCorreo());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Boolean IsFragment = false;
        Fragment frag = null;

        if (id == R.id.nav_perfil) {
            item.setChecked(true);
            setFragment(0);

            // Handle the camera action
        } else if (id == R.id.nav_play) {
            setFragment(1);
        } else if (id == R.id.nav_rank) {
            setFragment(2);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PerfilFragment perfilFragment = new PerfilFragment();
                fragmentTransaction.replace(R.id.basefrag, perfilFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PlayFragment playFragment = new PlayFragment();
                fragmentTransaction.replace(R.id.basefrag, playFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                RankingFragment rankFragment = new RankingFragment();
                fragmentTransaction.replace(R.id.basefrag, rankFragment);
                fragmentTransaction.commit();
                break;
        }
    }
    public Usuario2 getInfoUser(){return player;}
}
