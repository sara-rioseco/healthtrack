package com.healthtrack.service;

import com.healthtrack.model.Usuario;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UsuarioService {

    private Map<String, Usuario> usuarios = new HashMap<>();

    public void registrarUsuario(String nombre, double peso) {
        Usuario usuario = new Usuario(nombre, peso);
        usuarios.put(nombre, usuario);
    }

    public Usuario obtenerUsuario(String nombre) {
        return usuarios.get(nombre);
    }

    public void actualizarPesoUsuario(String nombre, double nuevoPeso) {
        Usuario usuario = usuarios.get(nombre);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + nombre);
        }
        usuario.actualizarPeso(nuevoPeso);
    }

    public boolean existeUsuario(String nombre) {
        return usuarios.containsKey(nombre);
    }
}