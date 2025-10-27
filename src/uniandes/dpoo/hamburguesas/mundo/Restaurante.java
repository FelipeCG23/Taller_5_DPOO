package uniandes.dpoo.hamburguesas.mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;

/**
 * La clase que administra toda la información del restaurante de hamburguesas
 */
public class Restaurante
{
    /**
     * La ruta a la carpeta donde se almacenan las facturas
     */
    private static final String CARPETA_FACTURAS = "./facturas/";

    /**
     * La primera parte del nombre de los archivos de facturas
     */
    private static final String PREFIJO_FACTURAS = "factura_";

    /**
     * La lista de pedidos que ha recibido el restaurante y que ya se han cerrado
     */
    private ArrayList<Pedido> pedidos;

    /**
     * La lista de ingredientes disponibles para las hamburguesas
     */
    private ArrayList<Ingrediente> ingredientes;

    /**
     * La lista de Items que conforman el menú de productos básicos del restaurante
     */
    private ArrayList<ProductoMenu> menuBase;

    /**
     * La lista de Combos que ofrece el restaurante
     */
    private ArrayList<Combo> menuCombos;

    /**
     * El pedido en curso. Cuando no hay un pedido en cuerso, este atributo será null.
     */
    private Pedido pedidoEnCurso;

    // ***************************************
    // CONSTRUCTOR
    // ***************************************
    /**
     * Crea un restaurante vacío, sin pedidos, ingredientes, ni ítems en el menú
     */
    public Restaurante() {
        pedidos = new ArrayList<Pedido>();
        ingredientes = new ArrayList<Ingrediente>();
        menuBase = new ArrayList<ProductoMenu>();
        menuCombos = new ArrayList<Combo>();
    }

    // ***************************************
    // Métodos
    // ***************************************
    /**
     * Inicia un nuevo pedido.
     * 
     * @param nombreCliente El nombre del cliente que está iniciando el pedido
     * @param direccionCliente La dirección del cliente
     * @throws YaHayUnPedidoEnCursoException Se lanza esta excepción si ya había un pedido en curso cuando se llamó a este método
     */
    public void iniciarPedido( String nombreCliente, String direccionCliente ) throws YaHayUnPedidoEnCursoException {
    	if (pedidoEnCurso != null) {
			throw new YaHayUnPedidoEnCursoException(pedidoEnCurso.getNombreCliente(), nombreCliente);
		}

		pedidoEnCurso = new Pedido(nombreCliente, direccionCliente);
    }

    /**
     * Este método cierra un pedido, creando la factura y guardándolo en el histórico de pedidos.
     * 
     * La factura queda guardada en un archivo cuyo nombre debe ser PREFIJO_FACTURAS, seguido del identificador del pedido, y debe tener extensión 'txt'. El archivo debe
     * quedar en la carpeta CARPETA_FACTURAS.
     * 
     * @throws NoHayPedidoEnCursoException Lanza esta excepción si no hay un pedido en curso
     * @throws FileNotFoundException Lanza esta excepción si hay problemas guardando el archivo
     */
    public void cerrarYGuardarPedido() throws NoHayPedidoEnCursoException, IOException {
		if (pedidoEnCurso == null) {
			throw new NoHayPedidoEnCursoException();
		}
		
		String nombreArchivo = PREFIJO_FACTURAS + pedidoEnCurso.getIdPedido() + ".txt";
		pedidoEnCurso.guardarFactura(new File(CARPETA_FACTURAS + nombreArchivo));
		pedidoEnCurso = null;
    }

    /**
     * Retorna el pedido actual en curso. Si no hay un pedido en curso, retorna null.
     * 
     * @return El pedido en curso o null
     */
    public Pedido getPedidoEnCurso() {
        return pedidoEnCurso;
    }

    /**
     * Retorna la lista completa de pedidos cerrados en el restaurante.
     * 
     * @return La lista de pedidos cerrados
     */
    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    /**
     * Retorna el menú de hamburguesas básicas del restaurante
     * 
     * @return
     */
    public ArrayList<ProductoMenu> getMenuBase() {
        return menuBase;
    }

