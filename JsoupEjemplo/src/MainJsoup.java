import java.io.File;
import java.io.IOException;
import org.jsoup.nodes.Element;


public class MainJsoup {
	
    public static void main(String[] args) throws IOException {

        //Document doc = Jsoup.connect("http://www.unap.cl/prontus_unap/site/edic/base/port/inicio.html").get();
        //System.out.println(doc.title() + "\n" + doc.html());
        
        //Elements links = doc.select("a[href]");
        //System.out.println("Tamaño de links encontrados: " + links.size());
        
        //Elements imgs = doc.select("img[src]");
        
        JsoupParsing jsp = new JsoupParsing("http://www.unap.cl/prontus_unap/site/edic/base/port/inicio.html", "/home/setsu/Escritorio/");
        
        System.out.println("CrawlingPage: " + jsp.getCrawlingFolder());
        
        File f = jsp.createParsedDoc();
        
        for(Element link: jsp.getLinks()) {
        	System.out.println("link: " + link.attr("abs:href"));
        	System.out.println("text: " + link.text() + "\n");
        }
       /* 
        for(Element imagen: imgs) {
        	System.out.println("img: " + imagen.attr("abs:src"));
        	System.out.println("text: " + imagen.text() + "\n");
        }*/
    }
}
