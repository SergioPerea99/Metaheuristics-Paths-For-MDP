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
public class BusquedaLocal extends Algoritmo{
    /*---------------- MÉTODOS PÚBLICOS ---------------*/
    
    /**
     * @brief Constructor parametrizado.
     * @param args
     * @param num_archivo
     * @param sem 
     */
    public BusquedaLocal(String[] args, Integer num_archivo,int sem){
        
        super(args,num_archivo);
        
        //Generamos la primera solución candidata de partida a partir de un aleatorio.
        int i = 0, punto;
        random.Set_random(getConfig().getSemillas().get(sem));
        
        /*INSTANCIO LA PRIMERA SOLUCIÓN VÁLIDA.*/
        while (M.size() < getNum_candidatos()){
            punto = random.Randint(0, num_elementos-1);
            M.add(punto);
            n.remove(punto);
            ++i;
        }

    }
    
    public void algBusquedaLocal(){

        
        /*Inicialización de estructuras y variables necesarias.*/
        ArrayList<Integer> v_M = new ArrayList<>(M);
        ArrayList<Integer> v_n = new ArrayList<>(n);
        ArrayList<Pair<Integer,Double>> v_distancias = new ArrayList<>();     
        
        /*GENERO EL VALOR DE LA SOLUCION INICIAL VÁLIDA*/
        costeTotal = costeSolucion(v_M);
        
        int seleccionado;
        int it = 0;
        boolean fin = false;
        double aux;
        
        
        while(it < getConfig().getMax_Iteraciones() && !fin){
            
            ordenacionMenorAporte(v_distancias, M);
            fin = true;
            /*Por si debe de comprobar con los 50 puntos seleccionados escogiendo del menor al mayor en coste.*/
            for(int i = 0; i < M.size() && it < getConfig().getMax_Iteraciones() && fin; i++){
                
                seleccionado = v_distancias.get(i).getKey();
                
                //2 bucle: Busqueda de un punto no seleccionado que supere al punto de menor coste encontrado en este momento.             
                for(int j = 0; j < v_n.size() && fin && it < getConfig().getMax_Iteraciones(); j++){

                    aux = factorizacion(seleccionado,v_n.get(j),M,costeTotal);

                    ++it;

                    /*Comprobación de si mejora o no la solución actual.*/
                    if(costeTotal < aux){
                        intercambiar(seleccionado,v_n.get(j),v_n,v_M);
                        costeTotal = aux;
                        fin = false; 
                    }
                }
            }
            System.out.println("ITERACIONES: "+it+" de "+getConfig().getMax_Iteraciones()+" :: "+costeTotal);
            
        }  
    }
    
    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    
    
    /**
     * @brief Método de intercambio de elementos.
     * @post Se realiza el intercambio entre elementos indicados como parámetros
     * para el HashSet de la solución, el ArrayList que simula a la solución Hashset.
     * A la misma vez, se hace el intercambio inverso al HashSet de los elementos no seleccionados
     * en la solución y en su ArrayList correspondiente.
     * @param seleccionado
     * @param j
     * @param v_n
     * @param v_M 
     */
    private void intercambiar(Integer seleccionado, Integer j,ArrayList<Integer> v_n, ArrayList<Integer> v_M){
        v_M.remove(seleccionado);
        v_M.add(j);
        M.remove(seleccionado);
        M.add(j);
        v_n.remove(j);
        v_n.add(seleccionado);
        n.remove(j);
        n.add(seleccionado);
    }
    
}
