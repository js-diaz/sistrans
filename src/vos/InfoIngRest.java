package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Información de un ingerediente que depende del restaurante que lo usa. Representación Detail.
 * @author JuanSebastian
 */

public class InfoIngRest {
	
	/**
	 * Precio de adicionar este ingrediente a algún plato.
	 */
	@JsonProperty(value = "precioAdicion")
	private Double precioAdicion;
	
	/**
	 * Precio de sustituir algún ingrediente por este en algún plato.
	 */
	@JsonProperty(value="precioSustituto")
	private Double precioSustituto;
	
	/**
	 * Ingrediente que se está describiendo
	 */
	@JsonProperty(value="ingrediente")
	private Ingrediente ingrediente;
	
	/**
	 * Restaurante que contiene el ingrediente
	 */
	@JsonProperty(value="restaurante")
	private RestauranteMinimum restaurante;

	public InfoIngRest(@JsonProperty(value="precioAdicion") Double precioAdicion, @JsonProperty(value="precioSustituto") Double precioSustituto,
			@JsonProperty(value="ingrediente") Ingrediente ingrediente, @JsonProperty(value="restaurante") RestauranteMinimum restaurante) {
		this.precioAdicion = precioAdicion;
		this.precioSustituto = precioSustituto;
		this.ingrediente = ingrediente;
		this.restaurante = restaurante;
	}

	public Double getPrecioAdicion() {
		return precioAdicion;
	}

	public void setPrecioAdicion(Double precioAdicion) {
		this.precioAdicion = precioAdicion;
	}

	public Double getPrecioSustituto() {
		return precioSustituto;
	}

	public void setPrecioSustituto(Double precioSustituto) {
		this.precioSustituto = precioSustituto;
	}
	
	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}

	public RestauranteMinimum getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteMinimum restaurante) {
		this.restaurante = restaurante;
	}
	
	

}
