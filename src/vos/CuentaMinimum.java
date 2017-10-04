package vos;

import java.util.Date;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela la cuenta de una persona.
 * @author jc161
 *
 */
public class CuentaMinimum {
	/**
	 * Valor total de la cuenta
	 */
	@JsonProperty(value="valor")
	protected double valor;
	/**
	 * Número de la cuenta
	 */
	@JsonProperty (value="numeroCuenta")
	private String numeroCuenta;
	/**
	 * Fecha de creación
	 */
	@JsonProperty (value="fecha")
	private Date fecha;
	
	
	/**
	 * Constructor de la cuenta.<br>
	 * @param pedidoProd Lista de pedidos de productos.<br>
	 * @param pedidoMenu Lista de pedidos de menús.<br>
	 * @param valor Valor de la cuenta.<br>
	 * @param numeroCuenta Número de la cuenta. <br>
	 * @param fecha Fecha de la cuenta.<br>
	 * @param cliente Cliente al que se le factura.
	 */
	public CuentaMinimum( @JsonProperty(value="valor")double valor, @JsonProperty(value="numeroCuenta")String numeroCuenta,
			@JsonProperty(value="fecha")Date fecha) {
		super();
		this.valor = valor;
		this.numeroCuenta = numeroCuenta;
		this.fecha = fecha;
	}
	
	/**
	 * Obtiene el valor de la cuenta.<br>
	 * @return valor
	 */
	public double getValor() {
		return valor;
	}
	/**
	 * Modifica el valor de la cuenta al dado por parámetro.<br>
	 * @param valor
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}
	/**
	 * Obtiene el número de cuenta.<br>
	 * @return numeroCuenta
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * Cambia el número de cuenta al dado por parámetro.<br>
	 * @param numeroCuenta
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	/**
	 * Obtiene la fecha de creación de la cuenta.<br>
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * Cambia la fecha a la dada por parámetro.<br>
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	
}
