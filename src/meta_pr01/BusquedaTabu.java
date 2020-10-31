/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import javafx.util.Pair;

/**
 *
 * @author spdlc
 */
public class BusquedaTabu extends Algoritmo{
    
    /*ESTRUCTURAS ADICIONALES DE LA BÚSQUEDA TABÚ NECESARIAS.*/
    private final LinkedList<Integer> lista_tabu;
    private final ArrayList<Pair<Integer,Integer>> mem_largo_plazo;
    
    /*ATRIBUTOS DE LA MEJOR SOLUCIÓN ENCONTRADA.*/
    private double coste_actual;
    private final HashSet<Integer> solucion_actual;

    /**
     * @brief Constructor parametrizado.
     * @param args
     * @param num_archivo
     * @param sem 
     */
    public BusquedaTabu(String[] args, Integer num_archivo,int sem){
        
        super(args,num_archivo);
        
        /*Generamos la primera solución candidata de partida a partir de un aleatorio.*/
        int i = 0, punto;
        random.Set_random(getConfig().getSemillas().get(sem));
        
        /*INSTANCIO LA PRIMERA SOLUCIÓN VÁLIDA.*/
        while (M.size() < getNum_candidatos()){
            punto = random.Randint(0, num_elementos-1);
            M.add(punto);
            n.remove(punto);
            ++i;
        }
        
        /*INSTANCIO E INICIALIZA LA LISTA TABÚ Y MEMORIA A LARGO PLAZO.*/
        lista_tabu = new LinkedList<>();
        mem_largo_plazo = new ArrayList<>(num_elementos);
        limpiarMemorias();

        solucion_actual = new HashSet<>(M);  

    }
    
    
    
