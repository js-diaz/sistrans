package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Información de un producto que depende del restaurante que lo vende. Representación Detail.
 * @author JuanSebastian
 */
public class InfoProdRest extends InfoProdRestMinimum {

	/**
	 * Producto que se está describiendo.
	 */
	@JsonProperty(value="producto")
	private ProductoMinimum producto;
	
	/**
	 * Restaurante que contiene el producto.
	 */
	@JsonProperty(value="restaurante")
	private RestauranteMinimum restaurante;

	public InfoProdRest(@JsonProperty(value="costo") Double costo, @JsonProperty(value="precio") Double precio,
			@JsonProperty(value="disponibilidad") Integer disponibilidad, @JsonProperty(value="producto") ProductoMinimum producto,
			@JsonProperty(value="restaurante") RestauranteMinimum restaurante) {
		super(costo, precio, disponibilidad);
		this.producto = producto;
		this.restaurante = restaurante;
	}

	public ProductoMinimum getProducto() {
		return producto;
	}

	public void setProducto(ProductoMinimum producto) {
		this.producto = producto;
	}

	public RestauranteMinimum getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteMinimum restaurante) {
		this.restaurante = restaurante;
	}
	
	
}
