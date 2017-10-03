package vos;
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
	protected String nombre;
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
