package alt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import rfc.Criterio;
import rfc.CriterioVerdad;
import rfc.CriterioVerdad.PalabrasVerdad;

public class RandomData {
	
	private String alphabet="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	
	public String randomString()
	{
		StringBuilder rta = new StringBuilder();
		while(rta.length()<10)
		{
			int index = (int) (Math.random()*51);
			rta.append(alphabet.charAt(index));
		}
		return "'"+rta.toString()+"'";
	}
	
	public String randomRest(ArrayList<String> restrictions)
	{
		return"'"+(restrictions.get((int)(Math.random()*(restrictions.size()-1))))+"'";
	}
	
	public String randomNumber(ArrayList<String> restrictions)
	{
		return(restrictions.get((int)(Math.random()*(restrictions.size()-1))));
	}
	
	public void writeIngredientToFile(String fileName) throws Exception
	{
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		String std="";
		FileWriter writer = new FileWriter(new File("./Docs/datos/ingrediente.txt"));
		for(int i=0;i<25;i++)
		{
			std="INSERT INTO INGREDIENTE (ID,NOMBRE,DESCRIPCION,TRADUCCION) VALUES (IDINGREDIENTE.NEXTVAL,";
			std+=randomString()+","+randomString()+","+randomString()+");\n";
			fw.write(std);
			std="";
			writer.write((i+1)+"\n");
		}
		fw.close();
		writer.close();
	}
	
	public void writeZona(String fileNameZona) throws Exception
	{
		File f = new File(fileNameZona);
		FileWriter fw = new FileWriter(f);
		FileWriter writer = new FileWriter(new File("./Docs/datos/zonas.txt"));
		String std="";
		ArrayList<String> data= new ArrayList<>();
		String nom="";
		int capacidad=0;
		int capacidadOcupada=0;
		for(int i=0;i<25;i++)
		{
			
			std="INSERT INTO ZONA (NOMBRE,CAPACIDAD,INGRESOESPECIAL,ABIERTAACTUALMENTE, CAPACIDADOCUPADA) VALUES (";
			nom=randomString();
			while(true)
			{
				if(data.contains(nom)) nom=randomString();
				else break;
			}
			data.add(nom);
			
			capacidad=(int)(Math.random()*50);
			if(capacidad>10) capacidadOcupada=capacidad-10;
			else capacidadOcupada=0;
			std+=nom+","+capacidad+","+(int)(Math.random()*100)%2+","+(int)(Math.random()*100)%2+","+capacidadOcupada+");\n";
			fw.write(std);
			std="";
			
			
			nom=nom.replaceAll("'", "");
			writer.write(nom+"\n");
			
		}
		fw.close();
		writer.close();
		
	}
	
	public void writeToZonaCond(String fileNameZonaCond) throws Exception
	{
		ArrayList<String> readFile=readFile("./Docs/datos/zonas.txt");
		ArrayList<String> readCon=readFile("./Docs/datos/cond.txt");
		ArrayList<String> agregados=null;
		String temp=null;
		int limite=0;
		FileWriter fw2= new FileWriter(new File(fileNameZonaCond));
		for(int i=0;i<readFile.size();i++)
		{
			limite=(int)(Math.random()*5);
			agregados= new ArrayList<>();
			for(int j=0;j<limite;j++)
			{
				temp=randomRest(readCon);
				while(true)
				{
					if(agregados.contains(temp)) temp=randomRest(readCon);
					else break;
				}
				agregados.add(temp);
				fw2.write("INSERT INTO CONDICIONZONA VALUES ("+temp+",'"+readFile.get(i)+"');\n");
			}
		}
		fw2.close();
		
	}
	
	private ArrayList<String> readFile(String fileName) throws Exception {
		File f = new File(fileName);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> conditions= new ArrayList<>();
		String l = br.readLine();
		while(l!=null)
		{
			conditions.add(l);
			l=br.readLine();
		}
		br.close();
		fr.close();
		return conditions;
	}

	public void writeProductoToFile(String fileName) throws Exception
	{
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		FileWriter writer = new FileWriter(new File("./Docs/datos/producto.txt"));
		String std="";
		ArrayList<String> tipos = tiposToString();
		double precio=0;
		double costo=0;
		for(int i=0;i<25;i++)
		{
			precio=Math.random()*1000;
			if(precio>100) costo=precio-100; else costo=0;
			std="INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE,PRECIO,DESCRIPCION,TRADUCCION,COSTOPRODUCCION,TIEMPO) "
					+ "VALUES (IDPRODUCTO.NEXTVAL,";
			std+=randomString()+","+randomRest(tipos)+","+(int)(Math.random()*100)%2+","+precio+","+randomString()+","+randomString()+","+costo+","+(Math.random()*100)+");\n";
			fw.write(std);
			std="";
			writer.write((i+1)+"\n");
		}
		fw.close();
		writer.close();
	}
	
	public void writeUsuario(String fileName) throws Exception
	{
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		FileWriter writer = new FileWriter(new File("./Docs/datos/usuario.txt"));
		String std="";
		ArrayList<String> roles = rolesToString();
		for(int i=0;i<50;i++)
		{
			std="INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,";
			std+=randomString()+","+randomString()+","+randomRest(roles)+");";
			fw.write(std+"\n");
			writer.write((i+1)+"\n");
		}
		writer.close();
		fw.close();
	}
	
