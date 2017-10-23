package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela a un usuario del sistema.
 * @author jc161
 *
 */
public class Usuario extends UsuarioMinimum {
	
	/**
	 * Preferencia del usuario
	 */
	@JsonProperty(value="preferencia")
	private Preferencia preferencia;
	/**
	 * historial de compras del usuario
	 */
	@JsonProperty(value="historial")
	private List<CuentaMinimum> historial;
	/**
	 * Restaurante del usuario si es representante.
	 */
	@JsonProperty(value="restaurante")
	private RestauranteMinimum restaurante;
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
	public Usuario(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="id")Long id, @JsonProperty(value="correo")String correo, @JsonProperty(value="rol")Rol rol, @JsonProperty(value="preferencia")Preferencia preferencia, @JsonProperty(value="historial")List<CuentaMinimum> historial,
			@JsonProperty(value="restaurante")RestauranteMinimum restaurante2) {
		super(nombre,id,correo,rol);
		this.preferencia = preferencia;
		this.historial = historial;
		this.restaurante = restaurante2;
	}
	public Preferencia getPreferencia() {
		return preferencia;
	}
	/**
	 * Modifica la preferencia al valor dado por parámetro.<br>
	 * @param preferencia
	 */
	public void setPreferencia(Preferencia preferencia) {
		this.preferencia = preferencia;
	}
	/**
	 * Obtiene el historial del usuario de compras.<br>
	 * @return historial
	 */
	public List<CuentaMinimum> getHistorial() {
		return historial;
	}
	/**
	 * Modifica el historial al dado por parámetro.<br>
	 * @param historial
	 */
	public void setHistorial(List<CuentaMinimum> historial) {
		this.historial = historial;
	}
	/**
	 * Obtiene el restaurante del usuario.<br>
	 * @return restaurante
	 */
	public RestauranteMinimum getRestaurante() {
		return restaurante;
	}
	/**
	 * Modifica el restaurante al valor dado por parámetro.<br>
	 * @param restaurante
	 */
	public void setRestaurante(RestauranteMinimum restaurante) {
		this.restaurante = restaurante;
	}
	
}
