/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.ArrayList;
import java.util.HashSet;
import javafx.util.Pair;

/**
 *
 * @author spdlc
 */
public abstract class Algoritmo {
    
    /*ATRIBUTOS PARA LA CARGA DE FICHEROS*/
    private Configurador config;
    private ArchivoDatos archivo;
    
    /*ATRIBUTOS GENERALES PARA LOS ALGORITMOS*/
    protected double costeTotal;
    protected int num_elementos;
    protected int num_candidatos;
    protected Random random;
    protected HashSet<Integer> M; //Vector solución candidata.
    protected HashSet<Integer> n; //HashSet de no candidatos.
    
    public Algoritmo(String[] _args,Integer num_archivo){
        config = new Configurador(_args[0]);
        archivo = new ArchivoDatos(getConfig().getArchivos().get(num_archivo));
        num_elementos = archivo.getTamMatriz();
        num_candidatos = archivo.getTamSolucion();
        random = new Random();
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
    public double costeSolucion(ArrayList<Integer> v_M){
        double coste = 0.0;
        //ArrayList<Integer> v_M = new ArrayList<>(M);
        for(int i = 0; i < v_M.size()- 1; i++)
            for(int j = i+1; j < v_M.size(); j++){
                if(archivo.getMatrizDatos()[v_M.get(i)][v_M.get(j)] != 0)
                    coste += archivo.getMatrizDatos()[v_M.get(i)][v_M.get(j)];
                else
                    coste += archivo.getMatrizDatos()[v_M.get(j)][v_M.get(i)];
            }
        return coste;
    }
    
    /**
     * @brief Distancias de un elemento respecto a los candidatos.
     * @post Método general para cualquier algoritmo que usemos, ya que se deberá comprobar el coste
     * de un punto respecto a todos los demás de la solución.
     * @param elem Entero correspondiente que se quiere comprobar su distancia con los demás candidatos.
     * @return Suma de las distancias del elemento del parámetro con todos los candidatos.
     */
    protected double distanciasElemento(Integer elem){
        double sumaDistancias = 0;
        for(Integer i : M)
            if(getArchivo().getMatrizDatos()[i][elem] != 0)
                sumaDistancias += archivo.getMatrizDatos()[i][elem];
            else
                sumaDistancias += archivo.getMatrizDatos()[elem][i];
        return sumaDistancias;
    }
    
    /**
     * @brief Función de factorización.
     * @param seleccionado
     * @param j
     * @return 
     */
    protected double factorizacion(int seleccionado,int j,HashSet<Integer> solucion_actual, double coste_actual){
        ArrayList<Integer> v_M = new ArrayList<>(solucion_actual);
        double costeMenor = 0, costeMayor =0;
        for (int k=0; k < v_M.size(); k++){
            if (v_M.get(k)!= seleccionado){
                if (getArchivo().getMatrizDatos()[seleccionado][v_M.get(k)] != 0)
                    costeMenor += getArchivo().getMatrizDatos()[seleccionado][v_M.get(k)];
                else
                    costeMenor += getArchivo().getMatrizDatos()[v_M.get(k)][seleccionado];
            }
            if (v_M.get(k)!= seleccionado){
                if(getArchivo().getMatrizDatos()[j][v_M.get(k)] != 0)
                    costeMayor+= getArchivo().getMatrizDatos()[j][v_M.get(k)];
                else
                    costeMayor+= getArchivo().getMatrizDatos()[v_M.get(k)][j];
            }
        }

        return coste_actual + costeMayor-costeMenor;
        
        
    }
    
    protected void ordenacionMenorAporte(ArrayList<Pair<Integer,Double>> v_distancias, HashSet<Integer> solucion){
        v_distancias.clear();
        ArrayList<Integer> v_solucion = new ArrayList<>(solucion);
        Pair<Integer,Double> añadir;
        for (int i = 0; i < v_solucion.size(); i++){
            añadir = new Pair<>(v_solucion.get(i),distanciasElemento(v_solucion.get(i)));
            v_distancias.add(añadir);
        }
       v_distancias.sort((o1,o2) -> o1.getValue().compareTo(o2.getValue()));
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

    /**
     * @return the coste
     */
    public double getCoste() {
        return costeTotal;
    }
    
    
    
    
}
