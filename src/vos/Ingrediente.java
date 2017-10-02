package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela un ingrediente.
 * @author jc161
 *
 */
public class Ingrediente {
	/**
	 * Nombre del ingrediente.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Descripción del ingrediente.
	 */
	@JsonProperty(value="descripcion")
	private String descripcion;
	/**
	 * Traducción de la descripción al inglés.
	 */
	@JsonProperty(value="traduccion")
	private String traduccion;
	/**
	 * Id del ingrediente.
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Constructor de la calse ingrediente.<br>
	 * @param nombre Nombre del ingrediente.<br>
	 * @param descripcion Respectiva descripción.<br>
	 * @param traduccion Traducción de la descripción.<br>
	 * @param id Identificación que posee.
	 */
	public Ingrediente(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="descripcion")String descripcion, 
			@JsonProperty(value="traduccion")String traduccion, @JsonProperty(value="id")Long id) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.traduccion = traduccion;
		this.id = id;
	}
	/**
	 * Obtiene el nombre.<br>
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre al dado por parámetro.<br>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene la descripción.<br>
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * Modifica la descripción a la dada por parámetro.<br>
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * Obtiene la traducción de la descripción.<br>
	 * @return traduccion
	 */
	public String getTraduccion() {
		return traduccion;
	}
	/**
	 * Cambia la traducción a la dada por parámetro.<br>
	 * @param traduccion
	 */
	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}
	/**
	 * Obtiene la identifiación del ingrediente.<br>
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Modifica la identificación del ingrediente a la dada por parámetro.<br>
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
	
	
}
