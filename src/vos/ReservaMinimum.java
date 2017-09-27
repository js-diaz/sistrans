package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Reserva para un evento en RotondAndes. Representaci�n Minimum.
 * @author JuanSebastian
 */
public class ReservaMinimum {

	/**
	 * Fecha en la que se efectu� la reserva.
	 */
	@JsonProperty(value="fecha")
	private Date fecha;
	
	/**
	 * N�mero de personas.
	 */
	@JsonProperty(value="personas")
	private Integer personas;
	
	public ReservaMinimum(@JsonProperty(value="fecha") Date fecha, @JsonProperty(value="personas")Integer personas) {
		this.fecha = fecha;
		this.personas = personas;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getPersonas() {
		return personas;
	}

	public void setPersonas(Integer personas) {
		this.personas = personas;
	}
	
	

}
