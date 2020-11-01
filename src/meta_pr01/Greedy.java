/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

/**
 *
 * @author spdlc
 */
public class Greedy extends Algoritmo{

    /*---------------- MÉTODOS PÚBLICOS ---------------*/
    
    /**
     * @brief Constructor parametrizado.
     * @post Creamos el greedy a partir de la superclase algoritmo que nos proporciona todos los datos
     * de los archivos necesarios: matriz de datos, hashSet de solución candidata, numero de candidatos...
     * @param args Parametro necesario para la posible lectura en la clase abstracta de los datos.
     * @param archivo
     * @param sem
     */
    public Greedy(String[] args, ArchivoDatos archivo,Integer sem){
        super(args, archivo);
        //Generamos el primer punto aleatorio a partir de la semilla y lo añadimos al hashSet solución.
        Random random = new Random();
        random.Set_random(getConfig().getSemillas().get(sem));
        
        int puntoInicio = random.Randint(0,getNum_elementos()-1);      
        M.add(puntoInicio);
        n.remove(puntoInicio);
    }
    
    /**
     * @brief Algoritmo Greedy.
     * @post A partir de un elemento random, se busca el siguiente elemento cuya
     * suma de sus distancias con respecto a los puntos seleccionados sea mayor que 
     * los demás elementos aún no seleccionados con los seleccionados.
     */
    public void algoritmoGreedy(){
        
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
            
            M.add(elem_seleccionado);
            n.remove(elem_seleccionado);

        }
        
    }

}
