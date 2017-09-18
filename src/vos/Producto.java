package vos;
/**
 * Clase que modela un producto de RotondAndes
 * @author jc161
 *
 */

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Producto {

	/**
	 * Enumeración de los tipos que puede tener un producto
	 * @author jc161
	 *
	 */
	public enum Tipos_De_Plato{
		POSTRE,BEBIDA,PLATO_FUERTE,
		ACOMPANAMIENTO,ENTRADA;
	}
	/**
	 * Determina si el producto es personalizable o no.
	 */
	@JsonProperty(value="personalizable")
	private boolean personalizable;
	/**
	 * Nombre del producto.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Precio del producto
	 */
	@JsonProperty(value="precio")
	private double precio;
	/**
	 * Tipo del producto
	 */
	@JsonProperty(value="tipo")
	private Tipos_De_Plato tipo;
	/**
	 * Descripcion del producto.
	 */
	@JsonProperty(value="descripcion")
	private String descripcion;
	/**
	 * Traducción del producto.
	 */
	@JsonProperty(value="traduccion")
	private String traduccion;
	/**
	 * Tiempo de preparación del producto.
	 */
	@JsonProperty(value="tiempo")
	private double tiempo;
	/**
	 * Costo de producción del plato
	 */
	@JsonProperty(value="costoProduccion")
	private double costoProduccion;
	/**
	 * Identificación del producto
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Listado de ingredientes del producto.
	 */
	@JsonProperty(value="ingredientes")
	private List<Ingrediente> ingredientes;
	/**
	 * Listado de categorías del producto
	 */
	@JsonProperty(value="categorias")
	private List<Categoria> categorias;
	/**
	 * Construye un nuevo producto con los paráemtros dados.<br>
	 * @param personalizable<br>
	 * @param nombre<br>
	 * @param prcio<br>
	 * @param tipo<br>
	 * @param descripcion<br>
	 * @param traduccion<br<
	 * @param tiempo<br>
	 * @param costoProduccion<br>
	 * @param id <br>
	 */
	public Producto(@JsonProperty(value="personalizable")boolean personalizable, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="precio") double prcio, @JsonProperty(value="tipo")Tipos_De_Plato tipo, @JsonProperty(value="descripcion")String descripcion,
			@JsonProperty(value="traduccion")String traduccion, @JsonProperty(value="tiempo")double tiempo, @JsonProperty(value="costoProduccion")double costoProduccion, @JsonProperty(value="id")Long id
			,@JsonProperty(value="ingredientes") List<Ingrediente> ingredientes, @JsonProperty(value="categorias")List<Categoria> categorias) {
		super();
		this.personalizable = personalizable;
		this.nombre = nombre;
		this.precio = prcio;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.traduccion = traduccion;
		this.tiempo = tiempo;
		this.costoProduccion = costoProduccion;
		this.id = id;
		this.ingredientes=ingredientes;
		this.categorias=categorias;
	}
	/**
	 * Permite ver si es personalizable o no.<br>
	 * @return personalizable.
	 */
	public boolean isPersonalizable() {
		return personalizable;
	}
	/**
	 * Modifica el valor de personalización.<br>
	 * @param personalizable
	 */
	public void setPersonalizable(boolean personalizable) {
		this.personalizable = personalizable;
	}
	/**
	 * Obtiene el nombre del producto.<br>
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre del producto al dado por parámetro.<br>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene el precio del producto.<br>
	 * @return precio
	 */
	public double getPrecio() {
		return precio;
	}
	/**
	 * Modifica el precio del producto al dado por parámetro.<br>
	 * @param precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	/**
	 * Obtiene el tipo de plato.<br>
	 * @return tipo
	 */
	public Tipos_De_Plato getTipo() {
		return tipo;
	}
	/**
	 * Modifica el tipo de plato al dado por parámetro.<br>
	 * @param tipo
	 */
	public void setTipo(Tipos_De_Plato tipo) {
		this.tipo = tipo;
	}
	/**
	 * Obtiene la descripción del producto.<br>
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * Modifica la descripción a aquella dada por parámetro.<br>
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * Obtiene la traducción de la descripción.<br>
	 * @return traduccion
	 */
	public String getTraduccion() {
		return traduccion;
	}
	/**
	 * Modifica la traducción a aquella dada por parámetro.<br>
	 * @param traduccion
	 */
	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}
	/**
	 * Obtiene el tiempo de preparación del plato.<br>
	 * @return tiempo
	 */
	public double getTiempo() {
		return tiempo;
	}
	/**
	 * Moficia el tiempo a aquel dado por parámetro.<br>
	 * @param tiempo
	 */
	public void setTiempo(double tiempo) {
		this.tiempo = tiempo;
	}
	/**
	 * Obtiene el costo de producción.<br>
	 * @return costoProduccion
	 */
	public double getCostoProduccion() {
		return costoProduccion;
	}
	/**
	 * Modifica el costo de producción al dado por parámetro.<br>
	 * @param costoProduccion
	 */
	public void setCostoProduccion(double costoProduccion) {
		this.costoProduccion = costoProduccion;
	}
	/**
	 * Obtiene el id.<br>
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Modifica el id al dado por parámetro.<br>
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Obtiene el listado de ingredientes del producto.<br>
	 * @return ingredientes.
	 */
	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}
	/**
	 * Modifica el listado de ingredientes al dado por parámetro.<br>
	 * @param ingredientes
	 */
	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
	/**
	 * Obtiene el listado de categortias.<br>
	 * @return categorias
	 */
	public List<Categoria> getCategorias() {
		return categorias;
	}
	/**
	 * Modifica las categorias a las dadas por parámetro.<br>
	 * @param categorias
	 */
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	/**
	 * Agrega un nuevo ingrediente a la lista.<br>
	 * @param i Ingrediente
	 */
	public void agregarIngrediente(Ingrediente i)
	{
		ingredientes.add(i);
	}
	/**
	 * Remueve un ingrediente de la lista.<br>
	 * @param i Ingrediente a remover
	 */
	public void removerIngrediente(Ingrediente i)
	{
		ingredientes.remove(i);
		
	}
	/**
	 * Agrega una nueva categoría a la lista.<br>
	 * @param c Categoria a usar.
	 */
	public void agregarCategoria(Categoria c)
	{
		categorias.add(c);
		
	}
	/**
	 * Remover una categoría de la lista.<br>
	 * @param c Categoría a usar.
	 */
	public void removerCategoria(Categoria c)
	{
		categorias.remove(c);
	}
	/**
	 * Remplaza uno de los ingredientes dados y retorna una nueva instancia del producto.<br>
	 * @param ingredienteActual Nombre del actual.<br>
	 * @param ingredienteNuevo Nombre del ingrediente nuevo.
	 */
	public Producto cambiarProducto(String ingredienteActual, String ingredienteNuevo)
	{
		//TODO
		return null;
	}
	/**
	 * Personaliza el producto retornando uno nuevo como resultado.<br>
	 * @param ingredientes Lista de ingredientes para personalizar.
	 */
	public Producto personalizarPlato(List<Ingrediente> ingredientes)
	{
		//TODO
		return null;
	}
	
	
	
	
	
	
}
