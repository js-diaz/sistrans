package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Restaurante que se encuentra en RotondAndes. Representación Detail.
 * @author JuanSebastian
 */
public class Restaurante extends RestauranteMinimum
{
	/**
	 * Zona en la que se encuentra el restaurante, en representación Minimum.
	 */
	@JsonProperty(value="zona")
	private ZonaMinimum zona;
	
	/**
	 * Categorias a las que pertenece el restaurante.
	 */
	@JsonProperty(value="categorias")
	private List<Categoria> categorias;
	
	/**
	 * Usuario representante del restaurante, en representación Minimum.
	 */
	@JsonProperty(value="representante")
	private UsuarioMinimum representante;
	
	/**
	 * Información de los productos que vende este restaurante, en representación Minimum.
	 */
	@JsonProperty(value="infoProductos")
	private List<InfoProdRestMinimum> infoProductos;
	
	/**
	 * Informacion de los ingredientes que usa este restaurante, en representación Minimum.
	 */
	@JsonProperty(value="infoIngredientes")
	private List<InfoIngRestMinimum> infoIngredientes;
	
	/**
	 * Menus que vende este restaurante, en representación Minimum.
	 */
	@JsonProperty(value="menus")
	private List<MenuMinimum> menus;

	public Restaurante(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="pagWeb")String pagWeb,
			@JsonProperty(value="zona") ZonaMinimum zona, @JsonProperty(value="categorias") List<Categoria> categorias,
			@JsonProperty(value="representante") UsuarioMinimum representante, @JsonProperty(value="infoProductos") List<InfoProdRestMinimum> infoProductos,
			@JsonProperty(value="infoIngredientes") List<InfoIngRestMinimum> infoIngredientes, @JsonProperty(value="menus") List<MenuMinimum> menus) {
		super(nombre, pagWeb);
		this.zona = zona;
		this.categorias = categorias;
		this.representante = representante;
		this.infoProductos = infoProductos;
		this.infoIngredientes = infoIngredientes;
		this.menus = menus;
	}

	public ZonaMinimum getZona() {
		return zona;
	}

	public void setZona(ZonaMinimum zona) {
		this.zona = zona;
	}
	
	public List<Categoria> getCategorias() {
		return categorias;
	}
	
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public UsuarioMinimum getRepresentante() {
		return representante;
	}

	public void setRepresentante(UsuarioMinimum representante) {
		this.representante = representante;
	}

	public List<InfoProdRestMinimum> getInfoProductos() {
		return infoProductos;
	}

	public void setInfoProductos(List<InfoProdRestMinimum> infoProductos) {
		this.infoProductos = infoProductos;
	}

	public List<InfoIngRestMinimum> getInfoIngredientes() {
		return infoIngredientes;
	}

	public void setInfoIngredientes(List<InfoIngRestMinimum> infoIngredientes) {
		this.infoIngredientes = infoIngredientes;
	}

	public List<MenuMinimum> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuMinimum> menus) {
		this.menus = menus;
	}
	
	
	
}
