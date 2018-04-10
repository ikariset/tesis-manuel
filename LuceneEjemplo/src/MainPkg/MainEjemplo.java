package MainPkg;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import LuceneComponentes.*;

public class MainEjemplo {
	
	//Argumentos
	static String rutaIndice = "/home/setsu/Escritorio/lucene/indices";
	static String rutaDatos = "/home/setsu/Escritorio/lucene/datos";
	Indexador indizador;
	Buscador buscador;
	
	public static void main(String[] args) {		
		System.out.println("Prueba de Lucene v1");
		MainEjemplo tester;
		try {
			tester = new MainEjemplo();
			tester.inicializar(rutaIndice);
			tester.crearIndice();
			tester.buscar("Ramesh");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void crearIndice() throws IOException{
		indizador = new Indexador(rutaIndice);
		int numIndexados;
		long inicio = System.currentTimeMillis();
	    numIndexados = indizador.crearIndice(rutaDatos, new FiltroArchivos());
	    long fin = System.currentTimeMillis();
	    indizador.cerrar();
	    System.out.println(numIndexados + " Archivos indizados, tiempo de trabajo: "
		         + (fin-inicio) + " ms");
	}		
	
	private void buscar(String peticion) throws IOException, ParseException{
		buscador = new Buscador(rutaIndice);
		long inicio = System.currentTimeMillis();
		TopDocs hits = buscador.buscar(peticion);
		long fin = System.currentTimeMillis();
		System.out.println(hits.totalHits + 
				" documents found. Time :" + (fin - inicio));
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = buscador.obtenerBusqueda(scoreDoc);
			System.out.println("File: " + doc.get(ConstantesLucene.FILE_PATH));
		}
		//buscador.cerrar();
	}
	
	private void inicializar(String ruta) throws IOException{
		File dir = new File(ruta);
		File[] archivos = dir.listFiles();
	    if(archivos!=null) {
	        for(File f: archivos) {
	            if(f.isDirectory()) {
	            	inicializar(f.getCanonicalPath());
	            } else {
	                f.delete();
	            }
	        }
	    }
	}
}
