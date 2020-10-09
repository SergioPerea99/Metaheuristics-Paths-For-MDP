/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.HashSet;

/**
 *
 * @author spdlc
 */
public abstract class Algoritmo {
    private Configurador config;
    private ArchivoDatos archivo;
    protected int num_elementos;
    protected int num_candidatos;
    protected HashSet<Integer> M; //Vector solución candidata.
    protected HashSet<Integer> n; //HashSet de no candidatos.
    
    public Algoritmo(String[] _args,Integer num_archivo){
        config = new Configurador(_args[0]);
        archivo = new ArchivoDatos(getConfig().getArchivos().get(num_archivo)); 
        num_elementos = archivo.getTamMatriz();
        num_candidatos = archivo.getTamSolucion();
        M = new HashSet<>(num_candidatos);
        n = new HashSet<>(num_elementos);
        for (int i=0; i < num_elementos; i++)
            n.add(i);
    }
    
    /**
     * @brief Sumatoria del coste final.
     * @post La suma de todas las distancias de cada uno de los puntos con respecto a los demás puntos.
     * @return Sumatoria final.
     */
    public float costeSolucion(){
        float coste = 0;
        for(Integer i : M)
            for(Integer j : M)
                coste += archivo.getMatrizDatos()[i][j];
        return coste;
    }

    
    /*------ GETTERS ------*/

    /**
     * @return the config
     */
    public Configurador getConfig() {
        return config;
    }

    /**
     * @return the archivo
     */
    public ArchivoDatos getArchivo() {
        return archivo;
    }

    /**
     * @return the num_elementos
     */
    public int getNum_elementos() {
        return num_elementos;
    }

    /**
     * @return the num_candidatos
     */
    public int getNum_candidatos() {
        return num_candidatos;
    }

    /**
     * @return the M
     */
    public HashSet<Integer> getM() {
        return M;
    }

    /**
     * @return the n
     */
    public HashSet<Integer> getN() {
        return n;
    }

}
