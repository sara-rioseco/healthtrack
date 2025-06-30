package com.healthtrack.functional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.*;

public class UsuarioRegistroTest extends BaseSeleniumTest {

    @Test(description = "Registro completo de usuario")
    public void testRegistroUsuarioCompleto() {
        // Given
        driver.get(BASE_URL + "/registro");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // When
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoEmail = driver.findElement(By.id("email"));
        WebElement campoPesoInicial = driver.findElement(By.id("peso-inicial"));
        WebElement botonRegistrar = driver.findElement(By.id("registrar"));

        String nombre = "Juan Pérez";
        String email = "juan.perez@example.com";
        String pesoInicial = "75.0";

        campoNombre.sendKeys(nombre);
        campoEmail.sendKeys(email);
        campoPesoInicial.sendKeys(pesoInicial);
        botonRegistrar.click();

        // Then
        WebElement mensajeExito = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("registro-exitoso"))
        );

        assertTrue(mensajeExito.isDisplayed());
        assertTrue(mensajeExito.getText().contains("Usuario registrado exitosamente"));

        // Verificar redirección al dashboard
        assertTrue(driver.getCurrentUrl().contains("/dashboard"));

        // Verificar que los datos del usuario se muestran correctamente
        WebElement nombreMostrado = driver.findElement(By.id("nombre-usuario"));
        WebElement pesoMostrado = driver.findElement(By.id("peso-actual"));

        assertEquals(nombreMostrado.getText(), nombre);
        assertTrue(pesoMostrado.getText().contains(pesoInicial));
    }

    @Test(description = "Registro con datos inválidos")
    public void testRegistroConDatosInvalidos() {
        // Given
        driver.get(BASE_URL + "/registro");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // When - Intentar registro con email inválido
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoEmail = driver.findElement(By.id("email"));
        WebElement campoPesoInicial = driver.findElement(By.id("peso-inicial"));
        WebElement botonRegistrar = driver.findElement(By.id("registrar"));

        campoNombre.sendKeys("Test User");
        campoEmail.sendKeys("email-invalido");
        campoPesoInicial.sendKeys("75.0");
        botonRegistrar.click();

        // Then
        WebElement mensajeError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("error-email"))
        );

        assertTrue(mensajeError.isDisplayed());
        assertTrue(mensajeError.getText().contains("Email inválido"));
    }

    @Test(description = "Registro con peso inválido")
    public void testRegistroConPesoInvalido() {
        // Given
        driver.get(BASE_URL + "/registro");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // When
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoEmail = driver.findElement(By.id("email"));
        WebElement campoPesoInicial = driver.findElement(By.id("peso-inicial"));
        WebElement botonRegistrar = driver.findElement(By.id("registrar"));

        campoNombre.sendKeys("Test User");
        campoEmail.sendKeys("test@example.com");
        campoPesoInicial.sendKeys("-10"); // Peso inválido
        botonRegistrar.click();

        // Then
        WebElement mensajeError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("error-peso"))
        );

        assertTrue(mensajeError.isDisplayed());
        assertTrue(mensajeError.getText().contains("peso no puede ser menor"));
    }
}