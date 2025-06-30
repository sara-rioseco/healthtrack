package com.healthtrack.unit;

import com.healthtrack.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Usuario - Unit Tests")
class UsuarioTest {

    private Usuario usuario;
    private static final String NOMBRE_USUARIO = "Juan Pérez";
    private static final double PESO_INICIAL = 75.5;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(NOMBRE_USUARIO, PESO_INICIAL);
    }

    @Test
    @DisplayName("Constructor debería crear usuario con nombre y peso correctos")
    void testConstructor_ConParametrosValidos() {
        // Given & When - usuario creado en setUp

        // Then
        assertThat(usuario.getNombre()).isEqualTo(NOMBRE_USUARIO);
        assertThat(usuario.getPeso()).isEqualTo(PESO_INICIAL);
    }

    @Test
    @DisplayName("Actualizar peso debería asignar el nuevo peso correctamente")
    void testActualizarPeso_DeberiaAsignarNuevoPeso() {
        // Given
        double nuevoPeso = 80.0;

        // When
        usuario.actualizarPeso(nuevoPeso);

        // Then
        assertThat(usuario.getPeso()).isEqualTo(nuevoPeso);
    }

    @ParameterizedTest
    @ValueSource(doubles = {50.0, 75.5, 100.3, 120.0})
    @DisplayName("Actualizar peso debería funcionar con diferentes pesos válidos")
    void testActualizarPeso_ConPesosValidos(double nuevoPeso) {
        // When
        usuario.actualizarPeso(nuevoPeso);

        // Then
        assertThat(usuario.getPeso()).isEqualTo(nuevoPeso);
    }

    @Test
    @DisplayName("Actualizar peso con valor decimal debería mantener precisión")
    void testActualizarPeso_ConPesoDecimal() {
        // Given
        double pesoDecimal = 73.75;

        // When
        usuario.actualizarPeso(pesoDecimal);

        // Then
        assertThat(usuario.getPeso()).isEqualTo(pesoDecimal);
    }

    @Test
    @DisplayName("Actualizar peso a cero debería ser válido")
    void testActualizarPeso_ConPesoCero() {
        // Given
        double pesoCero = 0.0;

        // When & Then
        assertThatThrownBy(() -> usuario.actualizarPeso(pesoCero))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El peso no puede ser menor a");
    }

    @Test
    @DisplayName("getNombre debería retornar el nombre correcto")
    void testGetNombre_DeberiaRetornarNombreCorrecto() {
        // When
        String nombre = usuario.getNombre();

        // Then
        assertThat(nombre).isEqualTo(NOMBRE_USUARIO);
    }

    @Test
    @DisplayName("getPeso debería retornar el peso actual")
    void testGetPeso_DeberiaRetornarPesoActual() {
        // When
        double peso = usuario.getPeso();

        // Then
        assertThat(peso).isEqualTo(PESO_INICIAL);
    }

    @Test
    @DisplayName("Actualizar peso múltiples veces debería mantener último valor")
    void testActualizarPeso_MultipleVeces() {
        // Given
        double peso1 = 80.0;
        double peso2 = 82.5;
        double peso3 = 78.0;

        // When
        usuario.actualizarPeso(peso1);
        usuario.actualizarPeso(peso2);
        usuario.actualizarPeso(peso3);

        // Then
        assertThat(usuario.getPeso()).isEqualTo(peso3);
    }
}
