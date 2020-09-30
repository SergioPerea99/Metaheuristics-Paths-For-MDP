/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr01;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author spdlc
 */
public class ArchivoDatos {
    private String nombre; //Para saber de que fichero es la matriz.
    private float matrizDatos[][];
    
    public ArchivoDatos(String ruta_archivo){     
       
        String linea;
        FileReader f = null;
        
        try{
            f = new FileReader(ruta_archivo);
            BufferedReader b = new BufferedReader(f);
            
            //Leemos la primera línea y nos quedamos con el primer número.
            linea = b.readLine();
            String[] split = linea.split(" ");
            
            Integer tamMatriz = Integer.parseInt(split[0]); //Coge el primer numero leido para el valor de filas y columnas.
            Integer tamVector = Integer.parseInt(split[1]);
            
            matrizDatos = new float[tamMatriz][tamMatriz];
            
            int errores = 0;
            //Una vez tenemos preparada la matriz para rellenar, bucle hasta que no encuentre nada que leer.
            while((linea=b.readLine())!= null){
               String[] rellenoMatriz = linea.split(" ");
               int fila = Integer.parseInt(rellenoMatriz[0]);
               int columna = Integer.parseInt(rellenoMatriz[1]);
               //Por si acaso hay varios espacios vacios entre los datos.
               try{
                    matrizDatos[fila][columna] =  Float.parseFloat(rellenoMatriz[2]);
               }catch(NumberFormatException ex){
                   ++errores;
               }
            }

        }catch(Exception e){
            System.out.println(e);
        }
        
    };

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the matrizDatos
     */
    public float[][] getMatrizDatos() {
        return matrizDatos;
    }
    
    
    
}
