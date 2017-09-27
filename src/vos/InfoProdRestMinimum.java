package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Información de un producto que depende del restaurante que lo vende. Representación Minimum.
 * @author JuanSebastian
 */
public class InfoProdRestMinimum {

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

	public InfoProdRestMinimum(@JsonProperty(value="costo") Double costo, Double precio, Integer disponibilidad) {
		this.costo = costo;
		this.precio = precio;
		this.disponibilidad = disponibilidad;
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
	
	
	 
	
}
