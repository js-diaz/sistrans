package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela un criterio cualquiera.<br>
 * @author jc161
 *
 */
public class Criterio {

	/**
	 * Nombre del criterio.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Tipos de agregaciones
	 */
	public enum Agregaciones{
		SUM,MIN,MAX,COUNT,AVG
	}
	
	/**
	 * Palabras especiales usadas para sql.<br>
	 */
	public enum PalabrasEspeciales{
		DISTINCT,ASC,DESC
	}
	/**
	 * Construye un nuevo criterio.<br>
	 * @param nombre Nombre del nuevo criterio.
	 */
	public Criterio(@JsonProperty (value="nombre")String nombre)
	{
		this.nombre=nombre;
	}
	/**
	 * Construye un nuevo criterio de agregación.<br>
	 * @param valorAgrupacion Valor del atributo a analizar.<br>
	 * @param agr Agregación a utilizar.<br>
	 * @param distinto Se buscan valores únicos.<br>
	 * @throws Exception Si se construye un criterio erróneo.
	 */
	public Criterio(@JsonProperty(value="valorAgrupacion")String valorAgrupacion, 
			@JsonProperty(value="agregacion")Agregaciones agr, @JsonProperty(value="distinto")
			boolean distinto) throws Exception
	{
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
	/**
	 * Crea un criterio de órden ascendente o descendete.<br>
	 * @param nombre Nombre del criterio.<br>
	 * @param ascendente Si es ascendente o no.
	 */
	public Criterio(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="ascendente") boolean ascendente)
	{
		this.nombre=nombre;
		if(ascendente) this.nombre+=" "+PalabrasEspeciales.ASC;
		else this.nombre+=" "+PalabrasEspeciales.DESC;
	}
	
	/**
	 * Retorna el nombre.<br>
	 * @return nombre
	 */
	public String getNombre(){ return nombre;}
	/**
	 * Modifica la categoría al parámetro dado.<br>
	 * @param n Nueva categoría
	 */
	public void setNombre(String n){ nombre=n;}
	/**
	 * Override del equals.<br>
	 * @param o Objeto a comparar.<br>
	 * @return Si los objetos son iguales o no.
	 */
	@Override
	public boolean equals(Object o)
	{
		if(! (o instanceof Criterio)) return false;
		Criterio c=(Criterio)o;
		String n1=nombre.replaceAll(""+PalabrasEspeciales.ASC, "").replaceAll(""+PalabrasEspeciales.DESC, "").trim();
		String n2=c.nombre.replaceAll(""+PalabrasEspeciales.ASC, "").replaceAll(""+PalabrasEspeciales.DESC, "").trim();
		return n1.equals(n2);
	}
}
