package rfc;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Contenedora de la información de mesa menú cliente.<br>
 * @author jc161
 *
 */
public class ContenedoraMesaMenuCliente {

	/**
	 * Tiene o no mesa
	 */
	@JsonProperty(value="tieneMesa")
	private boolean tieneMesa;
	/**
	 * Contenedora menuCliente.
	 */
	@JsonProperty(value="informacionMenu")
	private List<ContenedoraMenuCliente> informacionMenu;
	/**
	 * Crea una nueva contenedroa.<br>
	 * @param tieneMesa Si tiene o no una mesa.<br>
	 * @param informacionMenu Información del menú.
	 */
	public ContenedoraMesaMenuCliente(@JsonProperty(value="tieneMesa")boolean tieneMesa, 
			@JsonProperty(value="informacionMenu")List<ContenedoraMenuCliente> informacionMenu) {
		super();
		this.tieneMesa = tieneMesa;
		this.informacionMenu = informacionMenu;
	}
	/**
	 * Si tiene o no una mesa.<br>
	 * @return tieneMesa
	 */
	public boolean isTieneMesa() {
		return tieneMesa;
	}
	/**
	 * Modifica el valor de la posesión de la mesa al dado por parámetro.<br>
	 * @param tieneMesa
	 */
	public void setTieneMesa(boolean tieneMesa) {
		this.tieneMesa = tieneMesa;
	}
	/**
	 * Obtiene la información de menú.<br>
	 * @return informacionMenu
	 */
	public List<ContenedoraMenuCliente> getInformacionMenu() {
		return informacionMenu;
	}
	/**
	 * Modifica la información del menú al valor dado por parámetro.<br>
	 * @param informacionMenu
	 */
	public void setInformacionMenu(List<ContenedoraMenuCliente> informacionMenu) {
		this.informacionMenu = informacionMenu;
	}
	
	
}