	public void writePreferencia(String fileName) throws Exception
	{
		ArrayList<String> usuarios = readFile("./Docs/datos/usuario.txt");
		ArrayList<String> usados= new ArrayList<>();
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		FileWriter writer=new FileWriter(new File("./Docs/datos/preferencias.txt"));
		int index=0;
		String std="";
		String id="";
		double inic=0;
		for(int i=0;i<20;i++)
		{
			std="";
			index=(int)(Math.random()*(usuarios.size()-1));
			id=usuarios.get(index);
			while(true)
			{
				if(usados.contains(id)) id=usuarios.get((int)(Math.random()*(usuarios.size()-1)));
				else break;
			}
			usados.add(id);
			inic=(Math.random()*100);
			std="INSERT INTO PREFERENCIA (IDUSUARIO,PRECIOINICIAL,PRECIOFINAL) VALUES ("+id;
			std+=","+inic+","+(inic+100)+");\n";
			fw.write(std);
			writer.write(id+"\n");
		}
		fw.close();
		writer.close();
	}
	
	public void writeToPreferenciaZona(String fileName) throws Exception
	{
		ArrayList<String> pref=readFile("./Docs/datos/preferencias.txt");
		ArrayList<String> zonas=readFile("./Docs/datos/zonas.txt");
		FileWriter fw = new FileWriter(new File(fileName));
		int limite=0;
		String std=null;
		String temp=null;
		ArrayList<String> agregados=null;
		ArrayList<String> ids= new ArrayList<String>();
		for(int i=0;i<pref.size();i++)
		{
			limite=(int)(Math.random()*5);
			if(ids.contains(pref.get(i))) continue;
			ids.add(pref.get(i));
			agregados= new ArrayList<>();
			for(int j=0;j<limite;j++)
			{
				std="";
				temp=randomRest(zonas);
				while(true)
				{
					if(agregados.contains(temp)) temp=randomRest(zonas);
					else break;
				}
				agregados.add(temp);
				std="INSERT INTO PREFERENCIAZONA VALUES ("+pref.get(i)+","+temp+");\n";
				fw.write(std);
			}
		}
		fw.close();
	}
	
	public void writeToPreferenciaCategoria(String fileName) throws Exception
	{
		ArrayList<String> pref=readFile("./Docs/datos/preferencias.txt");
		ArrayList<String> zonas=readFile("./Docs/datos/categoria.txt");
		FileWriter fw = new FileWriter(new File(fileName));
		int limite=0;
		String std=null;
		String temp=null;
		ArrayList<String> agregados = new ArrayList<>();
		ArrayList<String> ids=new ArrayList<>();
		for(int i=0;i<pref.size();i++)
		{
			limite=(int)(Math.random()*5);
			agregados= new ArrayList<>();
			if(ids.contains(pref.get(i))) continue;
			ids.add(pref.get(i));
			for(int j=0;j<limite;j++)
			{
				std="";
				temp=randomRest(zonas);
				while(true)
				{
					if(agregados.contains(temp)) temp=randomRest(zonas);
					else break;
				}
				agregados.add(temp);
				std="INSERT INTO PREFERENCIACATEGORIA VALUES ("+pref.get(i)+","+temp+");\n";
				fw.write(std);
			}
		}
		fw.close();
	}
	//Hay que buscar valores con pedidoMenu y pedidoProd para el valor
	public void writeToCuenta(String fileName) throws Exception
	{
		ArrayList<String> usuarios=readFile("./Docs/datos/usuario.txt");
		FileWriter fw = new FileWriter(new File(fileName));
		FileWriter writer = new FileWriter(new File("./Docs/datos/cuentas.txt"));
		String std=null;
		for(int i=1;i<=100;i++)
		{
			std="";
			std="INSERT INTO CUENTA (VALOR,NUMEROCUENTA,FECHA,IDUSUARIO) VALUES (";
			std+=(Math.random()*300)+","+" 'NUMCUENTA.NEXTVAL' ,"+"TO_DATE('"+randomDate()+"','dd/mm/yyyy hh24:mi:ss'),"+randomNumber(usuarios)+");";
			fw.write(std+"\n");
			writer.write(i+"\n");
		}
		fw.close();
		writer.close();
	}
	
	private String randomDate() {
		int month =(int)(Math.random()*11)+1;
		int day=(int)(Math.random()*27)+1;
		int year=(int)(Math.random()*20)+1997;
		int min=(int) (Math.random()*59);
		int sec=(int)(Math.random()*59);
		int hour=(int)(Math.random()*23);
		//dd/mm/yyyy hh24:mi:ss
		return day+"/"+month+"/"+year+" "+hour+":"+min+":"+sec ;
	}

	private ArrayList<String> rolesToString()
	{
		ArrayList<String> s = new ArrayList<>();
		s.add("LOCAL");s.add("CLIENTE");s.add("OPERADOR");s.add("PROVEEDOR");
		return s;
	}
	
	
	private ArrayList<String> tiposToString() {
		ArrayList<String> s = new ArrayList<>();
		s.add("ENTRADA");s.add("PLATO FUERTE");s.add("ACOMPAÑAMIENTO");s.add("POSTRE");
		return s;
	}

	public static void main(String[] args) throws Exception {
		
		//CICLO A MODIFICAR PARA ESCRIBIR LÍNEAS
		RandomData r = new RandomData();
		r.writeIngredientToFile("./Docs/pruebas/ing.txt");
		r.writeProductoToFile("./Docs/pruebas/producto.txt");
		r.writeZona("./Docs/pruebas/zona.txt");
		r.writeToZonaCond("./Docs/pruebas/condZona.txt");
		r.writeUsuario("./Docs/pruebas/usuario.txt");
		r.writePreferencia("./Docs/pruebas/preferencias.txt");
		r.writeToPreferenciaCategoria("./Docs/pruebas/preferenciaCategoria.txt");
		r.writeToPreferenciaZona("./Docs/pruebas/preferenciaZona.txt");
		r.writeToCuenta("./Docs/pruebas/cuentas.txt");
	}
		 
		
}
