package rfc;
/**
 * Clase que modela un producto de RotondAndes
 * @author jc161
 *
 */

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoEnTotal {
	/**
	 * Precio del producto
	 */
	@JsonProperty(value="precio")
	private double precio;
	/**
	 * Costo total de producción.
	 */
	@JsonProperty(value="costo")
	private double costoProduccion;
	/**
	 * Identificación del producto
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Cantidad del producto
	 */
	@JsonProperty(value="cantidad")
	private int cantidad;
	/**
	 * Construye un nuevo producto con los paráemtros dados.<br>
	 * @param prcio Precio<br>
	 * @param cantidad Cantidad del producto.<br>
	 * @param costoProduccion<br>
	 * @param id <br>
	 */
	public ProductoEnTotal(@JsonProperty(value="id") Long id,@JsonProperty(value="cantidad") int cantidad,@JsonProperty("precio") double prcio,
			@JsonProperty(value="costo") double costoProduccion) {
		super();
		this.precio = prcio;
		this.costoProduccion = costoProduccion;
		this.id = id;
		this.cantidad=cantidad;
	}
	
	/**
	 * Obtiene el precio del producto.<br>
	 * @return precio
	 */
	public double getPrecio() {
		return precio;
	}
	/**
	 * Modifica el precio del producto al dado por parámetro.<br>
	 * @param precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	/**
	 * Obtiene el costo de producción.<br>
	 * @return costoProduccion
	 */
	public double getCostoProduccion() {
		return costoProduccion;
	}
	/**
	 * Modifica el costo de producción al dado por parámetro.<br>
	 * @param costoProduccion
	 */
	public void setCostoProduccion(double costoProduccion) {
		this.costoProduccion = costoProduccion;
	}
	/**
	 * Obtiene el id.<br>
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Modifica el id al dado por parámetro.<br>
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Obtiene la cantidad dada.<br>
	 * @return cantidad
	 */
	public int getCantidad()
	{
		return cantidad;
	}
	/**
	 * Modifica la cantidad por el valor dado por parámetro.<br>
	 * @param cant
	 */
	public void setCantidad(int cant)
	{
		cantidad=cant;
	}
	
	
}
