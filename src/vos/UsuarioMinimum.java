package vos;

import java.util.List;


import org.codehaus.jackson.annotate.JsonProperty;


/**
 * Clase que modela a un usuario del sistema.
 * @author jc161
 *
 */
public class UsuarioMinimum {
	/**
	 * Roles posibles de un usuario en el sistema.
	 * @author jc161
	 *
	 */
	public enum Rol {
		@JsonProperty("cliente")
		CLIENTE,
		@JsonProperty("local")
		LOCAL,
		@JsonProperty("operador")
		OPERADOR,
		@JsonProperty("proveedor")
		PROVEEDOR,
		@JsonProperty("organizadores")
		ORGANIZADORES;
	}
	
	/**
	 * Nombre del usuario
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Id del usuario
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Correo 
	 */
	@JsonProperty(value="correo")
	private String correo;
	/**
	 * Rol del usuairo
	 */
	@JsonProperty(value="rol")
	private Rol rol;
	/**
	 * Crea un nuevo usuario con los atributos dados por parámetro.<br>
	 * @param nombre<br>
	 * @param id<br>
	 * @param correo<br>
	 * @param rol<br>
	 * @param preferencia<br>
	 * @param historial<br>
	 * @param restaurante<br>
	 */
	public UsuarioMinimum(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="id")Long id, @JsonProperty(value="correo")String correo, @JsonProperty(value="rol")Rol rol) {
		super();
		this.nombre = nombre;
		this.id = id;
		this.correo = correo;
		this.rol = rol;
	}
	/**
	 * Obtiene el nombre del usuario.<br>
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre al valor dado por parámetro.<bR>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene el id del usuario.<br>
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Modifica el valor del id al dado por parámetro.<br>
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Obtiene el correo del usuario.<br>
	 * @return correo
	 */
	public String getCorreo() {
		return correo;
	}
	/**
	 * Modifica el correo al valor dado por parámetro.<br>
	 * @param correo
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	/**
	 * Obtiene el rol del usuario.<br>
	 * @return rol
	 */
	public Rol getRol() {
		return rol;
	}
	/**
	 * Modifica el rol al valor dado por parámetro.<br>
	 * @param rol
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}
}
