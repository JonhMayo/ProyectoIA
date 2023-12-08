/*
 */
package com.mycompany.practica5;
import java.io.*;
import java.util.*;
/**
 *
 * @author Jonathan
 */
public class CargarDatos {
    public static List<List<Double>> cargarDatos(String nombreArchivo) {
        List<List<Double>> datos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                List<Double> fila = new ArrayList<>();
                for (String val : values) {
                    fila.add(Double.parseDouble(val));
                }
                datos.add(fila);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public static void dividirDatos(List<List<Double>> datos, List<List<Double>> entrenamiento, List<List<Double>> prueba, double porcentajeEntrenamiento) {
        Collections.shuffle(datos);
        int tamanoEntrenamiento = (int) (datos.size() * porcentajeEntrenamiento);
        entrenamiento.addAll(datos.subList(0, tamanoEntrenamiento));
        prueba.addAll(datos.subList(tamanoEntrenamiento, datos.size()));
    }
}
