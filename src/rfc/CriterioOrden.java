package rfc;

import org.codehaus.jackson.annotate.JsonProperty;

public class CriterioOrden extends Criterio{
	@JsonProperty(value="agregacion")
	private CriterioAgregacion agregacion;
	/**
	 * Crea un criterio de órden ascendente o descendete.<br>
	 * @param nombre Nombre del criterio.<br>
	 * @param ascendente Si es ascendente o no.
	 */
	public CriterioOrden(@JsonProperty(value="agregacion") CriterioAgregacion agregacion,@JsonProperty(value="nombre") String nombre, @JsonProperty(value="ascendente") boolean ascendente)
	{
		super(nombre);
		if(agregacion!=null)
		{
			this.agregacion=agregacion;
			setNombre(agregacion.getNombre());
		}
		String temp="";
		temp=this.nombre;
		if(ascendente) temp+=" "+PalabrasEspeciales.ASC;
		else temp+=" "+PalabrasEspeciales.DESC;
		setNombre(temp);
	}
	/**
	 * Obtiene la agregación asignada.<br>
	 * @return agregacion
	 */
	public CriterioAgregacion getAgregacion() {
		return agregacion;
	}
	/**
	 * Modifica la agregación asignada al valor dado por parámetro.<br>
	 * @param agregacion
	 */
	public void setAgregacion(CriterioAgregacion agregacion) {
		this.agregacion = agregacion;
	}

}
