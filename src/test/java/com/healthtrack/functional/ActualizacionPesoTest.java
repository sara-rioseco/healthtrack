package com.healthtrack.functional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.*;

public class ActualizacionPesoTest extends BaseSeleniumTest {

    @Test(description = "Flujo completo de actualización de peso")
    public void testActualizarPeso_FlujoCorrecto() {
        // Given
        driver.get(BASE_URL + "/usuario/dashboard");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // When - Localizar elementos y actualizar peso
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoPeso = driver.findElement(By.id("peso"));
        WebElement botonActualizar = driver.findElement(By.id("actualizar-peso"));

        // Datos de prueba
        String nombreUsuario = "Test User";
        String pesoNuevo = "75.5";

        // Completar formulario
        campoNombre.clear();
        campoNombre.sendKeys(nombreUsuario);
        campoPeso.clear();
        campoPeso.sendKeys(pesoNuevo);

        // Hacer clic en actualizar
        botonActualizar.click();

        // Then - Verificar que la actualización fue exitosa
        WebElement mensajeExito = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-exito"))
        );

        assertTrue(mensajeExito.isDisplayed());
        assertTrue(mensajeExito.getText().contains("Peso actualizado correctamente"));

        // Verificar que el peso se muestra correctamente
        WebElement pesoMostrado = driver.findElement(By.id("peso-actual"));
        assertEquals(pesoMostrado.getText(), pesoNuevo + " kg");
    }

    @Test(description = "Validar persistencia del peso actualizado")
    public void testActualizarPeso_ValidarPersistencia() {
        // Given
        driver.get(BASE_URL + "/usuario/dashboard");
        String pesoInicial = "70.0";
        String pesoNuevo = "72.5";

        // When - Primera actualización
        actualizarPeso("Usuario Test", pesoInicial);

        // Navegar a otra página y regresar
        driver.get(BASE_URL + "/perfil");
        driver.get(BASE_URL + "/usuario/dashboard");

        // Then - Verificar que el peso inicial persiste
        WebElement pesoActual = driver.findElement(By.id("peso-actual"));
        assertTrue(pesoActual.getText().contains(pesoInicial));

        // When - Segunda actualización
        actualizarPeso("Usuario Test", pesoNuevo);

        // Then - Verificar nueva persistencia
        pesoActual = driver.findElement(By.id("peso-actual"));
        assertTrue(pesoActual.getText().contains(pesoNuevo));
    }

    @Test(description = "Validar visualización correcta del peso")
    public void testActualizarPeso_ValidarVisualizacion() {
        // Given
        driver.get(BASE_URL + "/usuario/dashboard");
        String peso = "68.75";

        // When
        actualizarPeso("Test Visualización", peso);

        // Then - Verificar elementos de visualización
        WebElement pesoMostrado = driver.findElement(By.id("peso-actual"));
        WebElement historialPeso = driver.findElement(By.id("historial-peso"));
        WebElement grafico = driver.findElement(By.id("grafico-peso"));

        // Verificar que todos los elementos muestran el peso correcto
        assertTrue(pesoMostrado.getText().contains(peso));
        assertTrue(historialPeso.getText().contains(peso));
        assertTrue(grafico.isDisplayed());
    }

    @Test(description = "Validar error con peso inválido")
    public void testActualizarPeso_PesoInvalido() {
        // Given
        driver.get(BASE_URL + "/usuario/dashboard");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // When - Intentar actualizar con peso negativo
        WebElement campoPeso = driver.findElement(By.id("peso"));
        WebElement botonActualizar = driver.findElement(By.id("actualizar-peso"));

        campoPeso.clear();
        campoPeso.sendKeys("-5.0");
        botonActualizar.click();

        // Then - Verificar mensaje de error
        WebElement mensajeError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-error"))
        );

        assertTrue(mensajeError.isDisplayed());
        assertTrue(mensajeError.getText().contains("peso no puede ser menor"));
    }

    // Método auxiliar para actualizar peso
    private void actualizarPeso(String nombre, String peso) {
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoPeso = driver.findElement(By.id("peso"));
        WebElement botonActualizar = driver.findElement(By.id("actualizar-peso"));

        campoNombre.clear();
        campoNombre.sendKeys(nombre);
        campoPeso.clear();
        campoPeso.sendKeys(peso);
        botonActualizar.click();

        // Esperar confirmación
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensaje-exito")));
    }
}