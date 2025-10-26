package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class ProductoMenuTest {
	private ProductoMenu pm;
	
	@BeforeEach
	void setUp() throws Exception {
		pm = new ProductoMenu("todoterreno", 25000);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		pm = null;
	}
	
	@Test
	void getNombre() {
		assertEquals("todoterreno", pm.getNombre(), "El nombre del producto no fue el esperado.");
	}
	
	@Test
	void getPrecio() {
		assertEquals(25000, pm.getPrecio(), "El precio no fue el esperado.");
	}
	
	@Test
	void generarTextoFactura() {
		StringBuffer sb = new StringBuffer();
		sb.append("todoterreno" + "\n");
		sb.append("            " + 25000 + "\n");
		assertEquals(sb.toString(), pm.generarTextoFactura(), "La factura generada no fue la esperada.");
	}
}