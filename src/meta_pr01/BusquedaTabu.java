/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author spdlc
 */
public class BusquedaTabu extends Algoritmo{
    /*ATRIBUTOS ESTÁTICOS*/
    private static final int TENENCIA_TABU = 5;
    private static final int INTENTOS_REINICIO = 100;
    private static final int PORCENTAJE_REINICIO_MLP = 50; //Porcentaje que indica el corte para intensificar (< 50) o diversificar (>= 50).
    
    /*ESTRUCTURAS ADICIONALES DE LA BÚSQUEDA TABÚ NECESARIAS.*/
    private final LinkedList<Integer> lista_tabu;
    private final ArrayList<Integer> mem_largo_plazo;
    
    /*ATRIBUTOS DE LA MEJOR SOLUCIÓN ENCONTRADA.*/
    private double coste_actual;
    private ArrayList<Integer> solucion_actual;
    
    /*ATRIBUTOS EXTRA PARA EVITAR COMPLICACIONES.*/
    int semilla;
    
    
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
        Random random = new Random();
        semilla = sem;
        random.Set_random(getConfig().getSemillas().get(semilla));
        
        /*INSTANCIO LA PRIMERA SOLUCIÓN VÁLIDA.*/
        while (M.size() < getNum_candidatos()){
            punto = random.Randint(0, num_elementos-1);
            M.add(punto);
            n.remove(punto);
            ++i;
        }
        
        /*INSTANCIO LA LISTA TABÚ.*/
        lista_tabu = new LinkedList<>();
        for (int j = 0; j < TENENCIA_TABU; j++)
            lista_tabu.push(-1);
        
        /*INSTANCIO LA MEMORIA A LARGO PLAZO.*/
        mem_largo_plazo = new ArrayList<>(num_elementos);
        for (int j = 0; j < num_elementos; j++)
            mem_largo_plazo.add(0);
        
