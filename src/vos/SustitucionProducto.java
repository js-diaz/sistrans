package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela cuando un cliente pide una sustitución de producto.
 * @author JuanSebastian
 */
public class SustitucionProducto {

	/**
	 * Producto a ser sustituido.
	 */
	@JsonProperty(value="original")
	private Producto original;
	
	/**
	 * Producto a sustituirlo.
	 */
	@JsonProperty(value="sustituto")
	private Producto sustituto;

	public SustitucionProducto(@JsonProperty(value="original") Producto original, @JsonProperty(value="sustituto") Producto sustituto) {
		this.original = original;
		this.sustituto = sustituto;
	}

	public Producto getOriginal() {
		return original;
	}

	public void setOriginal(Producto original) {
		this.original = original;
	}

	public Producto getSustituto() {
		return sustituto;
	}

	public void setSustituto(Producto sustituto) {
		this.sustituto = sustituto;
	}
	
	
	
	
}
