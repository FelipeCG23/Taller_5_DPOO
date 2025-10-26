package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class ProductoMenuTest {
	private ProductoMenu productoMenu;
	
	@BeforeEach
	void setUp() throws Exception {
		productoMenu = new ProductoMenu("todoterreno", 25000);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		productoMenu = null;
	}
	
	@Test
	void getNombre() {
		assertEquals("todoterreno", productoMenu.getNombre(), "El nombre del producto no fue el esperado.");
	}
	
	@Test
	void getPrecio() {
		assertEquals(25000, productoMenu.getPrecio(), "El precio no fue el esperado.");
	}
	
	@Test
	void generarTextoFactura() {
		StringBuffer sb = new StringBuffer();
		sb.append("todoterreno" + "\n");
		sb.append("            " + 25000 + "\n");
		assertEquals(sb.toString(), productoMenu.generarTextoFactura(), "La factura generada no fue la esperada.");
	}
}