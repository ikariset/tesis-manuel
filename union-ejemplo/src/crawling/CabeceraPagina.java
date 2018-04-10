package crawling;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;

/* Para realizar el crawling y creación de los archivos, 
 * se procederán a crear clases que representan la jerarquía dentro de la página
 * como tal, utilizando entonces conceptos de árboles n-arios.
 * Los atributos considerados inicialmente contemplados son la profundidad del
 * árbol, la cantidad de hijos que un nodo posee, y obviamente los datos que alojan (el HTML). 
 * profundidad */
public class CabeceraPagina {
	private int profundidad;
	private NodoPagina cabecera;
	private String directorio;
	private String pagina;
	
	public CabeceraPagina(String pagina, String directorio){
		this.profundidad = 0;
		this.pagina = pagina;
		this.directorio = directorio;
		
		try {
			this.createDirPadre();
			this.createCachePag();
			//Inicialización del nodo index
			this.cabecera = new NodoPagina(this.pagina, this.directorio, this.directorio);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Archivo de Caché vispags.txt no pudo ser creado en directorio.");
			//Agregar un logger en caso de cualquier cosa
		}
	}
	
	private void createDirPadre(){
		File salida = new File(this.directorio);
		salida.getParentFile().mkdirs();
	}
	
	private void createCachePag(){
		File salida = null; 
		try {
			//Creación de caché de páginas visitadas
			salida = new File(directorio + File.separator + Jsoup.connect(pagina).get().title() + File.separator + "vispags.txt");
			salida.canWrite();
			salida.createNewFile();
			
			System.out.println("Archivo de Caché " + salida.getCanonicalPath() + " creado.");
			
			
			//Fin creación de caché de páginas visitadas
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Archivo de Caché vispags.txt no pudo ser creado en directorio.");
			//Agregar un logger en caso de cualquier cosa
		}
	}
	
	public int getProfundidad() {
		return profundidad;
	}

	public NodoPagina getCabecera() {
		return cabecera;
	}

	public String getDirectorio() {
		return directorio;
	}

	public String getPagina() {
		return pagina;
	}
	
	

}
