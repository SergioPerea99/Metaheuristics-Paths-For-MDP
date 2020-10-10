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
public class BusquedaLocal extends Algoritmo{
    /*---------------- MÉTODOS PÚBLICOS ---------------*/
    
    public BusquedaLocal(String[] args, Integer num_archivo){
        super(args,num_archivo);
        //Generamos la primera solución candidata de partida a partir de un aleatorio.
        int i = 0, punto;
        Random random = new Random();
        random.Set_random(getConfig().getSemillas().get(0));

        while (i < getNum_candidatos()){
            punto = random.Randint(0, num_candidatos-1);
            System.out.println(punto);
            M.add(punto);
            n.remove(punto);
            ++i;
        }

    }
    
    public void algBusquedaLocal(){
//        //Primero vamos a buscar el de menor aporte.
//        HashSet<Integer> comprobados = new HashSet<>();
//        int seleccionado = -1, it = 0;
//        double costeMenor, costeMayor;
//        
//        //1 bucle: Por si debe de comprobar con los 50 puntos seleccionados escogiendo del menor al mayor en coste.
//        for(int i = 0; i < getNum_candidatos() && it < getMax_iteraciones(); i++){
//            costeMenor = puntoMenorAporte(comprobados,seleccionado);
//            //2 bucle: Busqueda de un punto no seleccionado que supere al punto de menor coste encontrado en este momento.
//            for(Integer j : n){
//                costeMayor = distanciasElemento(j);
//                if(costeMenor < costeMayor){
//                    intercambiar(seleccionado,j);
//                    factorizacion(costeMenor,costeMayor);
//                    costeMenor = costeMayor;
//                    ++it;
//                    break;
//                }
//            }
//        }
        
    }
    
    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    private double puntoMenorAporte(HashSet<Integer> comprobados, Integer seleccionado){
        double menorCoste = Integer.MAX_VALUE;
        for(Integer i : M){
            double dist_punto = distanciasElemento(i);
            if(menorCoste > dist_punto && comprobados.contains(i)){
                menorCoste = dist_punto;
                seleccionado = i;
            }
        }
        comprobados.add(seleccionado);
        return menorCoste;
    }
    
    private void intercambiar(Integer seleccionado, Integer j){
        M.remove(seleccionado);
        M.add(j);
        
        n.remove(j);
        n.add(seleccionado);
    }
    
    private void factorizacion(double costeMenor, double costeMayor){
        costeTotal = costeTotal + (costeMayor - costeMenor);
    }
}
