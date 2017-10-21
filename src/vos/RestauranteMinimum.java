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
	
	public RestauranteMinimum(@JsonProperty(value="nombre") String nombre, @JsonProperty(value="pagWeb") String pagWeb) {
		this.nombre = nombre;
		this.pagWeb = pagWeb;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestauranteMinimum other = (RestauranteMinimum) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	
}
