package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.*;
import uniandes.dpoo.hamburguesas.excepciones.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

public class RestauranteTest {
	private Restaurante restaurante;
	
	@TempDir
	File tempDir;
	
	@BeforeEach
	void setUp() throws Exception {
		restaurante = new Restaurante();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		restaurante = null;
	}
	
	@Test
	void pruebaIniciarPedido() throws YaHayUnPedidoEnCursoException {
		restaurante.iniciarPedido("Felipe Correa", "Carrera 4 #77-32");
		assertNotNull(restaurante.getPedidoEnCurso(), "No se inició el pedido correctamente.");
		assertEquals("Felipe Correa", restaurante.getPedidoEnCurso().getNombreCliente(), "El nombre del cliente no fue el esperado.");
	}
	
	@Test
	void testIniciarPedidoConPedidoEnCurso() throws YaHayUnPedidoEnCursoException {
		restaurante.iniciarPedido("Felipe Correa", "Carrera 4 #77-32");
		assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
			restaurante.iniciarPedido("Andrea Ganitsky", "Carrera 3 #77-64");
		});
	}
	
	@Test
	void pruebaCerrarYGuardarPedido() {
		assertThrows(NoHayPedidoEnCursoException.class, () -> {
			restaurante.cerrarYGuardarPedido();
		});
	}
	
	@Test
	void pruebaCerrarYGuardarPedido2() throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException {
		restaurante.iniciarPedido("Felipe Correa", "Carrera 4 #77-32");
		restaurante.getPedidoEnCurso().agregarProducto(new ProductoAjustado(new ProductoMenu("todoterreno", 25000)));
		restaurante.cerrarYGuardarPedido();
		assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso no se cerró correctamente.");
		assertTrue(restaurante.getPedidos().isEmpty());
	}
	
	@Test
	void pruebaCargarMenuRepetido() throws IOException, NoSuchMethodException, SecurityException {
		File archivoMenu = new File(tempDir, "menu.txt");
		try (FileWriter w = new FileWriter(archivoMenu)) {
			w.write("nuggets;14000\nnuggets;14000\n");
		}
		Method cargarMenu = Restaurante.class.getDeclaredMethod("cargarMenu", File.class);
		cargarMenu.setAccessible(true);
		Exception exception = assertThrows(InvocationTargetException.class, () -> {
			cargarMenu.invoke(restaurante, archivoMenu);
		});
		assertTrue(exception.getCause() instanceof ProductoRepetidoException,
				"Lo esperado era ProductoRepetidoException, pero se obtuvo: " + exception.getCause());
	}
	
	@Test
	void pruebaCargarIngredientesRepetidos() throws IOException, NoSuchMethodException, SecurityException, IngredienteRepetidoException {
		File archivoIngredientes = new File(tempDir, "ingredientes.txt");
		try (FileWriter w = new FileWriter(archivoIngredientes)) {
			w.write("arroz;4000\narroz;4000\n");
		}
		Method cargarIngredientes = Restaurante.class.getDeclaredMethod("cargarIngredientes", File.class);
		cargarIngredientes.setAccessible(true);
		Exception exception = assertThrows(InvocationTargetException.class, () -> {
			cargarIngredientes.invoke(restaurante, archivoIngredientes);
		});
		assertTrue(exception.getCause() instanceof IngredienteRepetidoException,
				"Lo esperado era IngredienteRepetidoException, pero se obtuvo: " + exception.getCause());
	}
	
	@Test
	void testCargarCombosConProductoFaltante() throws IOException, NoSuchMethodException, SecurityException, ProductoRepetidoException {
		File archivoCombos = new File(tempDir, "combos.txt");
		try (FileWriter w = new FileWriter(archivoCombos)) {
			w.write("combo corral pollo;9%;papas grandes;gaseosa\n");
		}

		Method cargarCombos = Restaurante.class.getDeclaredMethod("cargarCombos", File.class);
		cargarCombos.setAccessible(true);
		assertThrows(InvocationTargetException.class, () -> {
			cargarCombos.invoke(restaurante, archivoCombos);
		});
		restaurante.getMenuCombos();
	}
}
