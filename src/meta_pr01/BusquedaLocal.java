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
    
    public BusquedaLocal(String[] args, Integer num_archivo,int sem){
        super(args,num_archivo);
        //Generamos la primera solución candidata de partida a partir de un aleatorio.
        int i = 0, punto;
        Random random = new Random();
        random.Set_random(getConfig().getSemillas().get(sem));
        
//        while (i < getNum_candidatos()){
//            punto = random.Randint(0, num_elementos-1);
//            M.add(punto);
//            n.remove(punto);
//            ++i;
//        }
        int[] v = {97, 418, 135, 50, 92, 102, 285, 293, 194, 24, 400, 189, 38, 29, 332, 238, 441, 3, 53, 289, 258, 450, 379, 231, 471, 254, 283, 416, 459, 207, 113, 312, 357, 336, 308, 329, 200, 245, 169, 315, 369, 382, 478, 429, 278, 351, 337, 361, 134, 61};
        while (i < getNum_candidatos()){
            M.add(v[i++]);
        }
    }
    
    public void algBusquedaLocal(){
        System.out.println(getM());
        costeTotal = costeSolucion();
        System.out.println("VALOR INICIAL DE LA SOLUCION: "+costeTotal);
        //Primero vamos a buscar el de menor aporte.
        HashSet<Integer> comprobados = new HashSet<>();        
        Integer seleccionado = -1;
        int it = 0;
        double costeMenor, costeMayor;
        boolean fin = false;
        ArrayList<Integer> v_M = new ArrayList<>(M);
        ArrayList<Integer> v_n = new ArrayList<>(n);
        //1 bucle: Por si debe de comprobar con los 50 puntos seleccionados escogiendo del menor al mayor en coste.
        while(it < getMax_iteraciones() && !fin){
            seleccionado = puntoMenorAporte(comprobados, v_M);
            costeMenor = distanciasElemento(seleccionado);
            //2 bucle: Busqueda de un punto no seleccionado que supere al punto de menor coste encontrado en este momento.
            fin = true;
            for(int j = 0; j < v_n.size() && fin && it < getMax_iteraciones(); j++){
                costeMayor = distanciasElemento(j);
                double aux = factorizacion(seleccionado,v_n.get(j),costeMenor,costeMayor,v_n,v_M);
                System.out.println("ITERACIONES: "+it+" de "+getMax_iteraciones()+" :: "+costeTotal);
                ++it;
                if(costeTotal < aux){
                    System.out.println("--------- INTERCAMBIAR: "+seleccionado+"("+costeMenor+") --> "+v_n.get(j)+"("+costeMayor+") -----------");
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
    
    private void intercambiar(Integer seleccionado, Integer j,ArrayList<Integer> v_n, ArrayList<Integer> v_M){
        //System.out.println("Saco posicion "+seleccionado+" ---> Entra posicion "+j);
        v_M.remove(seleccionado);
        v_M.add(j);
        M.remove(seleccionado);
        M.add(j);
        v_n.remove(j);
        v_n.add(seleccionado);
        n.remove(j);
        n.add(seleccionado);
    }
    
    private double factorizacion(int seleccionado,int j, double costeMenor, double costeMayor,ArrayList<Integer> v_n, ArrayList<Integer> v_M ){
        double costeMenos=0.0, costeMas=0.0;
        for (int k=0; k < M.size(); k++){
            if (v_M.get(k)!= seleccionado){
                if (getArchivo().getMatrizDatos()[seleccionado][v_M.get(k)] != 0)
                    costeMenos += getArchivo().getMatrizDatos()[seleccionado][v_M.get(k)];
                else
                    costeMenos += getArchivo().getMatrizDatos()[v_M.get(k)][seleccionado];
            }
            if (v_M.get(k)!= seleccionado){
                if(getArchivo().getMatrizDatos()[j][v_M.get(k)] != 0)
                    costeMas+= getArchivo().getMatrizDatos()[j][v_M.get(k)];
                else
                    costeMas+= getArchivo().getMatrizDatos()[v_M.get(k)][j];
            }
        }

        return costeTotal + costeMas-costeMenos;
        
    }
}
