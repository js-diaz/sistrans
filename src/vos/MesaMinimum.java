package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Mesa con valores mínimos<br>
 * @author jc161
 *
 */
public class MesaMinimum {

	/**
	 * Id de la mesa
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Capacidad de la mesa
	 */
	@JsonProperty(value="capacidad")
	private int capacidad;
	/**
	 * Capacidad ocupada de la mesa
	 */
	@JsonProperty(value="capacidadOcupada")
	private int capacidadOcupada;
	/**
	 * Mesa minimum construida.<br>
	 * @param id Id de la mesa.<br>
	 * @param capacidad Capacidad de la mesa.<br>
	 * @param capacidadOcupada Capacidad ocuapda.
	 */
	public MesaMinimum(@JsonProperty(value="id")Long id, 
			@JsonProperty(value="capacidad")int capacidad, 
			@JsonProperty(value="capacidadOcupada")int capacidadOcupada) {
		super();
		this.id = id;
		this.capacidad = capacidad;
		this.capacidadOcupada = capacidadOcupada;
	}
	/**
	 * Obtiene el id.<br>
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Modifica el id al valor dado por parámetro.<br>
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Obtiene la capacidad.<br>
	 * @return capacidad
	 */
	public int getCapacidad() {
		return capacidad;
	}
	/**
	 * Modifica la capacidad al valor dado por parámetro.<br>
	 * @param capacidad
	 */
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	/**
	 * Retorna la capacidad ocupada.<br>
	 * @return capacidadOcupada
	 */
	public int getCapacidadOcupada() {
		return capacidadOcupada;
	}
	/**
	 *Modifica la capacidad ocupada al valor dado por parámetro.<br>
	 * @param capacidadOcupada
	 */
	public void setCapacidadOcupada(int capacidadOcupada) {
		this.capacidadOcupada = capacidadOcupada;
	}
	/**
	 * Override del equals.<br>
	 * @param m Objeto a comparar.<br>
	 * @return Si el objeto es o no igual al que se está comparando.
	 */
	@Override
	public boolean equals(Object m)
	{
		if(!(m instanceof Mesa)) return false;
		Mesa mesa=(Mesa)m;
		if(this.getId().equals(mesa.getId())) return true;
		else return false;
	}
	
	
	
	
}
