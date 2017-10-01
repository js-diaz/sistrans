package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa una condición de verdad.<br>
 * @author jc161
 *
 */
public class CriterioVerdad {

	/**
	 * Palabras reservadas para operaciones de verdad.
	 */
	public enum PalabrasVerdad{
		IN,NOT,LIKE,AND,OR
	}
	/**
	 * Arreglo de operaciones básicas de SQL.
	 */
	public String[] operaciones=new String[]{"=","<=","<",">",">="};
	/**
	 * Nombre de la condición técnica.
	 */
	@JsonProperty (value="nombre")
	private String nombre;
	/**
	 * Listado de criterios utilizados.
	 */
	private ArrayList<Criterio> criterios;
	/**
	 * Criterio para crear un criterio de comparación entre dos elementos.<br>
	 * @param valorAnterior Valor anterior de la comparación.<br>
	 * @param valorComparacion Valor a comparar.<br>
	 * @param i Índice de las operaciones. -1 si se va a usar un LIKE.<br>
	 * @param afirmativo Si es negativo o no.
	 */
	public CriterioVerdad(@JsonProperty(value="valorAnterior")Criterio valorAnterior, @JsonProperty(value="valorComparacion")String valorComparacion,
			@JsonProperty("operacion")String operacion, boolean afirmativo)
	{
		boolean agregado=false;
		criterios=new ArrayList<>();
		criterios.add(valorAnterior);
		for(int i=0;i<operaciones.length && !agregado;i++) 
			if(operaciones[i].equals(operacion))
			{
				nombre=valorAnterior.getNombre()+operacion+valorComparacion;
				agregado=true;
			}
		if(!agregado) nombre=valorAnterior.getNombre()+" "+PalabrasVerdad.LIKE+" "+valorComparacion;
		if(!afirmativo) nombre=PalabrasVerdad.NOT+" ("+nombre+")";
	}
	 
	/**
	 * Criterio para crear un criterio de comparación entre dos elementos.<br>
	 * <b> pre:</b> El criterio agregado es una agregación simple o un atributo sin órdenes.<br>
	 * @param valorAnterior Valor anterior de la comparación.<br>
	 * @param valorComparacion Valor a comparar.<br>
	 * @param i Índice de las operaciones. -1 si se va a usar un LIKE.<br>
	 * @param afirmativo Si es negativo o no.
	 */
	public CriterioVerdad(@JsonProperty(value="valorAnterior")Criterio valorAnterior, @JsonProperty(value="valorComparacion")Criterio valorComparacion,
			@JsonProperty("operacion")String operacion, boolean afirmativo)
	{
		boolean agregado=false;
		criterios=new ArrayList<>();
		criterios.add(valorAnterior);
		criterios.add(valorComparacion);
		for(int i=0;i<operaciones.length && !agregado;i++) 
			if(operaciones[i].equals(operacion))
			{
				nombre=valorAnterior.getNombre()+operacion+valorComparacion.getNombre();
				agregado=true;
			}
		if(!afirmativo) nombre=PalabrasVerdad.NOT+" ("+nombre+")";
	}
	/**
	 * Crea un criterio para verificar si está en un conjunto dado o no.<br>
	 * <b> pre:</b> El criterio agregado es una agregación simple o un atributo sin órdenes.<br>
	 * @param valor Valor a comparar.<br>
	 * @param valores Listado de valores.<br>
	 * @param afirmativo Si está o no.
	 */
	public CriterioVerdad(@JsonProperty(value="valor")Criterio valor, @JsonProperty(value="valores")List<Criterio>valores,
			@JsonProperty(value="afirmativo")boolean afirmativo)
	{
		criterios=new ArrayList<>();
		criterios.add(valor);
		nombre=valor+" "+PalabrasVerdad.IN+" ("+valores.get(0);
		if(valores.size()>1)
			for(int i=1;i<valores.size();i++)
			{
				nombre+=","+valores.get(i).getNombre();
				criterios.add(valores.get(i));
			}
		nombre+=")";
		if(!afirmativo)
			nombre=PalabrasVerdad.NOT+"("+nombre+")";
	}
	 
	/**
	 * Concatena dos criterios de verdad con un AND o un OR.<br>
	 * @param c1 Primer criterio.<br>
	 * @param c2 Segundo criterio.<br>
	 * @param conjuncion Es un operador de conjunción.<br>
	 * @param afirmativo Es una negación o no.
	 */
	public CriterioVerdad(CriterioVerdad c1, CriterioVerdad c2, boolean conjuncion, boolean afirmativo)
	{
		if(c1!=null && c2!=null)
		{
			criterios=new ArrayList<>();
			criterios.addAll(c1.criterios);
			criterios.addAll(c2.criterios);
			if(conjuncion) nombre=c1.nombre+" "+PalabrasVerdad.AND+" "+c2.nombre;
			else nombre=c1.nombre+" "+PalabrasVerdad.OR+" "+c2.nombre;
		}
		if(!afirmativo)
		{
			nombre=PalabrasVerdad.NOT+"("+nombre+")";
		}
		nombre="("+nombre+")";
	}
	/**
	 * Obtiene el nombre del criterio.<br>
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre al valor dado por parámetro.<br>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Obtiene el listado de criterios.<br>
	 * @return criterios
	 */
	public ArrayList<Criterio> getCriterios()
	{
		return criterios;
	}
	/**
	 * Modifica el valor de criterios al dado por parámetro.<br>
	 * @param criterios
	 */
	public void setCriterios(ArrayList<Criterio> criterios)
	{
		this.criterios=criterios;
	}
	
	
}
