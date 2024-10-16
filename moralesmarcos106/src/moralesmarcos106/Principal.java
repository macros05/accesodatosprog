package moralesmarcos106;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Principal {

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document document = builder.parse(new File("archivo.xml"));
            
            document.getDocumentElement().normalize();
            
            NodeList nodeList = document.getElementsByTagName("etiquetaElemento");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                  
                    System.out.println("Elemento: " + element.getNodeName());
                    System.out.println("Atributo id: " + element.getAttribute("id")); 
                    System.out.println("Contenido: " + element.getTextContent());
                    
                    grabarDatosEnArchivo(element);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void grabarDatosEnArchivo(Element element) {
        try (FileWriter writer = new FileWriter("datos_nodos.txt", true)) {
            String id = element.getAttribute("id"); 
            String contenido = element.getTextContent().trim();
            
            writer.write("Elemento: " + element.getNodeName() + ", ");
            writer.write("Atributo id: " + id + ", ");
            writer.write("Contenido: " + contenido + "\n");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
