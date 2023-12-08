/*
 */
package com.mycompany.practica5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Jonathan
 */
public class KNN {

    static class Data {

        double[] attributes;
        int clase;

        public Data(double[] attributes, int clase) {
            this.attributes = attributes;
            this.clase = clase;
        }
    }

    static class Distancia {

        int index;
        double distancia;

        public Distancia(int index, double distancia) {
            this.index = index;
            this.distancia = distancia;
        }
    }

    public static List<Data> leerDatos(String nombreArchivo) {
        List<Data> datos = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                double[] attributes = new double[parts.length - 1];
                for (int i = 0; i < parts.length - 1; i++) {
                    attributes[i] = Double.parseDouble(parts[i]);
                }
                int clase = Integer.parseInt(parts[parts.length - 1]);
                datos.add(new Data(attributes, clase));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public static double euclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public static double manhattanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = Math.abs(a[i] - b[i]);
            sum += diff;
        }
        return sum;
    }

    public static void main(String[] args) {
        int TPTot = 0;
        int TNTot = 0;
        int FPTot = 0;
        int FNTot = 0;
        int[][] adyacencia = new int[5][5];
        float[][] cant = new float[5][4];
        Scanner scanner = new Scanner(System.in);
        //System.out.print("Ingrese el nombre del archivo de entrenamiento: ");
        //String entrenamientoArchivo = scanner.nextLine();
        List<Data> entrenamiento = leerDatos("src\\dataSetCora.csv");

        //System.out.print("Ingrese el nombre del archivo de prueba: ");
        //String pruebaArchivo = scanner.nextLine();
        List<Data> prueba = leerDatos("src\\dataPruebaCora.csv");

        while (true) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    adyacencia[i][j] = 0;
                    if (j < 3) {
                        cant[i][j] = 0;
                    }
                }
            }
            int TP = 0;
            int TN = 0;
            int FP = 0;
            int FN = 0;
            float accuracy = 0;
            float precision = 0;
            float recall = 0;
            float fscore = 0;
            System.out.print("Tipo de distancia (1 para euclidiana, 2 para manhattan): ");
            int opcion = scanner.nextInt();
            if (opcion != 1 && opcion != 2) {
                System.out.println("Opción inválida");
                continue;
            }
            System.out.println("Cantidad de vecinos cercanos: ");
            int vecinos = scanner.nextInt();

            //System.out.println("x1      y1      y2      z2      PAV      X1      Y1      Y2      Z2      PAV      DISTANCIA");
            for (int i = 0; i < prueba.size(); i++) {
                int contC0 = 0;
                int contC1 = 0;
                int contC2 = 0;
                int contC3 = 0;
                //int contC4 = 0;
                Data datoPrueba = prueba.get(i);
                List<Distancia> distancias = new ArrayList<>();
                for (int j = 0; j < entrenamiento.size(); j++) {
                    Data datoEntrenamiento = entrenamiento.get(j);
                    double distancia = opcion == 1
                            ? euclideanDistance(datoPrueba.attributes, datoEntrenamiento.attributes)
                            : manhattanDistance(datoPrueba.attributes, datoEntrenamiento.attributes);
                    distancias.add(new Distancia(j, distancia));
                }
                distancias.sort((d1, d2) -> Double.compare(d1.distancia, d2.distancia));
                /*System.out.print("Presione Y para mostrar el siguiente dato o S para salir al menú principal: ");
                char continuar = scanner.next().charAt(0);
                if (continuar == 'S' || continuar == 's') {
                    break;
                }*/
                int count = 0;
                //System.out.println("x1      y1      y2      z2      PAV      X1      Y1      Y2      Z2      PAV      DISTANCIA");
                for (int j = 0; j < distancias.size(); j++) {
                    Distancia dist = distancias.get(j);
                    Data datoEntrenamiento = entrenamiento.get(dist.index);
                    /*System.out.printf("%.4f  %.4f  %.4f  %.4f  %d      %.4f  %.4f  %.4f  %.4f  %d      %.4f\n",
                            datoPrueba.attributes[0], datoPrueba.attributes[1], datoPrueba.attributes[2],
                            datoPrueba.attributes[3], datoPrueba.clase,
                            datoEntrenamiento.attributes[0], datoEntrenamiento.attributes[1],
                            datoEntrenamiento.attributes[2], datoEntrenamiento.attributes[3],
                            datoEntrenamiento.clase, dist.distancia);*/
                    if (count < vecinos) {
                        switch (datoEntrenamiento.clase) {
                            case 0:
                                contC0++;
                                break;
                            case 1:
                                contC1++;
                                break;
                            case 2:
                                contC2++;
                                break;
                            case 3:
                                contC3++;
                                break;
                            default:
                        }
                        count++;
                    }
                }
                //System.out.println(contC0 + "  " + contC1 + "  " + contC2 + "  " + contC3 + "  " + contC4);
                int maxContador = 0;
                int claseMayoritaria = 0;
                if (contC0 > maxContador) {
                    claseMayoritaria = 0;
                    maxContador = contC0;
                }

                if (contC1 > maxContador) {
                    claseMayoritaria = 1;
                    maxContador = contC1;
                }

                if (contC2 > maxContador) {
                    claseMayoritaria = 2;
                    maxContador = contC2;
                }
                if (contC3 > maxContador) {
                    claseMayoritaria = 3;
                    maxContador = contC3;
                }

                for (int j = 0; j < 4; j++) {
                    if (datoPrueba.clase == j && claseMayoritaria == j) {
                        TP++;
                        cant[datoPrueba.clase][0]++;
                        adyacencia[j][j]++;
                    } else if (datoPrueba.clase == j && claseMayoritaria != j) {
                        FN++;
                        cant[datoPrueba.clase][3]++;
                    } else if (datoPrueba.clase != j && claseMayoritaria == j) {
                        FP++;
                        adyacencia[datoPrueba.clase][j]++;
                        cant[datoPrueba.clase][2]++;
                    } else if (datoPrueba.clase != j && claseMayoritaria != j) {
                        TN++;
                        cant[datoPrueba.clase][1]++;
                    }
                }
                /*System.out.println("Predicción: Clase " + claseMayoritaria + " Realidad: Clase " + datoPrueba.clase);
                System.out.println("TP: " + TP + "  TN: " + TN + "  FP: " + FP + "  FN: " + FN);
                System.out.println("-----------------------------------------------------------------------------------------------------------");
                 */
                TPTot = TPTot + TP;
                TNTot = TNTot + TN;
                FNTot = FNTot + FN;
                FPTot = FPTot + FP;
                TP = 0;
                TN = 0;
                FN = 0;
                FP = 0;
            }
            System.out.print("  ");
            for (int i = 0; i < 4; i++) {
                System.out.printf("%5d", i);
            }
            System.out.println();  // Salto de línea después de imprimir los números de columna

            for (int i = 0; i < 4; i++) {
                System.out.print(i + " ");  // Imprimir el número de fila
                for (int j = 0; j < 4; j++) {
                    System.out.printf("%5d", adyacencia[i][j]);
                }
                System.out.println();  // Salto de línea después de cada fila
            }
            System.out.println("");
            System.out.printf("%5s", "Clase");
            System.out.printf("%5s", "TP");
            System.out.printf("%6s", "FP");
            System.out.printf("%7s", "Acc");
            System.out.printf("%7s", "Pre");
            System.out.printf("%7s", "Rec");
            System.out.printf("%7s", "F1");
            System.out.println("");
            float T = 0;
            float N = 0;
            for (int i = 0; i < 4; i++) {
                T = 0;
                N = 0;
                accuracy = (cant[i][0] + cant[i][1]) / (cant[i][0] + cant[i][1] + cant[i][2] + cant[i][3]);
                precision = (cant[i][0]) / (cant[i][0] + cant[i][2]);
                recall = (cant[i][0]) / (cant[i][0] + cant[i][3]);
                fscore = (2 * precision * recall) / (precision + recall);
                T = (cant[i][0]) / (cant[i][0] + cant[i][3]);
                N = (cant[i][2]) / (cant[i][2] + cant[i][1]);
                System.out.printf("%5d %5.2f %5.2f %5.2f %5.2f %5.2f %5.2f", i, T, N, accuracy, precision, recall, fscore);
                System.out.println("");
            }
            System.out.println("TP Totales: " + TPTot + "  TN Totales: " + TNTot + "  FP Totales: " + FPTot + "  FN Totales: " + FNTot);
            TPTot = 0;
            TNTot = 0;
            FPTot = 0;
            FNTot = 0;
            System.out.print("Presione Y para seleccionar una distancia o S para salir: ");
            char siguiente = scanner.next().charAt(0);
            if (siguiente == 'S' || siguiente == 's') {
                break;
            }
        }
        scanner.close();
    }
}
