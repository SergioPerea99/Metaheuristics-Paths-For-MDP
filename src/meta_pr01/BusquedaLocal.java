/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.ArrayList;
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
     * @param archivo
     * @param sem 
     */
    public BusquedaLocal(String[] args, ArchivoDatos archivo,Integer sem){
        
        super(args,archivo);
        
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
    
    /**
     * @brief Búsqueda local del primer mejor.
     * @post Búsqueda local del primero mejor; es decir, recorre el vecindario de forma que si encuentra 
     * un vecino que mejore a la solución entonces se realiza el movimiento hacia ese vecino y se vuelve a empezar
     * el proceso de encontrar un vecino que mejore. En caso de recorrer el vecindario y no mejorar, entonces acabará
     * dicha búsqueda.
     */
    public void algBusquedaLocal(){

        
        /*Inicialización de estructuras y variables necesarias.*/
        ArrayList<Integer> v_M = new ArrayList<>(M); //ArrayList del conjunto de elementos que forman la SOLUCIÓN.
        ArrayList<Integer> v_n = new ArrayList<>(n); //ArrayList del conjunto de elementos que NO forman parte de la solución.
        ArrayList<Pair<Integer,Double>> v_distancias = new ArrayList<>(); //ArrayList ordenado de las distancias de menor a mayor aporte de los elementos de la solución respecto a los demás.
        
        
        costeTotal = costeSolucion(v_M); /*GENERO EL VALOR DE LA SOLUCION INICIAL */
        
        int seleccionado; // Elemento seleccionado a ser eliminado de la solución.
        int it = 0; //Contador de iteraciones.
        boolean fin = false; //Booleano que representa si hay mejora o no (Controla el estancamiento de la busqueda local).
        double aux; //Valor auxiliar que devuelve el método de factorización.
        
        
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
            //System.out.println("ITERACIONES: "+it+" de "+getConfig().getMax_Iteraciones()+" :: "+costeTotal);
            
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
