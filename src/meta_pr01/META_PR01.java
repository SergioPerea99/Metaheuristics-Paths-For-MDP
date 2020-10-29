/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

/**
 *
 * @author spdlc
 */
public class META_PR01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);
        
        try{
            int i;
            do{
                System.out.println("---------------------------------------------------");
                System.out.println("---------------------------------------------------");
                System.out.println("Si quiere terminar de ejecutar algoritmos escriba -1.");
                
                
                System.out.print("FICHERO DE DATOS QUE QUIERE EJECUTAR ([0,8] vector de datos): ");
                i = Integer.parseInt (br.readLine());
                
                if(i == -1)
                    break;
                
                System.out.print("Escribe el algoritmo a querer usar (en minuscula): ");
                String eleccion = br.readLine();

                System.out.print("Escribe la semilla a querer usar([0,nº_Semillas - 1]): ");
                int sem = Integer.parseInt (br.readLine());
                long ms;
                Date inicio, fin;
                switch(eleccion){

                    /*----- <ALGORITMO GREEDY -----*/
                    case "greedy":
                        
                        Greedy greedy = new Greedy(args,i,sem);
                        inicio = new Date();
                        greedy.algoritmoGreedy();
                        fin = new Date();
                        ms = fin.getTime()-inicio.getTime();
                        System.out.println(ms+" milisegundos.");
                        System.out.println(greedy.getM());
                        ArrayList<Integer> v_M = new ArrayList<>(greedy.getM());
                        System.out.println(greedy.costeSolucion(v_M));
                        break;

                    case "busqueda local":
                        
                        BusquedaLocal b_local = new BusquedaLocal(args, i,sem);
                        inicio = new Date();
                        b_local.algBusquedaLocal();
                        fin = new Date();
                        ms = fin.getTime()-inicio.getTime();
                        System.out.println(ms+" milisegundos.");
                        System.out.println(b_local.getM());
                        System.out.println(b_local.getCoste());
                        break;

                    case "busqueda tabu":
                        BusquedaTabu b_tabu = new BusquedaTabu(args, i, sem);
                        inicio = new Date();
                        b_tabu.algBusquedaTabu();
                        fin = new Date();
                        ms = fin.getTime()-inicio.getTime();
                        System.out.println(ms+" milisegundos.");
                        break;
                    
                    default:
                        break;
                }
                System.out.println("");
            }while(i != -1);
            
        }catch(Exception e){
            System.err.println("DEBES INTRODUCIR UN VALOR VALIDO.");
        };
                
    }
    
}
