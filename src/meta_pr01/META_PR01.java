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
                System.out.println("Si quiere terminar de ejecutar el algoritmo pulse -1.");
                System.out.print("FICHERO DE DATOS QUE QUIERE EJECUTAR ([0,9] vector de datos): ");
                
                i = Integer.parseInt (br.readLine());
                Greedy greedy = new Greedy(args,i);
                //Greedy greedy = new Greedy(args,i);
                Vector<Greedy> vGreedys = new Vector<>();

                //Comprobación de que funciona el insertar el elemento aleatorio 
                //y que desaparece del hashSet de elementos no candidatos.
                HashSet<Integer> solucionGreedy = greedy.algoritmoGreedy();

                greedy.algoritmoGreedy();


                System.out.println(greedy.getM());
                System.out.println(greedy.costeSolucion());
            }while(i != -1);
            
        }catch(Exception e){
            System.err.println("DEBES INTRODUCIR UN VALOR VALIDO EN EL VECTOR DE ARCHIVOS.");
        };
        
        
    }
    
}
