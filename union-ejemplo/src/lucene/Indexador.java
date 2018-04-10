package lucene;

//Clases de JavaIO
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
//Clases de Lucene
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexador {
	private IndexWriter escritor;

	public Indexador(String RutaDirectorioIndice) throws IOException {
		//El directorio alojará los índices
		//Directory DirectorioIndices = FSDirectory.open(new File(RutaDirectorioIndice));	
		Directory DirectorioIndices = FSDirectory.open(
				Paths.get(new File(RutaDirectorioIndice).getCanonicalPath())
		);
		
		this.escritor = new IndexWriter(
				DirectorioIndices,
				new IndexWriterConfig(new StandardAnalyzer(CharArraySet.EMPTY_SET))
		);
	}
	
	public void cerrar() throws CorruptIndexException, IOException{
		escritor.close();
	}
	
	private Document DocumentoIndice(File archivo) throws FileNotFoundException, IOException{
		Document indice = new Document();
		
		//Se lee el contenido del archivo índice
		Field contenido = new TextField(
				ConstantesLucene.CONTENTS, 
				new FileReader(archivo)
		);
		System.out.println(contenido.stringValue());
		//Se lee el nombre del archivo índice
		Field nombreArchivo = new StringField(
				ConstantesLucene.FILE_NAME,
		        archivo.getName(),
		        Field.Store.YES
		);
		System.out.println(nombreArchivo.stringValue());
		//Se lee la ruta del archivo índice
		Field rutaArchivo = new StringField(
				ConstantesLucene.FILE_PATH,
				archivo.getCanonicalPath(),
				Field.Store.YES
		);
		
		indice.add(contenido);
		indice.add(nombreArchivo);
		indice.add(rutaArchivo);
		
		return indice;
	}
	
	private void indizarArchivo(File archivo) throws IOException{
		System.out.println("Indizando " + archivo.getCanonicalPath());
		Document doc = DocumentoIndice(archivo);
		escritor.addDocument(doc);
	}
	
	public int crearIndice(String RutaDocs, FiltroArchivos filtro) throws IOException{
		//Se obtienen todos las rutas de los archivos alojados para indización
		File[] documentos = new File(RutaDocs).listFiles();
		
		for(File archivo : documentos) {
			if(
				!archivo.isDirectory()
				&& !archivo.isHidden()
				&& archivo.exists()
				&& archivo.canRead()
				&& filtro.accept(archivo)
				) {
				indizarArchivo(archivo);
			}
		}
		
		return escritor.numDocs();
	}
}
