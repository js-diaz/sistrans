package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Clase usada para contener información.<br>
 * @author jc161
 *
 */
public class ContenedoraInformacion {
	/**
	 * Información utilizada.
	 */
	@JsonProperty(value="informacion")
	private List<Informacion> informacion;
	
	/**
	 * Constructor de una nueva conteendora.<br>
	 * @param nombres Nombres de la información a agregar.
	 */
	public ContenedoraInformacion(String[] nombres)
	{
		informacion=new ArrayList<>();
		for(String v:nombres)
			informacion.add(new Informacion(v,""));
	}
	/**
	 * Crea una contenedora vacía.
	 */
	public ContenedoraInformacion() {
		informacion=new ArrayList<>();
	}
	/**
	 * Modifica un valor dado en la lista.<br>
	 * @param i Valor a modificar.<br>
	 * @param valor Valor con el que se modifica.
	 */
	public void modificarInformacion(int i, String valor)
	{
		informacion.get(i).setValor(valor);
	}
	/**
	 * Da el nombre de una posición de la contenedora.<br>
	 * @param i Posición.<br>
	 * @return Nombre en la posición dada.
	 */
	public String darNombre(int i)
	{
		return informacion.get(i).getNombre();
	}
	
	/**
	 * Da el valor de una posición de la contenedora.<br>
	 * @param i Posición.<br>
	 * @return Valor en la posición dada.
	 */
	public String darValor(int i)
	{
		return informacion.get(i).getValor();
	}
	/**
	 * Agrega información a la lista.<br>
	 * @param nombre Nombre de al información.<br>
	 * @param valor Valor asignado.
	 */
	public void agregarInformacion(String nombre, String valor)
	{
		informacion.add(new Informacion(nombre,valor));
	}
	/**
	 * Obtiene información del sistema.<br>
	 * @return informacion
	 */
	public List<Informacion> getInformacion() {
		return informacion;
	}
	/**
	 * Modifica la información al valor dado por parámetro.<br>
	 * @param informacion
	 */
	public void setInformacion(List<Informacion> informacion) {
		this.informacion = informacion;
	}
	/**
	 * Pasa todo el mensaje a un toString.<br>
	 * @return mensaje de toda la información presente.
	 */
	public String toString()
	{
		String msj="";
		for(Informacion i:informacion)
		{
			msj+=i.getNombre()+":"+i.getValor()+"\n";
		}
		return msj;
	}

	
}
