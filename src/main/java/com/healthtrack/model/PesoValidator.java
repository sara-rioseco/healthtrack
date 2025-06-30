package com.healthtrack.model;

public class PesoValidator {

    private static final double PESO_MINIMO = 1.0;
    private static final double PESO_MAXIMO = 500.0;

    public static void validarPeso(double peso) {
        if (peso < PESO_MINIMO) {
            throw new IllegalArgumentException("El peso no puede ser menor a " + PESO_MINIMO + " kg");
        }
        if (peso > PESO_MAXIMO) {
            throw new IllegalArgumentException("El peso no puede ser mayor a " + PESO_MAXIMO + " kg");
        }
    }

    public static boolean esPesoValido(double peso) {
        try {
            validarPeso(peso);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}