package rfc;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase para contener criterios en un rango de fechas dado.<br>
 * @author jc161 
 *
 */
public class ContenedoraCriteriosConLimiteFecha {
	/**
	 * Contenedora de los criterios
	 */
	@JsonProperty(value="contenedoraCriterios")
	private ContenedoraCriterios contenedoraCriterios;
	/**
	 * Límite de las fechas
	 */
	@JsonProperty(value="limiteFechas")
	private LimiteFechas limiteFechas;
	/**
	 * Crea una nueva contenedora con los datos pasados por parámetro.<br>
	 * @param c Contenedora de criterios.<br>
	 * @param lf Límite de fechas
	 */
	public ContenedoraCriteriosConLimiteFecha(@JsonProperty(value="contenedoraCriterios")ContenedoraCriterios c, @JsonProperty(value="limiteFechas")LimiteFechas lf) {
		super();
		this.contenedoraCriterios = c;
		this.limiteFechas = lf;
	}
	/**
	 * Obtiene la contenedora de criterios.<br>
	 * @return contenedoraCriterios
	 */
	public ContenedoraCriterios getContenedoraCriterios() {
		return contenedoraCriterios;
	}
	/**
	 * Modifica la contenedora de criterios al valor dado por parámetro.<br>
	 * @param contenedoraCriterios
	 */
	public void setContenedoraCriterios(ContenedoraCriterios contenedoraCriterios) {
		this.contenedoraCriterios = contenedoraCriterios;
	}
	/**
	 * Obtiene el límite de fechas.<br>
	 * @return limiteFechas
	 */
	public LimiteFechas getLimiteFechas() {
		return limiteFechas;
	}
	/**
	 * Modifica el límite de fechas al valor dado por parámetro.<br>
	 * @param limiteFechas
	 */
	public void setLimiteFechas(LimiteFechas limiteFechas) {
		this.limiteFechas = limiteFechas;
	}
	
	
	
	
	
	
}
