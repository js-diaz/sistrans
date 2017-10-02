package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un menú ofrecido por algún restaurante. Representación Detail.
 * @author JuanSebastian
 */

public class Menu extends MenuMinimum{

	
	/**
	 * Lista de platos que componen el menú.
	 */
	@JsonProperty(value="platos")
	private List<InfoProdRest> platos;
	
	/**
	 * Categorias a las cuales pertenece el menú.
	 */
	@JsonProperty(value="categorias")
	private List<Categoria> categorias;

	public Menu(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="precio") Double precio, 
			@JsonProperty(value="costo") Double costo, @JsonProperty(value="restaurante") RestauranteMinimum restaurante,
			@JsonProperty(value="platos") List<InfoProdRest> platos, @JsonProperty(value="categorias") List<Categoria> categorias) {
		super(nombre, precio, costo, restaurante);
		this.platos = platos;
		this.categorias = categorias;
	}

	public List<InfoProdRest> getPlatos() {
		return platos;
	}

	public void setPlatos(List<InfoProdRest> platos) {
		this.platos = platos;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
	
}
