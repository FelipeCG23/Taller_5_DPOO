package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class PedidoTest {
	private Pedido pedido;
	
	@BeforeEach
	void setUp() throws Exception {
		pedido = new Pedido("Felipe Correa", "Carrera 4 #77-32");
	}
	
	@AfterEach
	void tearDown() throws Exception {
		pedido = null;
	}
	
	@Test
	void pruebaCrearPedido() {
		assertEquals("Felipe Correa", pedido.getNombreCliente(), "El nombre del cliente no fue el esperadso.");
		assertEquals("Carrera 4 #77-32", pedido.getDireccionCliente(), "La dirección del cliente no fue la esperada.");
	}
	
	@Test
	void pruebaAgregarProducto() {
		ProductoMenu pm = new ProductoMenu("todoterreno", 25000);
		assertTrue(pedido.agregarProducto(pm), "El producto no se agregó correctamente al pedido.");
		assertEquals(1, pedido.getProductos().size(), "El número de productos en el pedido no fue el esperado.");
		assertEquals(pm, pedido.getProductos().get(0), "El producto agregado no coincide con el esperado.");
	}
	
	@Test
	void precioTotalPedido() {
		ProductoMenu pm1 = new ProductoMenu("todoterreno", 25000);
		ProductoMenu pm2 = new ProductoMenu("corral queso", 16000);
		pedido.agregarProducto(pm1);
		pedido.agregarProducto(pm2);
		assertEquals(25000 + 16000 + (25000 + 16000) * 0.19, pedido.getPrecioTotalPedido(), "El precio total del pedido no fue el esperado.");
	}
	
	@Test
	void generarTextoFactura() {
		ProductoMenu pm1 = new ProductoMenu("todoterreno", 25000);
		ProductoMenu pm2 = new ProductoMenu("corral queso", 16000);
		pedido.agregarProducto(pm1);
		pedido.agregarProducto(pm2);
		StringBuffer sb = new StringBuffer();
		sb.append("Cliente: Felipe Correa\n");
		sb.append("Dirección: Carrera 4 #77-32\n");
		sb.append("----------------\n");
		for (Producto producto : pedido.getProductos()) {
			sb.append(producto.generarTextoFactura());
		}
		sb.append("----------------\n");
		sb.append( "Precio Neto:  " + pedido.getPrecioNetoPedido() + "\n");
        sb.append( "IVA:          " + pedido.getPrecioIVAPedido() + "\n");
        sb.append( "Precio Total: " + pedido.getPrecioTotalPedido() + "\n");
        assertEquals(sb.toString(), pedido.generarTextoFactura(), "La factura generada no fue la esperada.");
	}
	
	@Test
	void pruebaGuardarFactura() {
		ProductoMenu pm1 = new ProductoMenu("todoterreno", 25000);
		ProductoMenu pm2 = new ProductoMenu("corral queso", 16000);
		pedido.agregarProducto(pm1);
		pedido.agregarProducto(pm2);
		File archivoNuevo = new File("Factura_Prueba.txt");
		try {
			pedido.guardarFactura(archivoNuevo);
			assertTrue(archivoNuevo.exists(), "El archvio de la factura no fue creado correctamente.");
			assertEquals(Files.readString(Path.of(archivoNuevo.getAbsolutePath())), pedido.generarTextoFactura(), "El contenido del archivo de la factura no fue el esperado.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			archivoNuevo.delete();
		}
	}
}
