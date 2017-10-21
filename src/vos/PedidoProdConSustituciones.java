package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Pedido que ha hecho un cliente de un menu, con sustituciones en él.
 * @author JuanSebastian
 */
public class PedidoProdConSustituciones extends PedidoProd {

	/**
	 * Sustituciones de un ingrediente por otro.
	 */
	@JsonProperty(value="sustituciones") 
	private List<SustitucionIngrediente> sustituciones;
	
	public PedidoProdConSustituciones(@JsonProperty(value="cantidad") Integer cantidad, @JsonProperty(value="cuenta") CuentaMinimum cuenta,
			@JsonProperty(value="plato") InfoProdRest plato, @JsonProperty(value="entregado") Boolean entregado,
			@JsonProperty(value="sustituciones") List<SustitucionIngrediente> sustituciones) {
		super(cantidad, cuenta, plato, entregado);
		this.sustituciones = sustituciones;
	}

	public List<SustitucionIngrediente> getSustituciones() {
		return sustituciones;
	}

	public void setSustituciones(List<SustitucionIngrediente> sustituciones) {
		this.sustituciones = sustituciones;
	}
	
}
