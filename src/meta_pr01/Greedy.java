/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.HashSet;
//import java.util.Random;


/**
 *
 * @author spdlc
 */
public class Greedy extends Algoritmo{

    /*---------------- MÉTODOS PÚBLICOS ---------------*/
    
    /**
     * @Constructor del GREEDY.
     * @post Creamos el greedy a partir de la superclase algoritmo que nos proporciona todos los datos
     * de los archivos necesarios: matriz de datos, hashSet de solución candidata, numero de candidatos...
     * @param args Parametro necesario para la posible lectura en la clase abstracta de los datos.
     */
    public Greedy(String[] args, Integer num_archivo){
        super(args,num_archivo);
        //Generamos el primer punto aleatorio a partir de la semilla y lo añadimos al hashSet solución.
        Random random = new Random();
        random.Set_random(getConfig().getSemillas().get(0));
        
        int puntoInicio = random.Randint(0,getNum_elementos()-1);      
        M.add(puntoInicio);
        n.remove(puntoInicio);
    }
    
    /**
     * @brief Algoritmo Greedy para MDP.
     * @post A partir de un elemento random, se busca el siguiente elemento cuya
     * suma de sus distancias con respecto a los puntos seleccionados sea mayor que 
     * los demás elementos aún no seleccionados con los seleccionados.
     * @return HashSet correspondiente a la solución candidata final.
     */
    public HashSet<Integer> algoritmoGreedy(){
        
        //Primer bucle necesario para acabar cuando se haya llenado el vector Solución.
        while (M.size() < getNum_candidatos()){
            double distMax = 0;
            int elem_seleccionado = -1;
            for(Integer j : getN()){
                double aux = distanciasElemento(j);
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

}
