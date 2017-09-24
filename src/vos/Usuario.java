package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela a un usuario del sistema.
 * @author jc161
 *
 */
public class Usuario {
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
	 * Preferencia del usuario
	 */
	@JsonProperty(value="preferencia")
	private Preferencia preferencia;
	/**
	 * historial de compras del usuario
	 */
	@JsonProperty(value="historial")
	private List<Cuenta> historial;
	/**
	 * Restaurante del usuario si es representante.
	 */
	@JsonProperty(value="restaurante")
	private Restaurante restaurante;
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
	public Usuario(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="id")Long id, @JsonProperty(value="correo")String correo, @JsonProperty(value="rol")Rol rol, @JsonProperty(value="preferencia")Preferencia preferencia, @JsonProperty(value="historial")List<Cuenta> historial,
			@JsonProperty(value="restaurante")Restaurante restaurante) {
		super();
		this.nombre = nombre;
		this.id = id;
		this.correo = correo;
		this.rol = rol;
		this.preferencia = preferencia;
		this.historial = historial;
		this.restaurante = restaurante;
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
	/**
	 * Obtiene la preferencia del usuario.<br>
	 * @return preferencia
	 */
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
	public List<Cuenta> getHistorial() {
		return historial;
	}
	/**
	 * Modifica el historial al dado por parámetro.<br>
	 * @param historial
	 */
	public void setHistorial(List<Cuenta> historial) {
		this.historial = historial;
	}
	/**
	 * Obtiene el restaurante del usuario.<br>
	 * @return restaurante
	 */
	public Restaurante getRestaurante() {
		return restaurante;
	}
	/**
	 * Modifica el restaurante al valor dado por parámetro.<br>
	 * @param restaurante
	 */
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
	/**
	 * Agrega una nueva preferencia de precio.<br>
	 * @param inicial Precio inicial.<br>
	 * @param finalP Precio final.<br>
	 */
	public void agregarPreferenciaDePrecio(double inicial, double finalP)
	{
		
				preferencia.setPrecioInicial(inicial);
				preferencia.setPrecioInicial(finalP);
			
	
	}
	/**
	 * Remueve una preferencia de precio.<br>
	 *  
	 */
	public void removerPreferenciaPrecio()
	{
			preferencia.setPrecioFinal(null);
			preferencia.setPrecioInicial(null);
		
	}
	/**
	 * Elimina la preferencia del usuario.
	 */
	public void eliminarPreferencia()
	{
		preferencia=null;
	}
	/**
	 * Agrega una preferencia de zona al usuario.<br>
	 * @param zona Zona a agregar.
	 */
	public void agregarPreferenciaDeZona(Zona zona)
	{
		preferencia.agregarZona(zona);
	}
	/**
	 * Remueve una preferencia de zona del usuario<br>
	 * @param zona Zona a remover
	 */
	public void removerPreferenciaDeZona(Zona zona)
	{
		
		preferencia.removerZona(zona);
	}
	/**
	 * Agrega una nueva preferencia de categoria.<br>
	 * @param categoria Categoria a agregar.
	 */
	public void agregarPreferenciaDeCategoria(Categoria categoria)
	{
		preferencia.agregarCategoria(categoria);
	}
	/**
	 * Remueve una preferencia de categoría del sistema<br>
	 * @param categoria Categoria a remover.
	 */
	public void removerPreferenciaDeCategoria(Categoria categoria)
	{
		preferencia.removerCategoria(categoria);
	}
	/**
	 * Modifica la preferencia de precios del usuario.<br>
	 * @param precioInicial Precio inicial de la preferencia.<br>
	 * @param precioFinal Precio final de la preferencia.<br>
	 */
	public void modificarPreferenciaPrecio(double precioInicial, double precioFinal)
	{
		preferencia.setPrecioInicial(precioInicial);
		preferencia.setPrecioFinal(precioFinal);
	}
	
	
	
	
}
