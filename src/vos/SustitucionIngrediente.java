package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela cuando un cliente pide una sustitución de ingrediente.
 * @author JuanSebastian
 */
public class SustitucionIngrediente {

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

	public SustitucionIngrediente(@JsonProperty(value="original") Ingrediente original, @JsonProperty(value="sustituto") Ingrediente sustituto) {
		this.original = original;
		this.sustituto = sustituto;
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
	
	
	
	
}
