import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupParsing {
	private Document website;
	private Elements links;
	private String CrawlingFolder; 
	
	private void createCrawlingFolder(){
		File salida = new File(CrawlingFolder);
		salida.getParentFile().mkdirs();
	}
	
	public JsoupParsing(String link, String CrawlingFolder) throws IOException {
		this.website = Jsoup.connect(link).get();	
		this.links = this.website.select("a[href]");
		this.CrawlingFolder = CrawlingFolder + website.title() + File.separator;
		this.createCrawlingFolder();
	}
	
	public Elements getLinks() {
		return this.links;
	}

	public Document getWebsite() {
		return website;
	}
	
	public String getCrawlingFolder() {
		return CrawlingFolder;
	}

	public File createParsedDoc() throws IOException {
		File salida = new File(CrawlingFolder + File.separator + website.title() + ".txt");
		FileOutputStream content = new FileOutputStream(salida);
		salida.createNewFile();
		salida.canWrite();
		
		content.write(
				this.website.select(".container").text().getBytes(Charset.forName("UTF-8"))
		);
		
		System.out.println(salida.getCanonicalPath());
		
		content.close();
		return salida;
	}
}
