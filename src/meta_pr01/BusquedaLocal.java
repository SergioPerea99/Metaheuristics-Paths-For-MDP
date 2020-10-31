/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.ArrayList;
import java.util.HashSet;

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
        HashSet<Integer> comprobados = new HashSet<>();     
        
        /*GENERO EL VALOR DE LA SOLUCION INICIAL VÁLIDA*/
        costeTotal = costeSolucion(v_M);
        
        Integer seleccionado = -1;
        int it = 0;
        boolean fin = false;
        double aux;
        
        //1 bucle: Por si debe de comprobar con los 50 puntos seleccionados escogiendo del menor al mayor en coste.
        while(it < getConfig().getMax_Iteraciones() && !fin){
            seleccionado = puntoMenorAporte(comprobados, v_M);
            
            //2 bucle: Busqueda de un punto no seleccionado que supere al punto de menor coste encontrado en este momento.
            fin = true;
            for(int j = 0; j < v_n.size() && fin && it < getConfig().getMax_Iteraciones(); j++){
                
                aux = factorizacion(seleccionado,v_n.get(j),v_n,v_M);
                
                System.out.println("ITERACIONES: "+it+" de "+getConfig().getMax_Iteraciones()+" :: "+costeTotal);
                ++it;
                
                /*Comprobación de si mejora o no la solución actual.*/
                if(costeTotal < aux){
                    intercambiar(seleccionado,v_n.get(j),v_n,v_M);
                    costeTotal = aux;
                    comprobados.clear();
                    fin = false; 
                }
            }
            
        }  
    }
    
    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    private int puntoMenorAporte(HashSet<Integer> comprobados, ArrayList<Integer> v_M){
        double dist_punto;
        int pos = -1;
        double menorCoste = distanciasElemento(v_M.get(0));
        for(int i = 1; i < v_M.size(); i++){
            dist_punto = distanciasElemento(v_M.get(i));
            if(menorCoste > dist_punto && !comprobados.contains(v_M.get(i))){
                menorCoste = dist_punto;
                pos = i;
            }
        }
        if (pos != -1){
            comprobados.add(v_M.get(pos));
            return v_M.get(pos);
        }
        return v_M.get(0);
    }
    
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
    
    private double factorizacion(int seleccionado,int j,ArrayList<Integer> v_n, ArrayList<Integer> v_M ){
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

        return costeTotal + costeMayor-costeMenor;
        
    }
}
