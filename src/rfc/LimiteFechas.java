package rfc;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase usada para los límites de fechas.
 * @author jc161
 *
 */
public class LimiteFechas {

	/**
	 * Fecha inicial
	 */
	@JsonProperty(value="fechaInicial")
	private Date fechaInicial;
	/**
	 * Fecha final
	 */
	@JsonProperty(value="fechaFinal")
	private Date fechaFinal;
	
	/**
	 * Creador del límite de fechas.<br>
	 * @param fechaInicial Fecha inicial.<br>
	 * @param fechaFinal Fecha final.
	 */
	public LimiteFechas(@JsonProperty(value="fechaInicial")Date fechaInicial, 
			@JsonProperty(value="fechaFinal")Date fechaFinal) {
		super();
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
	}
	/**
	 * Obtiene la fecha inicial.<br>
	 * @return fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * Modifica la fecha inicial al valor dado por parámetro.<br>
	 * @param fechaInicial
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * Obtiene la fecha final.<br>
	 * @return fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * Modifica la fecha final al valor dado por parámetro.<br>
	 * @param fechaFinal
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	
}
