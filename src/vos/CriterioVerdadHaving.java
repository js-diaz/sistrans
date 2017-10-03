package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.CriterioVerdad.PalabrasVerdad;

public class CriterioVerdadHaving{
	
	
	/**
	 * Nombre de la condición técnica.
	 */
	@JsonProperty (value="nombre")
	private String nombre;
	/**
	 * Listado de criterios utilizados.
	 */
	private ArrayList<CriterioAgregacion> criterios;
	/**
	 * Arreglo de operaciones básicas de SQL.
	 */
	public String[] operaciones=new String[]{"=","<=","<",">",">="};
	
	/**
	 * Primer criterio de la comparación.
	 */
	private CriterioAgregacion valorAnterior;
	/**
	 * Valor de comparación
	 */
	private String comparacion;
	/**
	 * Criterio de comparación.
	 */
	private CriterioAgregacion criterioComparacion;
	/**
	 * Listado de valores
	 */
	private List<String> valores;
	/**
	 * Criterio 1
	 */
	private CriterioVerdadHaving c1;
	/**
	 * Criterio 2
	 */
	private CriterioVerdadHaving c2;
	/**
	 * Operación guardad
	 */
	private String operacion;
	/**
	 * Crea un criterio vacío.
	 */
	public CriterioVerdadHaving(){
		criterios= new ArrayList<>();
	}
	/**
	 * Construye un criterio de verdad generalizado para cualquier clase.<br>
	 * @param valorAnterior Valor anterior.<br>
	 * @param valorComparacion Valor de comparación.<br>
	 * @param criterioComparacion Criterio de comparación.<br>
	 * @param operacion Operación.<br>
	 * @param afirmativo Es afirmativo o no.<br>
	 * @param valores Valores de conjunto.<br>
	 * @param c1 Criterio 1.<br>
	 * @param c2 Criterio 2.<br>
	 * @param conjuncion Se usa una conjunción o no.<br>
	 * @throws Exception Si hay algún error.
	 */
	public CriterioVerdadHaving(@JsonProperty(value="valorAnterior1")CriterioAgregacion valorAnterior, @JsonProperty(value="valorComparacion1")String valorComparacion, @JsonProperty("valorComparacion2")  CriterioAgregacion criterioComparacion,
			@JsonProperty("operacion1")String operacion,@JsonProperty(value="afirmativo1") Boolean afirmativo, @JsonProperty("valores") List<String> valores,
			@JsonProperty(value="c1") CriterioVerdadHaving c1, @JsonProperty("c2") CriterioVerdadHaving c2, @JsonProperty("conjuncion") Boolean conjuncion) throws Exception
	{
		if(conjuncion==null) conjuncion=true;
		if(afirmativo==null) afirmativo=true;
		if(c1!=null && c2!=null)
		{
			tipo4(c1,c2,conjuncion,afirmativo);
		}
		else if(valorAnterior!=null)
		{
			this.valorAnterior=valorAnterior;
			if(valorComparacion!=null) tipo1(valorAnterior,valorComparacion,operacion,afirmativo);
			if(criterioComparacion!=null) tipo2(valorAnterior, criterioComparacion, operacion, afirmativo);
			if(valores!=null) tipo3(valorAnterior, valores, afirmativo);
		}
		else throw new Exception("No cumple con ningún parámetro establecido.");
	}
	/**
	 * Criterio para crear un criterio de comparación entre dos elementos.<br>
	 * @param valorAnterior Valor anterior de la comparación.<br>
	 * @param valorComparacion Valor a comparar.<br>
	 * @param i Índice de las operaciones. -1 si se va a usar un LIKE.<br>
	 * @param afirmativo Si es negativo o no.
	 */
	private void tipo1(@JsonProperty(value="valorAnterior1")CriterioAgregacion valorAnterior, @JsonProperty(value="valorComparacion1")String valorComparacion,
			@JsonProperty("operacion1")String operacion,@JsonProperty(value="afirmativo1") boolean afirmativo)
	{
		comparacion=valorComparacion;
		
		boolean agregado=false;
		criterios=new ArrayList<>();
		criterios.add(valorAnterior);
		for(int i=0;i<operaciones.length && !agregado;i++) 
			if(operaciones[i].equals(operacion))
			{
				this.operacion=operacion;
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
	private void tipo2(@JsonProperty(value="valorAnterior2")CriterioAgregacion valorAnterior, @JsonProperty(value="valorComparacion2")CriterioAgregacion valorComparacion,
			@JsonProperty("operacion2")String operacion,@JsonProperty(value="afirmativo2") boolean afirmativo)
	{
		criterioComparacion=valorComparacion;
		
		boolean agregado=false;
		criterios=new ArrayList<>();
		criterios.add(valorAnterior);
		criterios.add(valorComparacion);
		for(int i=0;i<operaciones.length && !agregado;i++) 
			if(operaciones[i].equals(operacion))
			{
				this.operacion=operacion;
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
	private void tipo3(@JsonProperty(value="valor3")CriterioAgregacion valor, @JsonProperty(value="valores3")List<String>valores,
			@JsonProperty(value="afirmativo3")boolean afirmativo)
	{
		this.valores=valores;
		
		criterios=new ArrayList<>();
		criterios.add(valor);
		nombre=valor.getNombre()+" "+PalabrasVerdad.IN+" ("+valores.get(0);
		if(valores.size()>1)
			for(int i=1;i<valores.size();i++)
			{
				nombre+=","+valores.get(i);
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
	private void tipo4(@JsonProperty(value="c1")CriterioVerdadHaving c1, @JsonProperty(value="c2")CriterioVerdadHaving c2, 
			@JsonProperty(value="conjuncion4")boolean conjuncion, @JsonProperty(value="afirmativo4")boolean afirmativo)
	{
		if(c1!=null && c2!=null)
		{
			this.c1=c1;
			this.c2=c2;
			
			criterios=new ArrayList<>();
			criterios.addAll(c1.criterios);
			criterios.addAll(c2.criterios);
			if(conjuncion) nombre=c1.nombre+" "+PalabrasVerdad.AND+" "+c2.nombre;
			else nombre=c1.nombre+" "+PalabrasVerdad.OR+" "+c2.nombre;
			
			if(!afirmativo)
			{
				nombre=PalabrasVerdad.NOT+"("+nombre+")";
			}
			nombre="("+nombre+")";
		}
		
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
	public ArrayList<CriterioAgregacion> getCriterios()
	{
		return criterios;
	}
	/**
	 * Modifica el valor de criterios al dado por parámetro.<br>
	 * @param criterios
	 */
	public void setCriterios(ArrayList<CriterioAgregacion> criterios)
	{
		this.criterios=criterios;
	}
	
	/**
	 * Obtiene el valor anterior.<br>
	 * @return valorAnterior
	 */
	public CriterioAgregacion getValorAnterior() {
		return valorAnterior;
	}
	/**
	 * Obtiene el valor de comparación.
	 * @return comparacion
	 */
	public String getComparacion() {
		return comparacion;
	}
	/**
	 * Obtiene el criterio de comparación.
	 * @return criterioComparacion
	 */
	public CriterioAgregacion getCriterioComparacion() {
		return criterioComparacion;
	}
	/**
	 * Obtiene el listado de valores.
	 * @return valores
	 */
	public List<String> getValores() {
		return valores;
	}
	/**
	 * Obtiene el criterio 1.<br>
	 * @return c1
	 */
	public CriterioVerdadHaving getC1() {
		return c1;
	}
	/**
	 * Obtiene el criterio 2.<br>
	 * @return c2
	 */
	public CriterioVerdadHaving getC2() {
		return c2;
	}
	/**
	 * Obtiene la operación.<br>
	 * @return operacion
	 */
	public String getOperacion()
	{
		return operacion;
	}
}
