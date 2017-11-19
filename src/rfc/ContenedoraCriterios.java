package rfc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase creda para contener los listados de criterios
 * @author jc161
 *
 */
public class ContenedoraCriterios {
	/**
	 * Criterios de orden
	 */
	@JsonProperty(value="orden")
	private List<CriterioOrden> orden;
	/**
	 * Criterios de agrupación.
	 */
	@JsonProperty(value="agrupacion")
	private List<Criterio> agrupacion;
	/**
	 * Criterios de agregación
	 */
	@JsonProperty(value="agregacion")
	private List<CriterioAgregacion> agregacion;
	/**
	 * Criterios del where
	 */
	@JsonProperty(value="where")
	private CriterioVerdad where;
	/**
	 * Criterios del having
	 */
	@JsonProperty(value="having")
	private CriterioVerdadHaving having;
	/**
	 * Construye una nueva contenedora con los parámetros dados.<br>
	 * @param orden Criterios de orden.<br>
	 * @param agrupacion Criterios de agrupación.<br>
	 * @param agregacion Criterios de agregación.<br>
	 * @param where Criterios del where.<br>
	 * @param having Criterios del having.
	 * @throws Exception Si hay algún error del constructor
	 */
	public ContenedoraCriterios(@JsonProperty(value="orden")List<CriterioOrden> orden, 
			@JsonProperty(value="agrupacion")List<Criterio> agrupacion, @JsonProperty(value="agregacion")List<CriterioAgregacion> agregacion,
			@JsonProperty(value="where")CriterioVerdad where, @JsonProperty(value="having")CriterioVerdadHaving having) throws Exception {
		super();
		this.orden = orden;
		this.agrupacion = agrupacion;
		if(agregacion!=null && agregacion.size()>0)
			this.agregacion = agregacion;
		else
		{
			this.agregacion=new ArrayList<CriterioAgregacion>();
			this.agregacion.add(new CriterioAgregacion(null,null,false));
		}
		this.where = where;
		this.having = having;
	}
	/**
	 * Obtiene el listado de orden.<br>
	 * @return orden
	 */
	public List<CriterioOrden> getOrden() {
		return orden;
	}
	/**
	 * Modifica el listado del orden al dado por parámetro.<br>
	 * @param orden
	 */
	public void setOrden(List<CriterioOrden> orden) {
		this.orden = orden;
	}
	/**
	 * Obtiene el listado de agrupaciones.<br>
	 * @return agrupacion
	 */
	public List<Criterio> getAgrupacion() {
		return agrupacion;
	}
	/**
	 * Modifica el listado de agrupaciones al dado por parámetro.<br>
	 * @param agrupacion
	 */
	public void setAgrupacion(List<Criterio> agrupacion) {
		this.agrupacion = agrupacion;
	}
	/**
	 * Obtiene las agregaciones.<br>
	 * @return agregacion
	 */
	public List<CriterioAgregacion> getAgregacion() {
		return agregacion;
	}
	/**
	 * Modifica el valor de la agregación al dado por parámtro.<br>
	 * @param agregacion
	 */
	public void setAgregacion(List<CriterioAgregacion> agregacion) {
		this.agregacion = agregacion;
	}
	/**
	 * Obtiene el criterio de verdad usado en el where.<br>
	 * @return where
	 */
	public CriterioVerdad getWhere() {
		return where;
	}
	/**
	 * Modifica el criterio de verdad del where al valor dado por parámetro.<br>
	 * @param where
	 */
	public void setWhere(CriterioVerdad where) {
		this.where = where;
	}
	/**
	 * Obtiene el criterio de verdad del having.<br>
	 * @return having
	 */
	public CriterioVerdadHaving getHaving() {
		return having;
	}
	/**
	 * Modifica el valor del criterio de verdad del having al dado por parámetro
	 * @param having
	 */
	public void setHaving(CriterioVerdadHaving having) {
		this.having = having;
	}
	
	
	
	
	
	
}
