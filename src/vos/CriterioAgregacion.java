package vos;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.Criterio.Agregaciones;
import vos.Criterio.PalabrasEspeciales;

public class CriterioAgregacion extends Criterio {

	/**
	 * Construye un nuevo criterio de agregación.<br>
	 * @param valorAgrupacion Valor del atributo a analizar.<br>
	 * @param agr Agregación a utilizar.<br>
	 * @param distinto Se buscan valores únicos.<br>
	 * @throws Exception Si se construye un criterio erróneo.
	 */
	public CriterioAgregacion(@JsonProperty(value="valorAgrupacion")String valorAgrupacion, 
			@JsonProperty(value="agregacion")Agregaciones agr, @JsonProperty(value="distinto")
			boolean distinto) throws Exception
	{
		super("");
		if(agr==null) agr=Agregaciones.COUNT;
		nombre=agr+"(";
		if(agr.equals(Agregaciones.COUNT)||agr.equals(Agregaciones.SUM)||agr.equals(Agregaciones.AVG))
		{
			if(distinto) nombre+=PalabrasEspeciales.DISTINCT+" ";
		}
		if(valorAgrupacion==null || valorAgrupacion.length()==0)
		{
			
			if(agr.equals(Agregaciones.COUNT))
			{
				if(!distinto)
				nombre+="*)";
				else nombre+=")";
			}
			else throw new Exception("Su agregación es inválida");
		}
		else
		{
			nombre+=valorAgrupacion+")";
		}
	}
	
}
