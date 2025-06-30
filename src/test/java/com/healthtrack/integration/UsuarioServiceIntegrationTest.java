package com.healthtrack.integration;

import com.healthtrack.model.Usuario;
import com.healthtrack.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UsuarioService - Integration Tests")
class UsuarioServiceIntegrationTest {

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
    }

    @Test
    @DisplayName("Flujo completo: registrar usuario y actualizar peso")
    void testFlujoCompleto_RegistrarYActualizarPeso() {
        // Given
        String nombre = "María García";
        double pesoInicial = 65.0;
        double pesoNuevo = 67.5;

        // When - Registrar usuario
        usuarioService.registrarUsuario(nombre, pesoInicial);

        // Then - Verificar registro
        Usuario usuario = usuarioService.obtenerUsuario(nombre);
        assertThat(usuario).isNotNull();
        assertThat(usuario.getNombre()).isEqualTo(nombre);
        assertThat(usuario.getPeso()).isEqualTo(pesoInicial);

        // When - Actualizar peso
        usuarioService.actualizarPesoUsuario(nombre, pesoNuevo);

        // Then - Verificar actualización
        assertThat(usuario.getPeso()).isEqualTo(pesoNuevo);
    }

    @Test
    @DisplayName("Actualizar peso de usuario inexistente debería lanzar excepción")
    void testActualizarPeso_UsuarioInexistente() {
        // Given
        String nombreInexistente = "Usuario Inexistente";
        double peso = 70.0;

        // When & Then
        assertThatThrownBy(() -> usuarioService.actualizarPesoUsuario(nombreInexistente, peso))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    @DisplayName("existeUsuario debería retornar true para usuario registrado")
    void testExisteUsuario_UsuarioRegistrado() {
        // Given
        String nombre = "Carlos López";
        usuarioService.registrarUsuario(nombre, 80.0);

        // When
        boolean existe = usuarioService.existeUsuario(nombre);

        // Then
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("existeUsuario debería retornar false para usuario no registrado")
    void testExisteUsuario_UsuarioNoRegistrado() {
        // Given
        String nombreInexistente = "Usuario No Registrado";

        // When
        boolean existe = usuarioService.existeUsuario(nombreInexistente);

        // Then
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Múltiples usuarios pueden ser registrados y gestionados independientemente")
    void testMultiplesUsuarios() {
        // Given
        String usuario1 = "Ana";
        String usuario2 = "Luis";
        double peso1 = 60.0;
        double peso2 = 85.0;
        double nuevoPeso1 = 62.0;

        // When
        usuarioService.registrarUsuario(usuario1, peso1);
        usuarioService.registrarUsuario(usuario2, peso2);
        usuarioService.actualizarPesoUsuario(usuario1, nuevoPeso1);

        // Then
        Usuario ana = usuarioService.obtenerUsuario(usuario1);
        Usuario luis = usuarioService.obtenerUsuario(usuario2);

        assertThat(ana.getPeso()).isEqualTo(nuevoPeso1);
        assertThat(luis.getPeso()).isEqualTo(peso2); // No debería haber cambiado
    }

    @Test
    @DisplayName("Validación de peso inválido debería propagarse desde el servicio")
    void testValidacionPesoInvalido_PropagacionExcepcion() {
        // Given
        String nombre = "Pedro";
        usuarioService.registrarUsuario(nombre, 75.0);
        double pesoInvalido = -5.0;

        // When & Then
        assertThatThrownBy(() -> usuarioService.actualizarPesoUsuario(nombre, pesoInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El peso no puede ser menor a");
    }
}