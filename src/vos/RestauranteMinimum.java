package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Restaurante que se encuentra en RotondAndes. Representación Minimum.
 * @author JuanSebastian
 */

public class RestauranteMinimum {
	
	/**
	 * Nombre del restaurante.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * URL de la página web del restaurante.
	 */
	@JsonProperty(value="pagWeb")
	private String pagWeb;

	/**
	 * Booleano que indica si el restaurante se encuentra activo o no.
	 */
	@JsonProperty(value="activo")
	private Boolean activo;
	
	
	public RestauranteMinimum(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="pagWeb") String pagWeb,
			@JsonProperty(value="activo") Boolean activo) {
		this.nombre = nombre;
		this.pagWeb = pagWeb;
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPagWeb() {
		return pagWeb;
	}
	
	public void setPagWeb(String pagWeb) {
		this.pagWeb = pagWeb;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestauranteMinimum other = (RestauranteMinimum) obj;
		if (activo == null) {
			if (other.activo != null)
				return false;
		} else if (!activo.equals(other.activo))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (pagWeb == null) {
			if (other.pagWeb != null)
				return false;
		} else if (!pagWeb.equals(other.pagWeb))
			return false;
		return true;
	}

	
	
}