        /*GENERO EL VALOR DE LA SOLUCION VÁLIDA INICIAL Y INDICO COMO MEJOR SOLUCIÓN DICHO COSTE*/
        costeTotal = costeSolucion();
        coste_actual = costeTotal;
        solucion_actual = new ArrayList<>(M);  
        
    }
    
    
    
    public void algBusquedaTabu(){
        
        /*Inicialización de estructuras y variables necesarias.*/
        ArrayList<Integer> solucion_temp = new ArrayList<>(solucion_actual);
        
        HashSet<Integer> comprobados = new HashSet<>();
        HashSet<Integer> vecinos = new HashSet<>();
        
        int it = 0, reinicio = 0, numVecinos, seleccionado, selecAux;
        double aux, costeAux;
        
        
        System.out.println(solucion_actual);
        System.out.println(coste_actual);
        //1 bucle: Por si debe de comprobar con los 50 puntos seleccionados escogiendo del menor al mayor en coste.
        while(it < getMax_iteraciones()){
            seleccionado = puntoMenorAporte(comprobados);
            System.out.println("ELEMENTO A QUITAR --> "+seleccionado+" :: "+distanciasElemento(seleccionado));
            numVecinos = 0;
            costeAux = 0;
            vecinos.clear();
            
            selecAux = seleccionado;
            //2 bucle: Busqueda de un punto no seleccionado que supere al punto de menor coste encontrado en este momento.
            while(numVecinos < 10){
                
                /*Controlo que no intente añadir un vecino ya mirado, no sea tabú y que sea válido.*/
                int nuevoVecino = generaVecino(vecinos);
                
                if(nuevoVecino != -1){
                    numVecinos++;
                    /*Una vez tengo un vecino valido, compruebo si mejora a la solucion actual o no.*/
                    aux = factorizacion(seleccionado,nuevoVecino,solucion_temp);
                
                    //System.out.println("VECINO GENERADO -> "+nuevoVecino+" :: FACTORIZACION CON ESE ELEMENTO -> "+aux);
                    /*Comprobación de si mejora o no un coste parcial para encontrar al mejor vecino del entorno de vecinos.*/
                    if(costeAux < aux){
                        //System.out.println(selecAux+" :: Intercambiando valor -> "+(costeAux - coste_actual));
                        intercambiar(selecAux,nuevoVecino,solucion_temp);
                        //System.out.println(nuevoVecino+" :: Bien intercambiado a valor -> "+(aux - coste_actual));
                        costeAux = aux;
                        selecAux = nuevoVecino; //Para que si cambia que sea siempre respecto al mismo elemento y no otro.
                    }
                }
            }
            
            /*Una vez haya 10 vecinos válidos generados y haber escogido al mejor de ellos, lo colocamos como solución actual (sea mejor o peor).*/
            coste_actual = costeAux;
            intercambiar(seleccionado, selecAux, solucion_actual); //Intercambio el mejor elemento de los vecinos por el peor elemento de la solución actual.
            System.out.println("ITERACIONES: "+it+" de "+getMax_iteraciones()+" en este numero de intentos "+reinicio+" :: "+coste_actual+"("+costeTotal+")");
            it++;
            
            /*Elimino el elemento que ya no se encuentra como posible vecino.*/
            n.remove(selecAux); 
            
            /*Actualizo memoria a corto plazo*/
            int entrar = lista_tabu.pollLast();
            lista_tabu.push(seleccionado);
            if(entrar != -1)
                n.add(entrar);
            
            
            /*Actualizo memoria a largo plazo*/
            for(int i = 0; i < mem_largo_plazo.size(); i++)
                if(solucion_actual.contains(i))
                    mem_largo_plazo.set(i,mem_largo_plazo.get(i) + 1);
        
            /*Compruebo si la solución Actual mejora a la mejor solución. Si no se mejora, se suma 1 al reinicio.*/
            if(costeTotal < coste_actual){
                
                /*Entonces, actualizo la mejor solución obtenida.*/
                costeTotal = coste_actual;
                
                //TODO: NO HACE FALTA LIMPIAR ENTERO, SOLO HE CAMBIADO 1 ELEMENTO !!!!!
                M.clear();
                M.addAll(solucion_actual); //Añado todos los elementos de la solución Actual en el HashSet de mi solución.
                comprobados.clear(); //Limpiar el hashSet que indica que elemento de menor aporte ha sido ya seleccionado.
            
                reinicio = 0;
            }else{
                ++reinicio;
                System.out.println("NO MEJORA :: "+reinicio);
            }
            
            ///////////////// HASTA AQUI PARA QUE TODO VA BIEN Y REVISADO !!!
            
            /*¿Habrá que reiniciar? Sí, en caso de que reinicio sea >= máximo de intentos.*/
            if(reinicio >= INTENTOS_REINICIO){
                reinicio = 0;
                /*Se usa en este caso, la memoria a largo plazo*/
                solucionPorFrecuencias();
                coste_actual = costeSolucion();
            
            
                /*Reiniciamos la MCP y la MLP.*/
                lista_tabu.clear();
                for(int i = 0; i < TENENCIA_TABU; i++)
                    lista_tabu.add(-1);
                mem_largo_plazo.clear();
                for(int i = 0; i < mem_largo_plazo.size(); i++)
                    mem_largo_plazo.set(i,0);

                /*¿La nueva solución actual supera a la mejor encontrada?*/
                if(costeTotal < coste_actual){
                    costeTotal = coste_actual;

                    //TODO: NO HACE FALTA LIMPIAR ENTERO, SOLO HE CAMBIADO 1 ELEMENTO !!!!!
                    M.clear();
                    M.addAll(solucion_actual); //Añado todos los elementos de la solución Actual en el HashSet de mi solución.
                    //v_M = solucion_actual;
                    comprobados.clear();
                }
            }
            
            ++it;
        }
    }
    
    /*---------------- MÉTODOS PRIVADOS ---------------*/
    
    private int puntoMenorAporte(HashSet<Integer> comprobados) {
        double dist_punto;
        int pos = -1;
        double menorCoste = distanciasElemento(solucion_actual.get(0));
        for(int i = 1; i < solucion_actual.size(); i++){
            dist_punto = distanciasElemento(solucion_actual.get(i));
            if(menorCoste > dist_punto && !comprobados.contains(solucion_actual.get(i))){
                menorCoste = dist_punto;
                pos = i;
            }
        }
        if (pos != -1){
            comprobados.add(solucion_actual.get(pos));
            return solucion_actual.get(pos);
        }
        return solucion_actual.get(0);
    }
    
    private void intercambiar(Integer seleccionado, Integer j, ArrayList<Integer> solucion_temp){
        solucion_temp.remove(seleccionado);
        solucion_temp.add(j);
    }
    
    private double factorizacion(int seleccionado,int j, ArrayList<Integer> v_M ){
        double costeMenor = 0, costeMayor =0;
        for (int k=0; k < M.size(); k++){
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
        /*Primero ordeno el vector de la memoria a largo plazo.*/
        mem_largo_plazo.sort((o1, o2) -> o1.compareTo(o2));
        
        Random random = new Random();
        random.Set_random(getConfig().getSemillas().get(semilla));
        random.Randint(0, 100);
        
        solucion_actual.clear();
        /*En caso de dar el primer 50% no inclusive, se intensificará.*/
        if(random.Randfloat(0, 100) < PORCENTAJE_REINICIO_MLP)
            for(int i = 0; i < num_candidatos; i++)
                solucion_actual.add(mem_largo_plazo.get(i));
        /*En caso de superar el 50% inclusive, se diversificará.*/
        else
            for(int i = num_elementos-1; i >= num_elementos-num_candidatos; i--)
                solucion_actual.add(mem_largo_plazo.get(i));
        
            
    } 
    
    /**
     * @brief Generar vecino válido.
     * @post A partir de un aleatorio, se genera un elemento válido a añadir como vecino.
     * Esto incluye que dicho vecino no se encuentre en la lista tabú de la memoria a
     * corto plazo.
     * @return Integer que indica el nuevo vecino generado.
     */
    private int generaVecino(HashSet<Integer> vecinos){
        for(Integer i : n)
            if(!vecinos.contains(i) && !lista_tabu.contains(i)){
                vecinos.add(i);
                return i;  
            }
        return -1;
    }
}
