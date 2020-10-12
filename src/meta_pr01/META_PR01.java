/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
                System.out.println("Si quiere terminar de ejecutar algoritmos pulse -1.");
                
                System.out.print("FICHERO DE DATOS QUE QUIERE EJECUTAR ([0,8] vector de datos): ");
                i = Integer.parseInt (br.readLine());
                        
                System.out.print("Escribe el algoritmo a querer usar (en minuscula): ");
                String eleccion = br.readLine();
                
                
                switch(eleccion){

                    /*----- <ALGORITMO GREEDY -----*/
                    case "greedy":
                        
                        Greedy greedy = new Greedy(args,i);
                        greedy.algoritmoGreedy();
                        System.out.println(greedy.getM());
                        System.out.println(greedy.costeSolucion());
                        break;

                    case "busqueda local":
                        System.out.print("Escribe la semilla a querer usar([0,n-1]): ");
                        int sem = Integer.parseInt (br.readLine());
                        BusquedaLocal b_local = new BusquedaLocal(args, i,sem);
                        b_local.algBusquedaLocal();
                        System.out.println(b_local.getM());
                        System.out.println(b_local.getCoste());
                        break;

                    default:
                        break;
                }
                System.out.println("");
            }while(i != -1);
            
        }catch(Exception e){
            System.err.println("DEBES INTRODUCIR UN VALOR VALIDO EN EL VECTOR DE ARCHIVOS.");
        };
                
    }
    
}
