package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Informaci�n de un ingerediente que depende del restaurante que lo usa. Representaci�n Detail.
 * @author JuanSebastian
 */

public class InfoIngRest {
	
	/**
	 * Precio de adicionar este ingrediente a alg�n plato.
	 */
	@JsonProperty(value = "precioAdicion")
	private Double precioAdicion;
	
	/**
	 * Precio de sustituir alg�n ingrediente por este en alg�n plato.
	 */
	@JsonProperty(value="precioSustituto")
	private Double precioSustituto;
	
	/**
	 * Ingrediente que se est� describiendo
	 */
	@JsonProperty(value="ingrediente")
	private Ingrediente ingrediente;
	
	/**
	 * Restaurante que contiene el ingrediente
	 */
	@JsonProperty(value="restaurante")
	private RestauranteMinimum restaurante;
	
	/**
	 * Ingredientes que pueden sustituir a este.
	 */
	@JsonProperty(value="sustitutos")
	private List<Ingrediente> sustitutos;

	public InfoIngRest(@JsonProperty(value="precioAdicion") Double precioAdicion, @JsonProperty(value="precioSustituto") Double precioSustituto,
			@JsonProperty(value="ingrediente") Ingrediente ingrediente, @JsonProperty(value="restaurante") RestauranteMinimum restaurante,
			@JsonProperty(value="sustitutos") List<Ingrediente> sustitutos) {
		this.precioAdicion = precioAdicion;
		this.precioSustituto = precioSustituto;
		this.ingrediente = ingrediente;
		this.restaurante = restaurante;
		this.sustitutos = sustitutos;
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
	
	public List<Ingrediente> getSustitutos() {
		return sustitutos;
	}
	
	public void setSustitutos(List<Ingrediente> sustitutos) {
		this.sustitutos = sustitutos;
	}

}
