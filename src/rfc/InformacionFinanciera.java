package rfc;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Información financiera de un restaurante.
 * @author jc161
 *
 */
public class InformacionFinanciera {
	/**
	 * Id del producto
	 */
	@JsonProperty(value="producto")
	private String  producto;
	/**
	 * Cantidad de cuentas asignadas.
	 */
	@JsonProperty(value="cantidadAsignada")
	private int cantidadAsignada;
	/**
	 * Cantidad de cuentas no asignadas
	 */
	@JsonProperty(value="cantidadNoAsignada")
	private int cantidadNoAsignada;
	/**
	 * Total de ventas asignadas.
	 */
	@JsonProperty(value="totalAsignado")
	private double totalAsignado;
	/**
	 * Total de ventas no asignadas.
	 */
	private double totalNoAsignado;
	/**
	 * Construye un nuevo objeto de información financiera.<br>
	 * @param  producto Id del producto.<br>
	 * @param cantidadAsignada Cantidad asignada.<br>
	 * @param cantidadNoAsignada Cantidad no asignada.<br>
	 * @param totalAsignado Total de mercancía asignada.<br>
	 * @param totalNoAsignado Total de mercancía no asignada.
	 */
	public InformacionFinanciera(@JsonProperty(value="producto")String  producto, 
			@JsonProperty(value="cantidadAsignada")int cantidadAsignada, @JsonProperty(value="cantidadNoAsignada")int cantidadNoAsignada, 
			@JsonProperty(value="totalAsignado")double totalAsignado, @JsonProperty(value="totalNoAsignado")double totalNoAsignado) {
		super();
		this .producto =  producto;
		this.cantidadAsignada = cantidadAsignada;
		this.cantidadNoAsignada = cantidadNoAsignada;
		this.totalAsignado = totalAsignado;
		this.totalNoAsignado = totalNoAsignado;
	}
	/**
	 * Obtiene el  producto del producto.<br>
	 * @return  producto
	 */
	public String getId() {
		return  producto;
	}
	/**
	 * Modifica el  producto al valor dado por parámetro.<br>
	 * @param  producto
	 */
	public void setId(String  producto) {
		this .producto =  producto;
	}
	/**
	 * Obtiene la cantidad asignada.<br>
	 * @return cantidadAsignada
	 */
	public int getCantidadAsignada() {
		return cantidadAsignada;
	}
	/**
	 * Modifica la cantidad asignada al dado por parámetro.<br>
	 * @param cantidadAsignada
	 */
	public void setCantidadAsignada(int cantidadAsignada) {
		this.cantidadAsignada = cantidadAsignada;
	}
	/**
	 * Obtiene la cantidad no asignada.<br>
	 * @return cantidadNoAsignada
	 */
	public int getCantidadNoAsignada() {
		return cantidadNoAsignada;
	}
	/**
	 * Modifica la cantidad no asignada al valor dado por parámetro.<br>
	 * @param cantidadNoAsignada
	 */
	public void setCantidadNoAsignada(int cantidadNoAsignada) {
		this.cantidadNoAsignada = cantidadNoAsignada;
	}
	/**
	 * Obtiene el total asignado.<br>
	 * @return totalAsignado
	 */
	public double getTotalAsignado() {
		return totalAsignado;
	}
	/**
	 * Modifica el valor del total asignado al dado por parámetro.<br>
	 * @param totalAsignado
	 */
	public void setTotalAsignado(double totalAsignado) {
		this.totalAsignado = totalAsignado;
	}
	/**
	 * Obtiene el valor del total no asignado.<br>
	 * @return totalNoAsignado.
	 */
	public double getTotalNoAsignado() {
		return totalNoAsignado;
	}
	/**
	 * Modifica el valor del total no asignado al dado por parámetro.<br>
	 * @param totalNoAsignado
	 */
	public void setTotalNoAsignado(double totalNoAsignado) {
		this.totalNoAsignado = totalNoAsignado;
	}
	
	
	
	
}