    /**
     * @brief Búsqueda Tabú.
     * @post Búsqueda de una solución óptima a partir de una solución inicial, una lista tabú de elementos sacados de la solución
     * recientemente y una memoria a largo plazo que indica las frecuencias de los elementos en el conjunto de la solución.
     * A partir de un número de intentos de mejora a la mejor Solución encontrada en todo el algoritmo, se realiza un reinicio del
     * algoritmo desde un conjunto de elementos para la solución generado a partir de la estructura de frecuencias.
     */
    public void algBusquedaTabu(){

        /*Inicialización de estructuras y variables necesarias.*/
        ArrayList<Integer> solucion_temp = new ArrayList<>(solucion_actual);
        ArrayList<Pair<Integer,Double>> v_distancias = new ArrayList<>();
        HashSet<Integer> vecinos = new HashSet<>(); //NO COGER MISMOS VECINOS.
        
        
        costeTotal = costeSolucion(solucion_temp); //MEJOR COSTE ENCONTRADO. SIENDO M LA MEJOR SOLUCION ENCONTRADA.
        coste_actual = costeTotal; //COSTE ACTUAL. SIENDO COSTE_ACTUAL EL MEJOR COSTE ACTUAL ENCONTRADO.

        int it = 0, reinicio = 0; //IT para las iteraciones. REINICIO para controlar cuando llamar a diversificar/intensificar una nueva solución.
        int numVecinos, seleccionado = -1, selecAñadir, selecQuitar; //Control de número de vecinos, elemento a eliminar de la solución y elemento a insertar en la solución.
        double aux, costeAux; //Costes auxiliares de soluciones que se van generando.
        
        int entorno = M.size()*n.size(); //ENTORNO COMPLETO: |E| = M*(N-M).
        int vecindario = entorno; //VECINDARIO QUE SE VA A IR GENERANDO POR ITERACION.
        
        
        /*HASTA QUE ITERE EL MAXIMO DE ITERACIONES INDICADAS.*/
        while(it < getConfig().getMax_Iteraciones()){
            
            /*Reinicio de valores necesario*/
            numVecinos = 0; //Limpiar contador de vecinos generados.
            costeAux = 0; //Limpiar mejor coste entre los vecinos.
            selecAñadir = seleccionado; //Será el primer elemento de la solución parcial en ser quitado una vez encuentra 1 vecino.
            selecQuitar = -1;
            vecinos.clear(); //Limpiar vecinos generados.
            solucion_temp.clear();
            solucion_temp = new ArrayList<>(solucion_actual);

            ordenacionMenorAporte(v_distancias,solucion_actual); //Ordenacion del vector de aportes.
            
            
            for(int i =0; i < M.size() && numVecinos < vecindario; i++){
                
                seleccionado = v_distancias.get(i).getKey();
                
                for(int j = 0; j < n.size() && numVecinos < vecindario; j++){
                    
                    int nuevoVecino = generaVecino(vecinos); /*Controlo que no intente añadir un vecino ya mirado, no sea tabú y que sea válido.*/

                    if(nuevoVecino != -1){ /*EN CASO DE SER VECINO VÁLIDO*/
                        aux = factorizacion(seleccionado,nuevoVecino,solucion_actual,coste_actual);

                        if(costeAux < aux){ 
                            if(!intercambiar(seleccionado,nuevoVecino,solucion_temp))
                                --numVecinos;
                            else{
                                costeAux = aux;
                                selecAñadir = nuevoVecino; //CAMBIAR SIEMPRE RESPECTO AL ELEMENTO QUE MENOR VALOR NOS DIÓ EN UN PRINCIPIO, MACHACANDO AL VECINO ÚLTIMO QUE LO CAMBIASE.
                                selecQuitar = seleccionado;
                            }
                        }
                        ++numVecinos;
                    }
                }
            }
            
            solucion_actual.remove(selecQuitar);
            solucion_actual.add(selecAñadir);
            coste_actual = costeAux;
            
            if (vecindario > getConfig().getMIN_VECINOS())
                vecindario = (int)(vecindario * getConfig().getREDUCCION_VECINDARIO());
            
            n.remove(selecAñadir); //ELIMINO EL VECINO QUE HA SIDO INSERTADO EN LA SOLUCION ACTUAL.
            
            /*ACTUALIACION MEM CORTO PLAZO.*/
            int entrar = lista_tabu.pollLast(); //ELIMINA EL ULTIMO ELEMENTO DE LA LISTA.
            lista_tabu.push(seleccionado); //AÑADE AL INICIO DE LA LISTA.
            if(entrar != -1)
                n.add(entrar); //AÑADIR A POSIBLE ELEMENTO QUE SE GENERE COMO VECINO SI SALE DE LA LISTA TABÚ.
            
            /*ACTUALIZACION DE MEM LARGO PLAZO.*/
            Pair<Integer,Integer> modificar;
            for(int i = 0; i < mem_largo_plazo.size(); i++)
                if(solucion_actual.contains(i)){
                    modificar = new Pair<>(i,mem_largo_plazo.get(i).getValue() + 1);
                    mem_largo_plazo.set(i, modificar);
                }
            
            
            if(costeTotal < coste_actual){ /*¿SOLUCIÓN ACTUAL ES MEJOR A LA MEJOR SOLUCIÓN QUE HABÍAMOS ENCONTRADO DESDE EL INICIO?*/
                
                M = new HashSet<>(solucion_actual);
                costeTotal = coste_actual;
                reinicio = 0;
                
            }else
                ++reinicio;
            
            System.out.println("ITER "+it+" ("+getConfig().getMax_Iteraciones()+") :: "+vecindario+" vecinos :: Nº intentos "+reinicio+" :: Coste actual -> "+coste_actual+"("+costeTotal+" con "+M.size()+" elementos).");
            it++;
            
            
            if(reinicio >= getConfig().getINTENTOS_REINICIO()){ /*EN CASO DE GENERAR SOLUCIÓN ACTUAL TOTALMENTE NUEVA.*/
                reinicio = 0;

                solucionPorFrecuencias(); //GENERA UNA SOLUCION ACTUAL COMPLETAMENTE NUEVA A PARTIR DE LA MEMORIA A LARGO PLAZO.
                limpiarMemorias(); //REINICIO DE LAS MEMORIAS.
                vecindario = entorno; //REINICIO DE NUEVO LOS VECINOS A GENERAR A PARTIR DE UNA SOLUCIÓN.
                
                if(costeTotal < coste_actual){ /*¿SOLUCIÓN ACTUAL ES MEJOR A LA MEJOR SOLUCIÓN QUE HABÍAMOS ENCONTRADO DESDE EL INICIO?*/
                    
                    M.clear();
                    solucion_actual.forEach((i) -> { //AÑADE ELEMENTO A ELEMENTO DE LA SOLUCION ACTUAL.
                        M.add(i); 
                    });
                    costeTotal = coste_actual;
                    
                }
            }
            
            ++it;
        }
    }
    
    
    
    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    
    /**
     * @brief Método de intercambio de elementos.
     * @post A partir de una solución temporal, se realizan los cambios entre el elemento correspondiente
     * al parametro seleccionado y el parametro añadir. En caso de que dicho intercambio conlleve no mantener
     * los elementos que debe de tener una solución, entonces deshace el intercambio y devuelve false. Si todo 
     * funciona bien, devolverá un true.
     * @param seleccionado
     * @param añadir
     * @param solucion_temp
     * @return Booleano que indica si se ha hecho un intercambio correcto o no.
     */
    private boolean intercambiar(Integer seleccionado, Integer añadir, ArrayList<Integer> solucion_temp){
        solucion_temp.remove(seleccionado);
        solucion_temp.add(añadir);
        if (solucion_temp.size() == num_candidatos){
            return true;
        }else if (solucion_temp.size() < num_candidatos){
            solucion_temp.add(seleccionado);
            return false;
        }else{
            solucion_temp.remove(añadir);
            return false;
        }
    }
    
    
    
