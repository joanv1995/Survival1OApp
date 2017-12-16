package com.example.usuario.pruebaretrofit.model;


import android.graphics.PointF;



public class IAs {

    //final static Logger log = Logger.getLogger(OneOctoberManagerImpl.class.getName());
    //private int id;
    //private String tipo;

    private String idIa;
    private int velocidad;
    private int numeroMaxEnMapa;
    private PointF posicion;
    private PointF posObjetivo;

    public IAs(String idIa, int velocidad, int numeroMaxEnMapa, PointF posicion, PointF posObjetivo) {
        this.idIa = idIa;
        this.velocidad = velocidad;
        this.numeroMaxEnMapa = numeroMaxEnMapa;
        this.posicion = posicion;
        this.posObjetivo = posObjetivo;
    }

    public String getIdIa() {
        return idIa;
    }
    public void setIdIa(String idIa) {
        this.idIa = idIa;
    }
    public int getVelocidad() {
        return velocidad;
    }
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    public int getNumeroMaxEnMapa() {
        return numeroMaxEnMapa;
    }
    public void setNumeroMaxEnMapa(int numeroMaxEnMapa) {
        this.numeroMaxEnMapa = numeroMaxEnMapa;
    }
    public PointF getPosicion() {
        return posicion;
    }
    public void setPosicion(PointF posicion) {
        this.posicion = posicion;
    }
    public PointF getPosObjetivo() {
        return posObjetivo;
    }
    public void setPosObjetivo(PointF posObjetivo) {
        this.posObjetivo = posObjetivo;
    }

    public void mover() {
        double distancia = getPosicion().length(getPosObjetivo().x,getPosObjetivo().y);
        double min = 1000;
        PointF act = new PointF();
        int[] vecPos = {1, -1, 0, 0};
        // de les quatre celes del voltant, miro quina es la que esta mes aprop del objectiu
        for (int i = 0; i < 4; i++) {
            PointF p = new PointF((int) getPosicion().x + vecPos[i], (int) getPosicion().y + vecPos[vecPos.length-1-i]);
            if(getPosObjetivo().length(p.x,p.y) < min ) {
                min = getPosObjetivo().length(p.x,p.y);
                act.set(p);
            }
        }
        // si m'ho ha calculat bé, actualitzo posicio
        if(!act.equals(new PointF()))
            setPosicion(act);
        else {
            //Log.error("No calcula laproxima cela");
        }
/*
        // de les quatre celes del voltant, miro quina es la que esta mes aprop del objectiu
        double min = 0;
        Point act = new Point();
        if(getPosObjetivo().distance(new Point((int) getPosicion().getX() + 1, (int) getPosicion().getY())) < min) {
            Point p = new Point((int) getPosicion().getX() + 1, (int) getPosicion().getY());
            min = getPosObjetivo().distance(p);
            act.setLocation(p);
        }
        if (getPosObjetivo().distance(new Point((int) getPosicion().getX() - 1, (int) getPosicion().getY())) < min) {
            Point p = new Point((int) getPosicion().getX() - 1, (int) getPosicion().getY());
            min = getPosObjetivo().distance(new Point((int) getPosicion().getX() - 1, (int) getPosicion().getY()));
            act.setLocation(p);
        }
        if (getPosObjetivo().distance(new Point((int) getPosicion().getX(), (int) getPosicion().getY() + 1)) < min){
            Point p = new Point((int) getPosicion().getX(), (int) getPosicion().getY() + 1);
            min = getPosObjetivo().distance(p);
            act.setLocation(p);
        }
        if(getPosObjetivo().distance(new Point((int)getPosicion().getX(),(int)getPosicion().getY()-1)) < min) {
            Point p = new Point((int) getPosicion().getX(), (int) getPosicion().getY() - 1);
            min = getPosObjetivo().distance(p);
            act.setLocation(p);
        }*/


    }





}