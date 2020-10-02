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
public class META_PR01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Greedy greedy = new Greedy(args);
        
        //Comprobación de que funciona el insertar el elemento aleatorio 
        //y que desaparece del hashSet de elementos no candidatos.
        HashSet<Integer> solucionGreedy = greedy.algoritmoGreedy();
        
        greedy.algoritmoGreedy();
        
        
        System.out.println(greedy.getM());
        System.out.println(greedy.costeSolucion());
    }
    
}
