package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Vo de la mesa.<br>
 * @author jc161
 *
 */
public class Mesa extends MesaMinimum {

	/**
	 * Zona a la que pertenece.
	 */
	@JsonProperty(value="zona")
	private ZonaMinimum zona;
	/**
	 * Listado de cuentas.
	 */
	@JsonProperty(value="cuentas")
	private List<CuentaMinimum> cuentas;
	/**
	 * Listado de cuentas pagadas.
	 */
	@JsonProperty(value="pagadas")
	private List<CuentaMinimum> pagadas;
	/**
	 * Construye una nueva mesa con los parámetros dados.<br>
	 * @param id Id de la mesa.<br>
	 * @param capacidad Capacidad de la mesa.<br>
	 * @param capacidadOcupada Capacidad ocupada de la mesa.<br>
	 * @param zona Zona de la mesa.<br>
	 * @param cuentas Cuentas de la mesa.<br>
	 */
	public Mesa(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, 
			@JsonProperty(value="capacidadOcupada")int capacidadOcupada, @JsonProperty(value="zona")ZonaMinimum zona, 
			@JsonProperty(value="cuentas")List<CuentaMinimum> cuentas,@JsonProperty(value="pagadas") List<CuentaMinimum> pagadas) {
		super(id, capacidad, capacidadOcupada);
		this.zona = zona;
		this.cuentas = cuentas;
		this.pagadas=pagadas;
	}
	/**
	 * Obtiene la zona en representación minimum.<br>
	 * @return zona
	 */
	public ZonaMinimum getZona() {
		return zona;
	}
	/**
	 * Modifica el valor de la zona al dado por parámetro.<br>
	 * @param zona
	 */
	public void setZona(ZonaMinimum zona) {
		this.zona = zona;
	}
	/**
	 * Obtiene el listado de cuentas.<br>
	 * @return cuentas
	 */
	public List<CuentaMinimum> getCuentas() {
		return cuentas;
	}
	/**
	 * Modifica el listado de cuentas al dado por parámetro.<br>
	 * @param cuentas
	 */
	public void setCuentas(List<CuentaMinimum> cuentas) {
		this.cuentas = cuentas;
	}
	/**
	 * Obtiene cuentas pagadas.<br>
	 * @return pagadas
	 */
	public List<CuentaMinimum> getPagadas() {
		return pagadas;
	}
	/**
	 * Modificar las cuentas pagadas al valor dado por parámetro.<br>
	 * @param pagadas
	 */
	public void setPagadas(List<CuentaMinimum> pagadas) {
		this.pagadas = pagadas;
	}
	
	
	
	
	
}
