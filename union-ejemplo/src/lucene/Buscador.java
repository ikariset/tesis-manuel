package lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Buscador {
	IndexSearcher buscadorIndice;
	QueryParser analizadorPeticion;
	Query peticion;
	
	public Buscador(String rutaIndice) throws IOException {
		Directory directorioIndice = 
				FSDirectory.open(Paths.get(new File(rutaIndice).getCanonicalPath()));
		
		System.out.println("Ruta: " + directorioIndice.toString());
		this.buscadorIndice = new IndexSearcher(DirectoryReader.open(directorioIndice));
		this.analizadorPeticion = new QueryParser(
				ConstantesLucene.CONTENTS,
				new StandardAnalyzer()
		);
		System.out.println("buscador índice: " + analizadorPeticion.getField().toString());
	}

	public TopDocs buscar(String expr) throws IOException, ParseException{
		this.peticion = analizadorPeticion.parse(expr);
		return buscadorIndice.search(peticion, ConstantesLucene.MAX_SEARCH);
	}
	
	public Document obtenerBusqueda(ScoreDoc scoreDoc) throws CorruptIndexException, IOException{
		return buscadorIndice.doc(scoreDoc.doc);	
	}
}
