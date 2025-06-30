package com.healthtrack.controller;

import com.healthtrack.model.Usuario;
import com.healthtrack.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ====== ENDPOINTS WEB (para las páginas HTML) ======

    @GetMapping("/")
    public String home() {
        return "redirect:/usuario/dashboard";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro"; // Retorna la vista registro.html
    }

    @GetMapping("/usuario/dashboard")
    public String mostrarDashboard(@RequestParam(required = false) String nombre, Model model) {
        if (nombre != null && usuarioService.existeUsuario(nombre)) {
            Usuario usuario = usuarioService.obtenerUsuario(nombre);
            model.addAttribute("usuario", usuario);
        }
        return "dashboard"; // Retorna la vista dashboard.html
    }

    @GetMapping("/perfil")
    public String mostrarPerfil() {
        return "perfil"; // Retorna la vista perfil.html
    }

    // ====== REST API ENDPOINTS ======

    @PostMapping("/api/usuarios/registrar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam double pesoInicial) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El nombre es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (email == null || !email.contains("@")) {
                response.put("success", false);
                response.put("message", "Email inválido");
                return ResponseEntity.badRequest().body(response);
            }

            if (usuarioService.existeUsuario(nombre)) {
                response.put("success", false);
                response.put("message", "El usuario ya existe");
                return ResponseEntity.badRequest().body(response);
            }

            // Registrar usuario
            usuarioService.registrarUsuario(nombre, pesoInicial);

            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("redirectUrl", "/usuario/dashboard?nombre=" + nombre);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/api/usuarios/actualizar-peso")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarPeso(
            @RequestParam String nombre,
            @RequestParam double peso) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (!usuarioService.existeUsuario(nombre)) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado: " + nombre);
                return ResponseEntity.badRequest().body(response);
            }

            usuarioService.actualizarPesoUsuario(nombre, peso);
            Usuario usuario = usuarioService.obtenerUsuario(nombre);

            response.put("success", true);
            response.put("message", "Peso actualizado correctamente");
            response.put("pesoActual", usuario.getPeso());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar el peso");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/api/usuarios/{nombre}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerUsuario(@PathVariable String nombre) {
        Map<String, Object> response = new HashMap<>();

        if (!usuarioService.existeUsuario(nombre)) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioService.obtenerUsuario(nombre);
        response.put("success", true);
        response.put("nombre", usuario.getNombre());
        response.put("peso", usuario.getPeso());

        return ResponseEntity.ok(response);
    }

    // ====== HEALTH CHECK ENDPOINT ======

    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.Instant.now().toString());
        health.put("application", "HealthTrack Platform");
        return ResponseEntity.ok(health);
    }
}