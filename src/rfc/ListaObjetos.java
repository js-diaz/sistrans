package rfc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ListaObjetos {

	/**
	 * Información
	 */
	@JsonProperty(value="Información")
	private List<Object> informacion;
	/**
	 * Construye una nueva lista de objetos.<br>
	 * @param informacion Lista de objetos
	 */
	public ListaObjetos(List<Object>informacion)
	{
		this.informacion=informacion;
	}
	/**
	 * Obtiene la información del listado.<br>
	 * @return informacion
	 */
	public List<Object> getInformacion()
	{
		return informacion;
	}
	/**
	 * Modifica la información al valor dado por parámetro.<br>
	 * @param informacion
	 */
	public void setInformacion(List<Object> informacion)
	{
		this.informacion=informacion;
	}
	/**
	 * Limpia toda la información de la lista.<br>
	 */
	public void clear()
	{
		informacion=new ArrayList<Object>();
	}
}
