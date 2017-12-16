package com.example.usuario.pruebaretrofit.activities.Mapa;


import android.graphics.Point;
import android.view.SurfaceView;

/****************** Aquesta clase va a android **************/

public class Mapa extends LibreriaTxt{
    /*// Coses que s'han de canviar

    private Point posPlayer; // posicio del jugador (aixo sera un get inicial)
    private String[][] mapa;
    //private java.util.List<Objeto> objectesDinsMapa = new ArrayList<>();

    private java.util.List<IAs> listaIas = new ArrayList<>();

    /*private int altura = 50, ample =100;
    private String [][] malla = new String[altura][ample];
    private String [][] mallaAnt = new String[altura][ample];
    private int idMalla;

    private String[] edificios = new String[9];
    /* 1 2 3
       4 5 6
       7 8 9  */

/*
    public Mapa(String nomMapa){
        // primer carrego el mapa (llegeixo el fitxer txt)
        mapa = llegirMapaTxt(nomMapa);

        // carrego els objectes ie els poso al mapa
        afegirObjectes();
        //posarObjectesAlMapa();

        // carrego els ias
        afegirIas();
        posarIasAlMapa();

        generateFrame(mapa, ta1, mapa.length, mapa[0].length);
    }

    public void afegirIas(){
        IAs ia1 = new IAs("v",1,4,new Point(25,19),new Point(15,15));
        listaIas.add(ia1);
    }
/*
    public String[][] getMalla() {
        return malla;
    }
    public void setMalla(String[][] malla) {
        this.malla = malla;
    }
    public int getIdMalla() {
        return idMalla;
    }
    public void setIdMalla(Integer idMalla) {
        this.idMalla = idMalla;
    }


    public void afegirObjectes(){
        Objeto banco = new Objeto("banco", "pa' sentarse", 20, 2);
        Objeto container = new Objeto("Container", "pa' tirar basura", 40, 4);

        banco.setIdObjMapa("b"); container.setIdObjMapa("c"); // li dic quin caracter correspon a l'objecte al mapa

        banco.setTamanoObjCeldaMap(new Point(2,1)); //dimensio del objecte al mapa
        banco.setPosicionObjeto(new Point(31,10)); //posició de la cela superior dreta al mapa
        container.setTamanoObjCeldaMap(new Point(2,2));
        container.setPosicionObjeto(new Point(31,40));

        objectesDinsMapa.add(banco); objectesDinsMapa.add(container);

        Objeto banco2 = new Objeto("banco", "pa' sentarse", 20, 2);
        Objeto container2 = new Objeto("Container", "pa' tirar basura", 40, 4);
        banco2.setIdObjMapa("b"); container2.setIdObjMapa("c"); // li dic quin caracter correspon a l'objecte al mapa
        banco2.setPosicionObjeto(new Point(61,20));
        container2.setPosicionObjeto(new Point(61,40));
        banco2.setTamanoObjCeldaMap(new Point(2,1)); //dimensio del objecte al mapa
        container2.setTamanoObjCeldaMap(new Point(2,2));
        objectesDinsMapa.add(banco2); objectesDinsMapa.add(container2);
    }

    public static String[][] posarObjectesAlMapa(String[][] mapa, java.util.List<Objeto> objectesDinsMapa){
        for (Objeto o: objectesDinsMapa) {
            for (int amp=0;amp<o.getTamanoObjCeldaMap().getX();amp++) {
                mapa[(int) o.getPosicionObjeto().getY()][(int) o.getPosicionObjeto().getX() + amp] = o.getIdObjMapa();
                for (int alt = 0; alt < o.getTamanoObjCeldaMap().getY(); alt++) {
                    mapa[(int) o.getPosicionObjeto().getY() + alt][(int) o.getPosicionObjeto().getX()+amp] = o.getIdObjMapa();
                }
            }
        }
        return mapa;
    }

    public void posarIasAlMapa(){
        for (IAs a: listaIas) {
            mapa[(int) a.getPosicion().getY()][(int)a.getPosicion().getX()] = a.getIdIa();
            mapa[(int) a.getPosObjetivo().getY()][(int)a.getPosObjetivo().getX()] = "*";
        }
    }

    // a partir del n y nn et diu a quin edifici estas
    public String quinEdifici(int n, int nn)
    {
       /* String s= " ";
        switch (nn){
            case 1:
                switch (n){
                    case 1:
                        s=edificios[0];//"H";
                    break;
                    case 4:
                        s=edificios[1];//"O";
                    break;
                    case 7:
                        s=edificios[2];//"O";
                    break;
                }
                break;
            case 4:
                switch (n){
                    case 1:
                        s=edificios[3];//"O";
                    break;
                    case 4:
                        s= edificios[4];//"E";
                    break;
                    case 7:
                        s=edificios[5];//"P";
                    break;
                }
                break;
            case 7:
                switch (n){
                    case 1:
                        s=edificios[6];//"C";
                    break;
                    case 4:
                        s=edificios[7];//"O";
                    break;
                    case 7:
                        s=edificios[8];//"O";
                    break;
                }
                break;

        }
        return s;
       return null;
    }


    // A partir de la posicio et retorna el n y nn per saber en quin edifici estas
    public Point quinDecim(int posX, int posY){
        /*int m,mm;

        if(posX<(3*ample/10) && posX>=(ample/10))
            m=1;
        else if (posX<(6*ample/10) && posX>=(4*ample/10))
            m=4;
        else if(posX<(9*ample/10) && posX>=(7*ample/10))
            m=7;
        else
            m=0;

        if(posY<(3*altura/10) && posY>=(altura/10))
            mm=1;
        else if (posY<(6*altura/10) && posY>=(4*altura/10))
            mm=4;
        else if(posY<(9*altura/10) && posY>=(7*altura/10))
            mm=7;
        else
            mm=0;
        return new Point(m,mm);
        return null;
    }


    // dibuixo el mapa en el frame
    public static void dibuixarMapa(String [][] mapa, TextArea ta, int alt, int ampl) {
        ta.setText("");
        //mapa[posY][posX] ="@";
        for(int i = 0; i< alt; i++)
        {
            for(int j = 0; j< ampl; j++ )
            {
                ta.append(mapa[i][j]);
            }
            ta.append("\n");
        }
    }

    // faig una finestra per provar el mapa
    public Frame generateFrame (String [][] mapa, TextArea ta, int alt, int ampl)
    {
        JFrame f = new JFrame("Mapa");

        ta1.setFont(new Font("monospaced", Font.PLAIN, 12));
        //f.setSize(1000, 1000);
        final JPanel contentPane = new JPanel(new BorderLayout());

        dibuixarMapa(mapa, ta, alt, ampl);
        //f.add(ta1);
        JButton button = new JButton("Derecha");
        JButton button2 = new JButton("Izquierda");
        JButton button3 = new JButton("Abajo");
        JButton button4 = new JButton("Arriba");

        contentPane.setLayout(new FlowLayout());

        contentPane.add(ta1, BorderLayout.PAGE_START);
        contentPane.add(button, BorderLayout.NORTH);
        contentPane.add(button2, BorderLayout.SOUTH);
        contentPane.add(button3, BorderLayout.EAST);
        contentPane.add(button4, BorderLayout.WEST);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //malla[posY][posX] ="-";
                /*Point p=quinDecim(posX, posY);
                if(p.getX()==0 || p.getY()==0)
                    malla[posY][posX] ="-";
                else
                    malla[posY][posX] =quinEdifici(p.getX(),p.getY());
                posX += 1;dibuixarMapa();

                for (IAs a: listaIas) {
                    mapa[(int) a.getPosicion().getY()][(int)a.getPosicion().getX()] = "-";
                    a.mover();
                    mapa[(int) a.getPosicion().getY()][(int)a.getPosicion().getX()] = a.getIdIa();
                    //mapa[(int) a.getPosObjetivo().getY()][(int)a.getPosObjetivo().getX()] = "*";
                }
                dibuixarMapa(mapa,ta,alt,ampl);
            }
        });
       /* button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point p=quinDecim(posX, posY);
                if(p.getX()==0 || p.getY()==0)
                    malla[posY][posX] ="-";
                else
                    malla[posY][posX] =quinEdifici(p.getX(),p.getY());
                posX = posX - 1;dibuixarMapa();
                //malla[posY][posX] ="@";
            }
        });
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point p=quinDecim(posX, posY);
                if(p.getX()==0 || p.getY()==0)
                    malla[posY][posX] ="-";
                else
                    malla[posY][posX] =quinEdifici(p.getX(),p.getY());
                posY += 1;dibuixarMapa();
                //malla[posY][posX] ="@";
            }
        });
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point p=quinDecim(posX, posY);
                if(p.getX()==0 || p.getY()==0)
                    malla[posY][posX] ="-";
                else
                    malla[posY][posX] =quinEdifici(p.getX(),p.getY());
                posY = posY - 1;dibuixarMapa();
                //malla[posY][posX] ="@";
            }
        });
        //f.getContentPane().add(ta1);
        //f.getContentPane().add(button);
        f.setContentPane(contentPane);
        f.pack();
        f.setVisible(true);
        return f;
    }

    public static void main(String[] args)  {
        Mapa m = new Mapa("mapaEscola");

    }*/
}