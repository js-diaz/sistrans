package rfc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.CuentaMinimum;
import vos.Preferencia;
import vos.RestauranteMinimum;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que modela a un usuario del sistema.
 * @author jc161
 *
 */
public class UsuarioCompleto extends Usuario {
	
	/**
	 * Atributo para las frecuencias
	 */
	private String[] frecuencias= new String[7];
	/**
	 * Enumeración de los días
	 */
	public enum Dia{
		LUNES,MARTES,MIERCOLES,JUEVES,VIERNES,SABADO,DOMINGO
	}
	/**
	 * Arreglo de constantes de los días
	 */
	private Dia[] dias= new Dia[]{Dia.LUNES,Dia.MARTES,Dia.MIERCOLES,
									Dia.JUEVES,Dia.VIERNES,Dia.SABADO,
									Dia.DOMINGO};
	/**
	 * Constructor del usuario completo.<br>
	 * @param nombre Nombre del usuario.<br>
	 * @param id Id del usuario.<br>
	 * @param correo Correo del usuario.<br>
	 * @param rol Rol del usuario.<br>
	 * @param preferencia Preferencia del usuario.<br>
	 * @param historial Historial de compras.<br>
	 * @param restaurante2 Restaurante del que es dueño.<br>
	 * @param frecuencias Frecuencia de compras por dia de semana.
	 */
	public UsuarioCompleto(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="id")Long id, @JsonProperty(value="correo")String correo, @JsonProperty(value="rol")Rol rol, @JsonProperty(value="preferencia")Preferencia preferencia, @JsonProperty(value="historial")List<CuentaMinimum> historial,
			@JsonProperty(value="restaurante")RestauranteMinimum restaurante2,@JsonProperty("frecuencia") int[] frecuencias)
	{
		super(nombre, id, correo, rol, preferencia, historial, restaurante2);
		if(frecuencias.length==7)
			for(int i=0;i<7;i++)
			{
				this.frecuencias[i]=dias[i]+": "+frecuencias[i];
			}
	}
	/**
	 * Obtiene las frecuencias del usuario.<br>
	 * @return frecuencias
	 */
	public String[] getFrecuencias() {
		return frecuencias;
	}
	/**
	 * Modifica el valor de las frecuencias al valor dado por parámetro.<br>
	 * @param frecuencias Frecuencias
	 */
	public void setFrecuencias(String[] frecuencias) {
		this.frecuencias = frecuencias;
	}
	
	
	
}