    /**
     * Retorna el menú de los combos del restaurante
     * 
     * @return
     */
    public ArrayList<Combo> getMenuCombos() {
        return menuCombos;
    }

    /**
     * Retorna la lista completa de ingredientes del restaurante
     * 
     * @return
     */
    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    /**
     * Carga la información de los ingredientes, los productos y los combos del restaurante
     * 
     * @param archivoIngredientes El archivo que tiene la información de los ingredientes
     * @param archivoMenu El archivo que tiene la información de los productos base
     * @param archivoCombos El archivo que tiene la información de los combos
     * @throws IOException
     * @throws NumberFormatException
     */
    public void cargarInformacionRestaurante(File archivoIngredientes, File archivoMenu, File archivoCombos)
    		throws HamburguesaException, NumberFormatException, IOException {
        cargarIngredientes(archivoIngredientes);
        cargarMenu(archivoMenu);
        cargarCombos(archivoCombos);
    }

    private void cargarIngredientes( File archivoIngredientes ) throws IngredienteRepetidoException, IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(archivoIngredientes))) {
            String linea = reader.readLine();
            while(linea != null) {
                if (!linea.isEmpty()) {
                    String[] ingredientesStr = linea.split(";");
                    String nombreIngrediente = ingredientesStr[0];
                    int costoIngrediente = Integer.parseInt(ingredientesStr[1]);

                    // Check for duplicate ingredient
					if (ingredientes.stream().anyMatch(i -> i.getNombre().equals(nombreIngrediente))) {
						throw new IngredienteRepetidoException(nombreIngrediente);
					}

					Ingrediente ingrediente = new Ingrediente(nombreIngrediente, costoIngrediente);
					ingredientes.add(ingrediente);
				}
			}
		}
	}

    private void cargarMenu(File archivoMenu) throws ProductoRepetidoException, IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(archivoMenu))) {
            String linea = reader.readLine();
            while(linea != null) {
                if (!linea.isEmpty()) {
                	String[] productoStr = linea.split(";");
					String nombreProducto = productoStr[0];
					int costoProducto = Integer.parseInt(productoStr[1]);

					if (menuBase.stream().anyMatch(prod -> prod.getNombre().equals(nombreProducto))) {
						throw new ProductoRepetidoException(nombreProducto);
					}

					ProductoMenu producto = new ProductoMenu(nombreProducto, costoProducto);
					menuBase.add(producto);
				}
			}
		}
	}

    private void cargarCombos(File archivoCombos) throws ProductoRepetidoException, ProductoFaltanteException, IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(archivoCombos))) {
            String linea = reader.readLine( );
            while( linea != null ) {
                if(!linea.isEmpty()) {
                	String[] comboStr = linea.split(";");

					if (comboStr.length < 2) {
						throw new IOException("Formato incorrecto en archivo de combos: " + linea);
					}

					String nombreCombo = comboStr[0].trim();

					double descuento = Double.parseDouble(comboStr[1].replace("%", "").trim()) / 100;

					if (menuCombos.stream().anyMatch(c -> c.getNombre().equals(nombreCombo))) {
						throw new ProductoRepetidoException(nombreCombo);
					}

					ArrayList<ProductoMenu> itemsCombo = new ArrayList<>();
					for (int i = 2; i < comboStr.length; i++) {
						ProductoMenu productoItem = buscarProductoEnMenu(comboStr[i].trim());

						if (productoItem == null) {
							throw new ProductoFaltanteException(comboStr[i].trim());
						}
						itemsCombo.add(productoItem);
					}

					menuCombos.add(new Combo(nombreCombo, descuento, itemsCombo));
				}
			}
		}
	}

    private ProductoMenu buscarProductoEnMenu(String nombreProducto) {
		for (ProductoMenu prod : menuBase) {
			if (prod.getNombre().equals(nombreProducto)) {
				return prod;
			}
		}
		return null;
	}

}
