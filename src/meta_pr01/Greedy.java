/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.HashSet;
import java.util.Random;


/**
 *
 * @author spdlc
 */
public class Greedy extends Algoritmo{

    //private HashSet<Integer> M; //HashSet de candidatos.
    //private HashSet<Integer> n; //HashSet de no candidatos.
  
    /*---------------- MÉTODOS PÚBLICOS ---------------*/
    
    /**
     * @Constructor del GREEDY.
     * @post Creamos el greedy a partir de la superclase algoritmo que nos proporciona todos los datos
     * de los archivos necesarios: matriz de datos, hashSet de solución candidata, numero de candidatos...
     * @param args Parametro necesario para la posible lectura en la clase abstracta de los datos.
     */
    public Greedy(String[] args){
        super(args);
        /*M = new HashSet<>(getNum_candidatos());
        n = new HashSet<>(getNum_elementos());
        
        for (int i=0; i < getNum_elementos(); i++)
            n.add(i);*/

    }
    
    /**
     * @brief Algoritmo Greedy para MDP.
     * @post A partir de un elemento random, se busca el siguiente elemento cuya
     * suma de sus distancias con respecto a los puntos seleccionados sea mayor que 
     * los demás elementos aún no seleccionados con los seleccionados.
     * @return HashSet correspondiente a la solución candidata final.
     */
    public HashSet<Integer> algoritmoGreedy(){
        
        //Generamos el primer punto aleatorio a partir de la semilla y lo añadimos al hashSet solución.
        Random  random = new Random(getConfig().getSemillas().get(0));
        int puntoInicio = random.nextInt(getNum_elementos());
        
        M.add(puntoInicio);
        n.remove(puntoInicio);
        
        //Primer bucle necesario para acabar cuando se haya llenado el vector Solución.
        while (M.size() < getNum_candidatos()){
            float distMax = 0;
            int elem_seleccionado = -1;
            for(Integer j : getN()){
                float aux = distanciasElemento(j);
                if(distMax < aux){
                    distMax = aux;
                    elem_seleccionado = j;
                }
            }
            //Añadimos el elemento que se necesita en la solucion candidata y
            //lo quitamos de los no candidatos.
            M.add(elem_seleccionado);
            n.remove(elem_seleccionado);

        }
        
        return M;
    }

    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    /**
     * @brief Distancias de un elemento respecto a los candidatos.
     * @param elem Entero correspondiente que se quiere comprobar su distancia con los demás candidatos.
     * @return Suma de las distancias del elemento del parámetro con todos los candidatos.
     */
    private float distanciasElemento(Integer elem){
        float sumaDistancias = 0;
        for(Integer i : getM())
            //OJO: He puesto esta comprobacion para asegurar que sumaba de verdad las distancias y
            //no cogia la parte de la matriz que estaba sin rellenar.
            if(getArchivo().getMatrizDatos()[i][elem] != 0)
                sumaDistancias += getArchivo().getMatrizDatos()[i][elem];
            else
                sumaDistancias += getArchivo().getMatrizDatos()[elem][i];
        return sumaDistancias;
    }
    
}
