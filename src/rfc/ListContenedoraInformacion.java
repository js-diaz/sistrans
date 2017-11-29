package rfc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Clase usada para contener información.<br>
 * @author jc161
 *
 */
public class ListContenedoraInformacion {
	/**
	 * Información utilizada.
	 */
	@JsonProperty(value="informacion")
	private List<ContenedoraInformacion> informacion;
	
	/**
	 * Constructor de una nueva conteendora.<br>
	 * @param list Información de la contenedora.
	 */
	public ListContenedoraInformacion(List<ContenedoraInformacion>list)
	{
		informacion=list;
	}
	
	/**
	 * Obtiene información del sistema.<br>
	 * @return informacion
	 */
	public List<ContenedoraInformacion> getInformacion() {
		return informacion;
	}
	/**
	 * Modifica la información al valor dado por parámetro.<br>
	 * @param informacion
	 */
	public void setInformacion(List<ContenedoraInformacion> informacion) {
		this.informacion = informacion;
	}

	
}
