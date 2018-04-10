package crawling;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NodoPagina {
	//Cantidad de nodos hijos que se poseen
	private int CantNodos = 0;
	
	//Sitio web actual en el nodo
	private Document website;
	
	//Arreglo de elementos que la página puede tener
	private Elements librerias; //Librerías utilizadas
	private Elements links; //Vínculos utilizados en página
	private Elements medios; //Recursos de medios usados
	
	//Directorios en donde se alojarán la información de los links
	private String dirPadre;
	private String dirCache;
	
	//Nodos hijos del nodo actual
	private ArrayList <NodoPagina> hijos;
		
	public NodoPagina(String link, String dirPadre, String dirCache) throws IOException {
		/*1. Se conecta al nodo pedido
		 *2. Vuelca los recursos requeridos del nodo
		 *3. Genera el documento actual
		 *4. Si links.lenght es mayor que cero, por cada link
		 *4.1. Crea el directorio hijo donde se alojarán los nodos hijo
		 *4.1. Crea un Nodo nuevo por cada link hijo
		 *4. Si no
		 *4.2. Hace nada*/
		//1
		this.website = Jsoup.connect(link).get();	
		//2
		this.links = this.website.select("a[href]");
		this.librerias = this.website.select("links[href]");
		this.medios = this.website.select("[src]");
		this.dirPadre = dirPadre + website.title() + File.separator;
		this.dirCache = dirCache + File.separator;
		
		//this.createDirPadre();
		this.createParsedDoc();
		this.createChildsNodes();
	}
	
	private Boolean createParsedDoc() throws IOException {
		Boolean resultado = false;
		File salida = new File(dirPadre + File.separator + website.title() + ".txt");
		FileOutputStream content = new FileOutputStream(salida);
		salida.canWrite();
		
		content.write(
				this.website.select(".container").text().getBytes(Charset.forName("UTF-8"))
		);
		
		if(salida.createNewFile()) resultado = true; 
		
		//System.out.println(salida.getCanonicalPath());
		content.close();
		
		return resultado;
	}
	
	private void createDirSons(String sondir){
		File salida = new File(dirPadre + File.separator + sondir);
		salida.getParentFile().mkdirs();
	}
	
	private void createChildsNodes() {
		try {
			if(!this.links.isEmpty()) {
				for(Element link: this.links) {
					if(this.CantNodos == 0) createDirSons(link.text() + "_childs");
					NodoPagina aux = new NodoPagina(link.attr("abs:href"), this.dirPadre + File.separator + link.text() + "_childs" + File.separator, this.dirCache);
					
					if(this.writeToCache(aux.website.text())){
						this.hijos.add(aux);
						System.out.println("Enlace " + link.attr("abs:href") + " agregado exitosamente");
					}
					else System.out.println("Enlace " + link.attr("abs:href") + " no agregado a hijos, se encuentra en caché");
					
		        	//System.out.println("text: " + link.text() + "\n");
		        	
		        }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean writeToCache(String link) {
		File cache = new File(this.dirCache + "vispags.txt");
		Boolean resultado = false;
		try {
			Scanner rc = new Scanner(this.dirCache + "vispags.txt");
			FileOutputStream wc = new FileOutputStream(cache);
			Boolean cont = true;
			cache.canWrite();
			cache.canRead();
			
			do {
				if(!rc.nextLine().equals(link)) {
					Files.write(
							Paths.get(this.dirCache + "vispags.txt"), 
							link.getBytes(Charset.forName("UTF-8")), 
							StandardOpenOption.APPEND
					);
					cont = false;
					resultado = true;
				}
			}
			while(rc.hasNextLine() && cont);
			
			
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Elements getLinks() {
		return this.links;
	}

	public Document getWebsite() {
		return website;
	}
	
	public String getDirPadre() {
		return dirPadre;
	}

}
