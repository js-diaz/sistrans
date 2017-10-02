package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un men� ofrecido por alg�n restaurante. Representaci�n Minimum.
 * @author JuanSebastian
 */
public class MenuMinimum {
	
	/**
	 * Nombre del men�
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Precio de venta del men�.
	 */
	@JsonProperty(value="precio")
	private Double precio;
	
	/**
	 * Costo de elaboraci�n del men�.
	 */
	@JsonProperty(value="costo")
	private Double costo;
	
	/**
	 * Restaurante al cual pertenece el men�.
	 */
	@JsonProperty(value="restaurante")
	private RestauranteMinimum restaurante;

	public MenuMinimum(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="precio") Double precio,
			@JsonProperty(value="costo") Double costo, @JsonProperty(value="restaurante") RestauranteMinimum restaurante) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.costo = costo;
		this.restaurante = restaurante;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}
	
	public RestauranteMinimum getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteMinimum restaurante) {
		this.restaurante = restaurante;
	}

	
}
