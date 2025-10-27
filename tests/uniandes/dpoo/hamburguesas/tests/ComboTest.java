package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.util.ArrayList;

public class ComboTest {
	private Combo combo;
	
	@BeforeEach
	void setUp() throws Exception {
		ProductoMenu pm1 = new ProductoMenu("todoterreno", 25000);
		ProductoMenu pm2 = new ProductoMenu("papas grandes", 6900);
		ProductoMenu pm3 = new ProductoMenu("gaseosa", 5000);
		ArrayList<ProductoMenu> productos = new ArrayList<>();
		productos.add(pm1);
		productos.add(pm2);
		productos.add(pm3);
		combo = new Combo("combo todoterreno", 0.07, productos);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		combo = null;
	}
	
	@Test
	void geNombre() {
		assertEquals("combo todoterreno", combo.getNombre(), "El nombre del combo no fue el esperado.");
	}
	
	@Test
	void getDescuento() {
		assertEquals(0.07, combo.getDescuento(), "El descuento del combo no fue el esperado.");
	}
	
	@Test
	void getPrecio() {
		assertEquals((25000 + 6900 + 5000) * (1 - 0.07), combo.getPrecio(), "El precio del combo no fue el esperado.");
	}
	
	@Test
	void getTextoFactura() {
		StringBuffer sb = new StringBuffer();
		sb.append("Combo " + combo.getNombre() + "\n");
		sb.append("Descuento: " + combo.getDescuento() + "\n");
		sb.append("            " + combo.getPrecio() + "\n");
		assertEquals(sb.toString(), combo.generarTextoFactura(), "La factura generada no fue la esperada.");
	}
}
