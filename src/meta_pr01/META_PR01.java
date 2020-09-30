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
public class META_PR01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configurador config = new Configurador(args[0]);
        ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(0));
        
        //Comprobamos que funciona:
        System.out.println(config.getArchivos());
        
        /*
        for (int i = 0; i < archivo.getMatrizDatos().length; i++){
            for(int j=0; j < archivo.getMatrizDatos().length; j++)
                System.out.print(archivo.getMatrizDatos()[i][j] + " ");
            System.out.println("");
        }*/
    }
    
}
