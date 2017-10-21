package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela cuando un cliente pide una sustitución de ingrediente en algun producto de un menu.
 * @author JuanSebastian
 */
public class SustitucionIngredienteEnProducto {

	/**
	 * Ingrediente a ser sustituido.
	 */
	@JsonProperty(value="original")
	private Ingrediente original;
	
	/**
	 * Ingrediente a sustituirlo.
	 */
	@JsonProperty(value="sustituto")
	private Ingrediente sustituto;
	
	/**
	 * Producto en el que se llevará a cabo la sustitución.
	 */
	@JsonProperty(value="producto")
	private Producto producto;

	public SustitucionIngredienteEnProducto(@JsonProperty(value="original") Ingrediente original, @JsonProperty(value="sustituto") Ingrediente sustituto,
			@JsonProperty(value="producto") Producto producto) {
		this.original = original;
		this.sustituto = sustituto;
		this.producto = producto;
	}

	public Ingrediente getOriginal() {
		return original;
	}

	public void setOriginal(Ingrediente original) {
		this.original = original;
	}

	public Ingrediente getSustituto() {
		return sustituto;
	}

	public void setSustituto(Ingrediente sustituto) {
		this.sustituto = sustituto;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}
