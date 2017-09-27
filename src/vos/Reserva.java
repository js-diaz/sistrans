package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Reserva para un evento en RotondAndes. Representaci�n Detail.
 * @author JuanSebastian
 */
public class Reserva extends ReservaMinimum {
	
	
	/**
	 * Usuario que hizo la reserva.
	 */
	@JsonProperty(value="reservador")
	private UsuarioMinimum reservador;
	
	/**
	 * Zona a reservar.
	 */
	@JsonProperty(value="zona")
	private ZonaMinimum zona;
	
	/**
	 * Men� que se servir� en el evento. 
	 * Este atributo es opcional, y ser� null si no se requiere uno.
	 */
	@JsonProperty(value="menu")
	private MenuMinimum menu;
	
	
	
	public Reserva(@JsonProperty(value="fecha") Date fecha, @JsonProperty(value="personas")Integer personas,
			@JsonProperty(value="reservador") UsuarioMinimum reservador, @JsonProperty(value="zona") ZonaMinimum zona,
			@JsonProperty(value="menu") MenuMinimum menu) {
		super(fecha, personas);
		this.reservador = reservador;
		this.zona = zona;
		this.menu = menu;
	}

	public UsuarioMinimum getReservador() {
		return reservador;
	}

	public void setReservador(UsuarioMinimum reservador) {
		this.reservador = reservador;
	}

	public ZonaMinimum getZona() {
		return zona;
	}

	public void setZona(ZonaMinimum zona) {
		this.zona = zona;
	}

	public MenuMinimum getMenu() {
		return menu;
	}

	public void setMenu(MenuMinimum menu) {
		this.menu = menu;
	}
	
}
