package com.healthtrack.model;

public class Usuario {
    private final String nombre;
    private double peso;

    public Usuario(String nombre, double peso) {
        this.nombre = nombre;
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    // MÉTODO CORREGIDO - Ahora asigna correctamente el nuevo peso
    public void actualizarPeso(double nuevoPeso) {
        PesoValidator.validarPeso(nuevoPeso);
        this.peso = nuevoPeso;
    }

    public void mostrarInformacion() {
        System.out.println("Usuario: " + nombre + ", Peso Actual: " + peso + " kg");
    }
}
