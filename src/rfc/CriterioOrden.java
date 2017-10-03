package rfc;

import org.codehaus.jackson.annotate.JsonProperty;

public class CriterioOrden extends Criterio{
	/**
	 * Crea un criterio de órden ascendente o descendete.<br>
	 * @param nombre Nombre del criterio.<br>
	 * @param ascendente Si es ascendente o no.
	 */
	public CriterioOrden(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="ascendente") boolean ascendente)
	{
		super(nombre);
		String temp="";
		temp=nombre;
		if(ascendente) temp+=" "+PalabrasEspeciales.ASC;
		else temp+=" "+PalabrasEspeciales.DESC;
		setNombre(temp);
	}

}
