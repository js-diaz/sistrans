package vos;

import java.util.ArrayList;
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
	public void agregarPreferenciaDeZona(ZonaMinimum zona)
	{
		preferencia.agregarZonaMinimum(zona);
	}
	/**
	 * Remueve una preferencia de zona del usuario<br>
	 * @param zona Zona a remover
	 */
	public void removerPreferenciaDeZona(ZonaMinimum zona)
	{
		
		preferencia.removerZonaMinimum(zona);
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
