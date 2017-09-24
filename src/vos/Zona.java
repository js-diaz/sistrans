package vos;
/**
 * Clase que modela una zona del restuarante.
 * @author jc161
 *
 */

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Zona {
	/**
	 * Capacidad de la zona.
	 */
	@JsonProperty(value="capacidad")
	private int capacidad;
	/**
	 * Posibilidad de ingreso especial.
	 */
	@JsonProperty(value="ingresoEspecial")
	private boolean ingresoEspecial;
	/**
	 * Está abierta o no.
	 */
	@JsonProperty(value="abiertaActualmente")
	private boolean abiertaActualmente;
	/**
	 * capacidad ocupada
	 */
	@JsonProperty(value="capacidadOcupada")
	private int capacidadOcupada;
	/**
	 * Nombre de la zona
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Condiciones de la zona.
	 */
	@JsonProperty(value="condiciones")
	private List<CondicionTecnica> condiciones;
	/**
	 * Restaurantes de la zona.
	 */
	@JsonProperty(value="restaurantes")
	private List<Restaurante> restaurantes;
	/**
	 * Construye una nueva zona con los parámetros dados.<br>
	 * @param capacidad<br>
	 * @param ingresoEspecial<br>
	 * @param abiertaActualmente<br>
	 * @param capacidadOcupada<br>
	 * @param nombre<br>
	 * @param condiciones<br>
	 * @param restaurantes<br>
	 */
	public Zona(@JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="ingresoEspecial")boolean ingresoEspecial, @JsonProperty(value="abiertaActualmente")boolean abiertaActualmente, @JsonProperty(value="capacidadOcupada")int capacidadOcupada, @JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="condiciones")List<CondicionTecnica> condiciones, @JsonProperty(value="restaurantes")List<Restaurante> restaurantes) {
		super();
		this.capacidad = capacidad;
		this.ingresoEspecial = ingresoEspecial;
		this.abiertaActualmente = abiertaActualmente;
		this.capacidadOcupada = capacidadOcupada;
		this.nombre = nombre;
		this.condiciones = condiciones;
		this.restaurantes = restaurantes;
	}
	/**
	 * Obtiene la capacidad de la zona.<br>
	 * @return capacidad
	 */
	public int getCapacidad() {
		return capacidad;
	}
	/**
	 * Modifica la capacidad al valor dado por parámetro.<bR>
	 * @param capacidad
	 */
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	/**
	 * Obtiene si es de ingreso especial o no.<br>
	 * @return ingresoEspecial
	 */
	public boolean isIngresoEspecial() {
		return ingresoEspecial;
	}
	/**
	 * Modifica el valor del ingreso especial al dado por parámetro.<br>
	 * @param ingresoEspecial
	 */
	public void setIngresoEspecial(boolean ingresoEspecial) {
		this.ingresoEspecial = ingresoEspecial;
	}
	/**
	 * Obtiene si está abierto actualmente o no.<br>
	 * @return abiertaActualmente.
	 */
	public boolean isAbiertaActualmente() {
		return abiertaActualmente;
	}
	/**
	 * Modifica el valor de verdad de estar abierto en la rotonda.<br>
	 * @param abiertaActualmente
	 */
	public void setAbiertaActualmente(boolean abiertaActualmente) {
		this.abiertaActualmente = abiertaActualmente;
	}
	/**
	 * Obtiene la capacidad ocupada del lugar.<br>
	 * @return capacidadOcupada
	 */
	public int getCapacidadOcupada() {
		return capacidadOcupada;
	}
	/**
	 * Modifica la capacidad ocupada del restaurante al valor dado por parámetro.<bR>
	 * @param capacidadOcupada
	 */
	public void setCapacidadOcupada(int capacidadOcupada) {
		this.capacidadOcupada = capacidadOcupada;
	}
	/**
	 * Obtiene el nombre de la zona.<br>
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre al valor dado por parámetro.<br>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene un listado de las condiciones técnicas.<br>
	 * @return condiciones
	 */
	public List<CondicionTecnica> getCondiciones() {
		return condiciones;
	}
	/**
	 * Modifica el valor de las condiciones al dado por parámetro.<bR>
	 * @param condiciones 
	 */
	public void setCondiciones(List<CondicionTecnica> condiciones) {
		this.condiciones = condiciones;
	}
	/**
	 * Obtiene los restaurantes de la zona.<br>
	 * @return restaurantes
	 */
	public List<Restaurante> getRestaurantes() {
		return restaurantes;
	}
	/**
	 * Modifica el valor de los restaurantes al dado por parámetro.<br>
	 * @param restaurantes
	 */
	public void setRestaurantes(List<Restaurante> restaurantes) {
		this.restaurantes = restaurantes;
	}
	/**
	 * Agrega un restaurante a la zona.<br>
	 * @param restaurante Restaurante a agregar.<br>
	 */
	public void agregarRestaurante(Restaurante restaurante)
	{
		restaurantes.add(restaurante);
	}
	/**
	 * Elimina un restaurante de la zona asignada.<br>
	 * @param r Restaurante a eliminar.
	 */
	public void eliminarRestaurante(Restaurante r)
	{
		restaurantes.remove(r);
	}
	/**
	 * Agrega una condición a la lista dada.<br>
	 * @param c Condición técnica.
	 */
	public void agregarCondicionTecnica(CondicionTecnica c)
	{
		condiciones.add(c);
	}
	/**
	 * Elimina una condición de la lista dada.<br>
	 * @param c Condición técnica.
	 */
	public void removerCondicionTecnica(CondicionTecnica c)
	{
		condiciones.remove(c);
	}
	/**
	 * Aumenta la capacidad de la zona en el valor dado.<br>
	 * @param cantidad
	 */
	public void aumentarCapacidad(int cantidad)
	{
		capacidadOcupada+=cantidad;
	}
	/**
	 * Reduce la capacidad de la zona dada.<br>
	 * @param cantidad
	 */
	public void reducirCapacidad(int cantidad)
	{
		capacidadOcupada-=cantidad;
	}

}
