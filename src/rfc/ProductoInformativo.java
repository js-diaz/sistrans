package rfc;
/**
 * Clase que modela un producto de RotondAndes
 * @author jc161
 *
 */

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoInformativo {

	
	
	/**
	 * Nombre del producto.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Tipo del producto
	 */
	@JsonProperty(value="tipo")
	private String tipo;
	/**
	 * Descripcion del producto.
	 */
	@JsonProperty(value="descripcion")
	private String descripcion;
	/**
	 * Traducción del producto.
	 */
	@JsonProperty(value="traduccion")
	private String traduccion;
	
	/**
	 * Identificación del producto
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Construye un nuevo producto con los paráemtros dados.<br>
	 * @param personalizable<br>
	 * @param nombre<br>
	 * @param prcio<br>
	 * @param tipo<br>
	 * @param descripcion<br>
	 * @param traduccion<br<
	 * @param tiempo<br>
	 * @param costoProduccion<br>
	 * @param id <br>
	 */
	public ProductoInformativo(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo, @JsonProperty(value="descripcion")String descripcion,
			@JsonProperty(value="traduccion")String traduccion, @JsonProperty(value="id")Long id) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.traduccion = traduccion;
		this.id = id;
	}
	
	/**
	 * Obtiene el nombre del producto.<br>
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre del producto al dado por parámetro.<br>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Obtiene el tipo de plato.<br>
	 * @return tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * Modifica el tipo de plato al dado por parámetro.<br>
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * Obtiene la descripción del producto.<br>
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * Modifica la descripción a aquella dada por parámetro.<br>
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
	 * Modifica la traducción a aquella dada por parámetro.<br>
	 * @param traduccion
	 */
	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
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
	
	
	
}

