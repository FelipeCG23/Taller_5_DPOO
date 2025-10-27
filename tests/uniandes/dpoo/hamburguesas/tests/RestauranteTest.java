package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.*;
import uniandes.dpoo.hamburguesas.excepciones.*;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

public class RestauranteTest {
	private Restaurante restaurante;
	
	@TempDir
	java.nio.file.Path tempDir;
	
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
	void pruebaIniciarPedidoYaHayPedido() throws YaHayUnPedidoEnCursoException {
		restaurante.iniciarPedido("Felipe Correa", "Carrera 4 #77-32");
		assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
			restaurante.cerrarYGuardarPedido();
		});
	}
	
	@Test
	void pruebaCerrarYGuardarPedido() throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException {
		restaurante.iniciarPedido("Felipe Correa", "Carrera 4 #77-32");
		restaurante.getPedidoEnCurso().agregarProducto(new ProductoAjustado(new ProductoMenu("todoterreno", 25000)));
		restaurante.cerrarYGuardarPedido();
		assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso no se cerró correctamente.");
		assertTrue(restaurante.getPedidos().isEmpty());
	}
}
