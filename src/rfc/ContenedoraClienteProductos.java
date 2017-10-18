package rfc;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Contenedora de la información de un cliente y los productos que consume.<br>
 * @author jc161
 *
 */
public class ContenedoraClienteProductos {
	/**
	 * Id del cliente
	 */
	@JsonProperty(value="id")
	private Long id;
	/**
	 * Contenedora mesaMenuCliente
	 */
	@JsonProperty(value="informacionMesaMenu")
	private List<ContenedoraMesaMenuCliente> informacionMesaMenu;
	/**
	 * Construye una nueva contenedora.<br>
	 * @param id Id del cliente.<br>
	 * @param informacionMesaMenu Contenedora de informacion
	 */
	public ContenedoraClienteProductos(@JsonProperty(value="id")Long id, @JsonProperty(value="informacionMesaMenu")List<ContenedoraMesaMenuCliente> informacionMesaMenu) {
		super();
		this.id = id;
		this.informacionMesaMenu = informacionMesaMenu;
	}
	/**
	 * Obtiene el id de cliente.<br>
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Modifica el di al valor dado por parámetro.<br>
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Obtiene la contenedora.<br>
	 * @return informacionMesaMenu
	 */
	public List<ContenedoraMesaMenuCliente> getInformacionMesaMenu() {
		return informacionMesaMenu;
	}
	/**
	 * Modifica el valor de la contenedora al dado por parámetro.<br>
	 * @param informacionMesaMenu
	 */
	public void setInformacionMesaMenu(List<ContenedoraMesaMenuCliente> infomracionMesaMenu) {
		this.informacionMesaMenu = infomracionMesaMenu;
	}
	
	
}
