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
    
    /*ATRIBUTOS ESTÁTICOS*/
    //private static final int TENENCIA_TABU = 5;
    //private static final double PORCENTAJE_REINICIO_MLP = 0.5; //Porcentaje que indica el corte para DIVERSIFICAR (< 50) o INTENSIFICAR (>= 50).
    
    /*ESTRUCTURAS ADICIONALES DE LA BÚSQUEDA TABÚ NECESARIAS.*/
    private final LinkedList<Integer> lista_tabu;
    private ArrayList<Pair<Integer,Integer>> mem_largo_plazo;
    
    /*ATRIBUTOS DE LA MEJOR SOLUCIÓN ENCONTRADA.*/
    private double coste_actual;
    private HashSet<Integer> solucion_actual;

    
    
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
        inicializarMemorias();

        solucion_actual = new HashSet<>(M);  

    }
    
    
    
    public void algBusquedaTabu(){

        /*Inicialización de estructuras y variables necesarias.*/
        ArrayList<Integer> solucion_temp = new ArrayList<>(solucion_actual);
        
        costeTotal = costeSolucion(solucion_temp); //MEJOR COSTE ENCONTRADO. SIENDO M LA MEJOR SOLUCION ENCONTRADA.
        coste_actual = costeTotal; //COSTE ACTUAL. SIENDO COSTE_ACTUAL EL MEJOR COSTE ACTUAL ENCONTRADO.

        HashSet<Integer> comprobados = new HashSet<>(); //NO SELECCIONAR ELEMENTO DE MENOR VALOR YA COMPROBADO.
        HashSet<Integer> vecinos = new HashSet<>(); //NO COGER MISMOS VECINOS.
        
        int it = 0, reinicio = 0; //IT para las iteraciones. REINICIO para controlar cuando llamar a diversificar/intensificar una nueva solución.
        int numVecinos, seleccionado, selecAux; //Control de número de vecinos, elemento a eliminar de la solución y elemento a insertar en la solución.
        double aux, costeAux; //Costes auxiliares de soluciones que se van generando.
        System.out.println(n.size());
        
        int entorno = M.size()*n.size(); //ENTORNO COMPLETO: |E| = M*(N-M).
        int vecindario = entorno; //VECINDARIO QUE SE VA A IR GENERANDO POR ITERACION.
        
        /*HASTA QUE ITERE EL MAXIMO DE ITERACIONES INDICADAS.*/
        while(it < getConfig().getMax_Iteraciones()){
            /*Selecciono el elemento que se sacará de la solución actual.*/
            seleccionado = puntoMenorAporte(comprobados);
            
            /*Reinicio de valores necesario*/
            numVecinos = 0; //Limpiar contador de vecinos generados.
            costeAux = 0; //Limpiar mejor coste entre los vecinos.
            selecAux = seleccionado; //Será el primer elemento de la solución parcial en ser quitado una vez encuentra 1 vecino.
            vecinos.clear(); //Limpiar vecinos generados.
            
            /*BUSQUEDA DE N VECINOS VÁLIDOS.*/
            while(numVecinos < vecindario){
                
                /*Controlo que no intente añadir un vecino ya mirado, no sea tabú y que sea válido.*/
                int nuevoVecino = generaVecino(vecinos);
                
                
                if(nuevoVecino != -1){ /*EN CASO DE SER VECINO VÁLIDO*/
                    aux = factorizacion(seleccionado,nuevoVecino);
                    /*LA PRIMERA VEZ ENTRA SEGURO, YA QUE ES EL MEJOR VECINO ENCONTRADO HASTA EL MOMENTO.*/
                    if(costeAux < aux){ 
                        if(!intercambiar(selecAux,nuevoVecino))
                            --numVecinos;
                        else{
                            costeAux = aux;
                            selecAux = nuevoVecino; //CAMBIAR SIEMPRE RESPECTO AL ELEMENTO QUE MENOR VALOR NOS DIÓ EN UN PRINCIPIO, MACHACANDO AL VECINO ÚLTIMO QUE LO CAMBIASE.
                        }
                    }
                    ++numVecinos;
                }
            }
            System.out.println("NUMERO DE VECINOS GENERADOS: "+numVecinos);
            if (vecindario > 10)
                vecindario = (int)(vecindario * getConfig().getREDUCCION_VECINDARIO());
            
            n.remove(selecAux); //ELIMINAR ELEMENTO A GENERAR COMO VECINO EL INSERTADO EN LA SOLUCION ACTUAL.
            comprobados.clear(); //Limpiar el hashSet que indica qué elemento de menor aporte había sido ya seleccionado.
            
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
            
            /*¿SOLUCIÓN ACTUAL ES MEJOR A LA MEJOR SOLUCIÓN QUE HABÍAMOS ENCONTRADO DESDE EL INICIO?*/
            if(costeTotal < coste_actual){
                M.remove(seleccionado);
                M.add(selecAux);
                
                if(M.size() == num_candidatos)
                    costeTotal = coste_actual;
                else
                    System.out.println("ERROR EN LA INSERCION DE ELEMENTOS DEL VECTOR DE LA MEJOR SOLUCIÓN FINAL. TAMAÑO de M: "+M.size());
                reinicio = 0;
            }else
                ++reinicio;
            
            System.out.println("ITER "+it+" de "+getConfig().getMax_Iteraciones()+" :: Nº intentos "+reinicio+" :: "+coste_actual+" con "+solucion_actual.size()+"("+costeTotal+" con "+M.size()+")");
            it++;
            
            
            
            /*EN CASO DE GENERAR SOLUCIÓN ACTUAL TOTALMENTE NUEVA.*/
            if(reinicio >= getConfig().getINTENTOS_REINICIO()){
                reinicio = 0;
                
                /*Se usa en este caso, la memoria a largo plazo*/
                solucionPorFrecuencias(); //GENERA UNA SOLUCION ACTUAL COMPLETAMENTE NUEVA.
                
                inicializarMemorias(); //REINICIO DE LAS MEMORIAS.

                /*¿SOLUCIÓN ACTUAL ES MEJOR A LA MEJOR SOLUCIÓN QUE HABÍAMOS ENCONTRADO DESDE EL INICIO?*/
                if(costeTotal < coste_actual){
                    /*DEBE LIMPIAR POR COMPLETO A LA MEJOR SOLUCION ENCONTRADA FINAL Y COPIAR LOS ELEMENTOS DE LA SOLUCION ACTUAL GENERADA A PARTIR DE LA MEMORIA A LARGO PLAZO.*/
                    M.clear();
                    
                    solucion_actual.forEach((i) -> { //AÑADE ELEMENTO A ELEMENTO DE LA SOLUCION ACTUAL.
                        M.add(i); 
                    });

                    if (M.size() == num_candidatos)
                        costeTotal = coste_actual;
                    else
                        System.out.println("ERROR EN LA INSERCION DE ELEMENTOS DEL VECTOR DE LA MEJOR SOLUCIÓN FINAL. TAMAÑO de M: "+M.size());
                }
            }
            
            ++it;
        }
    }
    
    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    private int puntoMenorAporte(HashSet<Integer> comprobados) {
        ArrayList<Integer> solucion_temp = new ArrayList<>(solucion_actual);
        double dist_punto;
        int pos = -1;
        double menorCoste = distanciasElemento(solucion_temp.get(0));
        for(int i = 1; i < solucion_temp.size(); i++){
            dist_punto = distanciasElemento(solucion_temp.get(i));
            if(menorCoste > dist_punto && !comprobados.contains(solucion_temp.get(i))){
                menorCoste = dist_punto;
                pos = i;
            }
        }
        if (pos != -1){
            comprobados.add(solucion_temp.get(pos));
            return solucion_temp.get(pos);
        }
        comprobados.add(solucion_temp.get(0));
        return solucion_temp.get(0);
    }
    
    private boolean intercambiar(Integer seleccionado, Integer j){
        solucion_actual.remove(seleccionado);
        solucion_actual.add(j);
        if(num_candidatos == solucion_actual.size())
            return true;
        else if (num_candidatos > solucion_actual.size()){
            solucion_actual.add(seleccionado);
            return false;
        }else{
            solucion_actual.remove(j);
            return false;
        }
    }
    
    private double factorizacion(int seleccionado,int j){
        ArrayList<Integer> v_M = new ArrayList<>(solucion_actual);
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

        return coste_actual + costeMayor-costeMenor;
        
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
            System.out.println("DIVERSIFICACIÓN (SOLUCION ACTUAL) --> tamaño solución actual : "+solucion_actual.size());
        }else{ /*INTENSIFICARÁ.*/
            while (solucion_actual.size() < num_candidatos)    
                solucion_actual.add(mem_largo_plazo.get(j--).getKey());
            System.out.println("INTENSIFICACIÓN (SOLUCION ACTUAL) --> tamaño solución actual : "+solucion_actual.size());
        }
        
        ArrayList<Integer> aux = new ArrayList<>(solucion_actual);
        coste_actual = costeSolucion(aux); //OPERACION DEL NUEVO COSTE DE LA SOLUCION ACTUAL.
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
        ArrayList<Integer> v_n = new ArrayList<>(n);
        for(int i = 0; i < v_n.size(); i++){
            i = random.Randint(0, v_n.size()-1); //POSICION ALEATORIA ENTRE LOS ELEMENTOS QUE PUEDEN SER SELECCIONADOS PARA FORMAR EL SIGUIENTE VECINO.
            if(!vecinos.contains(v_n.get(i)) && !lista_tabu.contains(v_n.get(i)) && !solucion_actual.contains(v_n.get(i))){
                vecinos.add(i);
                return i;  
            }
        }
        return -1;
    }
    
    
    /**
     * @brief Iniciar/Reiniciar valores de memorias.
     * @post Inicia/Reinicia los valores de la lista tabú de la estructura que mantiene la memoria a largo plazo.
     */
    private void inicializarMemorias(){
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

