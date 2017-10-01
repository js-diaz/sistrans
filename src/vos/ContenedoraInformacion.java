package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ContenedoraInformacion {
	
	@JsonProperty(value="contenido")
	private List<Informacion> informacion;
	

	public ContenedoraInformacion(String[] nombres)
	{
		informacion=new ArrayList<>();
		for(String v:nombres)
			informacion.add(new Informacion(v,""));
	}
	
	public void modificarInformacion(int i, String valor)
	{
		informacion.get(i).setValor(valor);
	}
	
	
	
	public List<Informacion> getInformacion() {
		return informacion;
	}

	public void setInformacion(List<Informacion> informacion) {
		this.informacion = informacion;
	}



	private class Informacion{
		@JsonProperty(value="nombre")
		private String nombre;
		
		
		@JsonProperty(value="valor")
		private String valor;
		
		public Informacion(@JsonProperty(value="nombre") String nombre,
				@JsonProperty(value="valor") String valor)
		{
			this.nombre=nombre;
			this.valor=valor;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}
		
		
	}
	
	public String toString()
	{
		String msj="";
		for(Informacion i:informacion)
		{
			msj+=i.getNombre()+":"+i.getValor()+"\n";
		}
		return msj;
	}

	
}
