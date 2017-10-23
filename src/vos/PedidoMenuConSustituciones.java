package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Pedido que ha hecho un cliente de un menu, con sustituciones en él. Representación Detail
 * @author JuanSebastian
 */
public class PedidoMenuConSustituciones extends PedidoMenu {

	/**
	 * Sustituciones de un producto por otro.
	 */
	@JsonProperty(value="sustitucionesProducto")
	private List<SustitucionProducto> sustitucionesProducto;
	
	/**
	 * Sustituciones de un ingrediente por otro en un producto.
	 */
	@JsonProperty(value="sustitucionesIngrediente") 
	private List<SustitucionIngredienteEnProducto> sustitucionesIngrediente;
	
	public PedidoMenuConSustituciones(@JsonProperty(value="cantidad") Integer cantidad, @JsonProperty(value="cuenta") CuentaMinimum cuenta,
			@JsonProperty(value="menu") MenuMinimum menu, @JsonProperty(value="entregado") Boolean entregado,
			@JsonProperty(value="sustitucionesProducto") List<SustitucionProducto> sustitucionesProducto, @JsonProperty(value="sustitucionesIngrediente") List<SustitucionIngredienteEnProducto> sustitucionesIngrediente) {
		super(cantidad, cuenta, menu, entregado);
		this.sustitucionesIngrediente = sustitucionesIngrediente;
		this.sustitucionesProducto = sustitucionesProducto;
	}

	public List<SustitucionProducto> getSustitucionesProducto() {
		return sustitucionesProducto;
	}

	public void setSustitucionesProducto(List<SustitucionProducto> sustitucionesProducto) {
		this.sustitucionesProducto = sustitucionesProducto;
	}

	public List<SustitucionIngredienteEnProducto> getSustitucionesIngrediente() {
		return sustitucionesIngrediente;
	}

	public void setSustitucionesIngrediente(List<SustitucionIngredienteEnProducto> sustitucionesIngrediente) {
		this.sustitucionesIngrediente = sustitucionesIngrediente;
	}
	
	

}
