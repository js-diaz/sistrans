package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Informaci�n de un producto que depende del restaurante que lo vende. Representaci�n Detail.
 * @author JuanSebastian
 */
public class InfoProdRest {
	
	/**
	 * Costo de adquirir el producto para este restaurante.
	 */
	@JsonProperty(value="costo")
	private Double costo;
	
	/**
	 * Precio de venta del producto para este restaurante.
	 */
	@JsonProperty(value="precio")
	private Double precio;
	
	/**
	 * Disponibilidad del producto para este restaurante.
	 */
	 @JsonProperty(value="disponibilidad")
	private Integer disponibilidad;
	 	
	/**
	 * Fecha en que se empieza a servir el producto.
	 */
	@JsonProperty(value="fechaInicio") 
	private Date fechaInicio;
		
	/**
	 * Fecha en que se acaba de servir el producto.
	 */
	@JsonProperty(value="fechaFin") 
	private Date fechaFin;
	
	/**
	 * Producto que se est� describiendo.
=======
	 * Producto que se est� describiendo.
>>>>>>> 9e38f927db53a3dc18ba1f5b52b61a0e5825d595
	 */
	@JsonProperty(value="producto")
	private Producto producto;
	
	/**
	 * Restaurante que contiene el producto.
	 */
	@JsonProperty(value="restaurante")
	private RestauranteMinimum restaurante;
	/**
	 * Cantidad máxima del producto
	 */
	@JsonProperty("cantidadMaxima")
	private int cantidadMaxima;

	public InfoProdRest(@JsonProperty(value="costo") Double costo, @JsonProperty(value="precio") Double precio,
			@JsonProperty(value="disponibilidad") Integer disponibilidad, 
			@JsonProperty(value="fechaInicio") Date fechaInicio, @JsonProperty(value="fechaFin") Date fechaFin,
			@JsonProperty(value="producto") Producto producto,
			@JsonProperty(value="restaurante") RestauranteMinimum restaurante,@JsonProperty(value="cantidadMaxima")int cantidadMaxima) {
		this.costo = costo;
		this.precio = precio;
		this.disponibilidad = disponibilidad;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.producto = producto;
		this.restaurante = restaurante;
		this.cantidadMaxima = cantidadMaxima;
	}

	public Double getCosto() {
		return costo;
	}



	public void setCosto(Double costo) {
		this.costo = costo;
	}



	public Double getPrecio() {
		return precio;
	}



	public void setPrecio(Double precio) {
		this.precio = precio;
	}



	public Integer getDisponibilidad() {
		return disponibilidad;
	}



	public void setDisponibilidad(Integer disponibilidad) {
		this.disponibilidad = disponibilidad;
	}



	public Date getFechaInicio() {
		return fechaInicio;
	}



	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}



	public Date getFechaFin() {
		return fechaFin;
	}



	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}



	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public RestauranteMinimum getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteMinimum restaurante) {
		this.restaurante = restaurante;
	}
	public int getCantidadMaxima() {
		return cantidadMaxima;
	}

	public void setCantidadMaxima(int cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}	
	
}
