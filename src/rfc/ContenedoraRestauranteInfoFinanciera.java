package rfc;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Contenedora de restaurante con su información de pedidos de productos en venta
 * @author jc161
 *
 */
public class ContenedoraRestauranteInfoFinanciera {
	/**
	 * Nombre de restaurante
	 */
	@JsonProperty(value="nombreRestaurante")
	private String nombreRestaurante;
	/**
	 * Listado de informaciones financieras por producto
	 */
	@JsonProperty(value="informacionFinanciera")
	private List<InformacionFinanciera> informacionFinanciera;
	/**
	 * Construye una nueva contenedora.<br>
	 * @param nombreRestaurante Nombre del restaurante.<br>
	 * @param informacionFinanciera InformacionFinanciera.
	 */
	public ContenedoraRestauranteInfoFinanciera(@JsonProperty(value="nombreRestaurante")String nombreRestaurante,
			@JsonProperty(value="informacionFinanciera")List<InformacionFinanciera> informacionFinanciera) {
		super();
		this.nombreRestaurante = nombreRestaurante;
		this.informacionFinanciera = informacionFinanciera;
	}
	
	/**
	 * Obtiene el nombre del restaurante.<br>
	 * @return nombreRestaurante
	 */
	public String getNombreRestaurante() {
		return nombreRestaurante;
	}
	/**
	 * Modifica el nombre del restaurante al valor dado por parámetro.<br>
	 * @param nombreRestaurante
	 */
	public void setNombreRestaurante(String nombreRestaurante) {
		this.nombreRestaurante = nombreRestaurante;
	}
	/**
	 * Obtiene la información financiera.<br>
	 * @return informacionFinanciera.
	 */
	public List<InformacionFinanciera> getInformacionFinanciera() {
		return informacionFinanciera;
	}
	/**
	 * Modifica la información financiera por el valor dado por parámetro.<br>
	 * @param informacionFinanciera
	 */
	public void setInformacionFinanciera(List<InformacionFinanciera> informacionFinanciera) {
		this.informacionFinanciera = informacionFinanciera;
	}
	
	
	
}
