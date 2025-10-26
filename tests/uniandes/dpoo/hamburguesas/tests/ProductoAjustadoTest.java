package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class ProductoAjustadoTest {
	private ProductoMenu pm;
	private ProductoAjustado pa;
	
	@BeforeEach
	void setUp() throws Exception {
		pm = new ProductoMenu("corral queso", 16000);
		pa = new ProductoAjustado(pm);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		pm = null;
		pa = null;
	}
	
	@Test
	void getNombre() {
		assertEquals("corral queso", pa.getNombre(), "El nombre del producto ajustado no fue el esperado."); 
	}
	
	@Test
	void agregarIngrediente() {
		boolean nuevoIngrediente = pa.agregarIngrediente(new Ingrediente("tocineta express", 2500));
		assertTrue(nuevoIngrediente, "El ingrediente no fue el esperado o no se agregó correctamente.");
	}
	
	@Test
	void eliminarIngrediente() {
		pa.agregarIngrediente(new Ingrediente("tocineta express", 2500));
		boolean ingredienteEliminado = pa.eliminarIngrediente("tocineta express");
		assertTrue(ingredienteEliminado, "El ingrediente eliminado no fue el espereado o no se eliminó correctamente.");
	}
}
