package vos;
/**
 * Clase que modela una zona del restuarante.
 * @author jc161
 *
 */


import org.codehaus.jackson.annotate.JsonProperty;

public class ZonaMinimum {
	/**
	 * Capacidad de la zona.
	 */
	@JsonProperty(value="capacidad")
	protected int capacidad;
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
	protected int capacidadOcupada;
	/**
	 * Nombre de la zona
	 */
	@JsonProperty(value="nombre")
	private String nombre;
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
	public ZonaMinimum(@JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="ingresoEspecial")boolean ingresoEspecial, @JsonProperty(value="abiertaActualmente")boolean abiertaActualmente, @JsonProperty(value="capacidadOcupada")int capacidadOcupada, @JsonProperty(value="nombre")String nombre)
	{
		super();
		this.capacidad = capacidad;
		this.ingresoEspecial = ingresoEspecial;
		this.abiertaActualmente = abiertaActualmente;
		this.capacidadOcupada = capacidadOcupada;
		this.nombre = nombre;
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
	

}