    /***
     * @brief Actualizar solución actual por estancamiento.
     * @post A partir de la memoria a largo plazo, se ordena la memoria a largo plazo
     * y a partir de un número aleatorio se decide si intensificar hacia una nueva solución
     * actual o si diversificar.
     */
    private void solucionPorFrecuencias(){

        mem_largo_plazo.sort((o1,o2) -> o1.getValue().compareTo(o2.getValue())); /*ORDENACION DE LA MEMORIA A LARGO PLAZO.*/
        solucion_actual.clear(); /*LIMPIAR LA SOLUCION ACTUAL.*/
        
        double aleatorio = random.Randfloat(0, 1); /*ALEATORIO DE PORCENTAJE PARA VER SI INTENSIFICA O DIVERSIFICA HACIA UNA NUEVA SOLUCION ACTUAL.*/
        int i = 0, j = num_elementos-1;
        if(aleatorio < getConfig().getPROB_INTENSIFICAR_DIVERSIFICAR()){ /*DIVERSIFICARÁ.*/
            while (solucion_actual.size() < num_candidatos)
                solucion_actual.add(mem_largo_plazo.get(i++).getKey());
            System.out.print("DIVERSIFICACIÓN (SOLUCION ACTUAL) --> "+getConfig().getPROB_INTENSIFICAR_DIVERSIFICAR());
            //getConfig().setPROB_INTENSIFICAR_DIVERSIFICAR(getConfig().getPROB_INTENSIFICAR_DIVERSIFICAR() - 0.01);
        }else{ /*INTENSIFICARÁ.*/
            while (solucion_actual.size() < num_candidatos)    
                solucion_actual.add(mem_largo_plazo.get(j--).getKey());
            System.out.print("INTENSIFICACIÓN (SOLUCION ACTUAL)");
        }
        
        ArrayList<Integer> aux = new ArrayList<>(solucion_actual);
        coste_actual = costeSolucion(aux); //OPERACION DEL NUEVO COSTE DE LA SOLUCION ACTUAL.
        System.out.println(" :: coste actual --> "+coste_actual);
        aux.sort((o1,o2) -> o1.compareTo(o2));
        
    } 
    
    /**
     * @brief Generar vecino válido.
     * @post A partir de un aleatorio, se genera un elemento válido a añadir como vecino.
     * Esto incluye que dicho vecino no se encuentre en la lista tabú de la memoria a
     * corto plazo.
     * @return Integer que indica el nuevo vecino generado.
     */
    private int generaVecino(HashSet<Integer> vecinos){
        
        int i = random.Randint(0, num_elementos-1); //ALEATOIO ENTRE LOS ELEMENTOS (0,499).
        
        if(!vecinos.contains(i) && !lista_tabu.contains(i) && !solucion_actual.contains(i)){ /*ALEATORIO QUE NO SEA VECINO GENERADO ANTERIORMENTE, QUE NO SEA UN VECINO TABÚ Y QUE DICHO ELEMENTO NO SE ENCUENTRE YA EN LA SOLUCIÓN.*/
            vecinos.add(i);
            return i;  
        }
        
        return -1;
    }
    
    
    /**
     * @brief Iniciar/Reiniciar valores de memorias.
     * @post Inicia/Reinicia los valores de la lista tabú y de la estructura que mantiene la memoria a largo plazo.
     */
    private void limpiarMemorias(){
        /*LISTA TABÚ.*/
        lista_tabu.clear();
        for (int i = 0; i < getConfig().getTENENCIA_TABU(); i++) {
            lista_tabu.add(-1);
        }
        
        /*MEMORIA A LARGO PLAZO.*/
        mem_largo_plazo.clear();
        Pair<Integer, Integer> añadir;
        for (int j = 0; j < num_elementos; j++) {
            añadir = new Pair<>(j, 0);
            mem_largo_plazo.add(j, añadir);
        }
        mem_largo_plazo.sort((o1, o2) -> o1.getKey().compareTo(o2.getKey()));
    }
}

