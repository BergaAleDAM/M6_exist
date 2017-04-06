package Activitat2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

public class Colecciones {

    Collection col;
    ConfigConnexio cc = new ConfigConnexio();
    Service[] serveis;
    CollectionManagementService cms;

    public Colecciones() {
        this.col = cc.iniciar();
        buscarCollectionManagement();
    }

    public void nomColeccioActual() {

        try {
            System.out.println(col.getName());
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nomColeccioPare() {
        try {
            System.out.println(col.getParentCollection().getName());
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String[] llistatColeccionsFilles() {
        try {
            return col.listChildCollections();
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void crearColeccio(String nombre) {
        try {
            cms.createCollection(nombre);
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarCollecio(String nombre) {
        try {
            cms.removeCollection(nombre);
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean cercarEnColeccio(String coleccio, String recurso) {

        boolean cierto;
        try {
            col = DatabaseManager.getCollection("xmldb:exist://localhost:8080/exist/xmlrpc/db/" + coleccio, "admin", "admin");

            Resource r = col.getResource(recurso);

            cierto = r != null;

        } catch (XMLDBException ex) {
            cierto = false;
        }

        return cierto;

    }

    public void buscarCollectionManagement() {
        try {
            serveis = col.getServices();
            for (Service s : serveis) {
                if (s.getName().equals("CollectionManagementService")) {
                    cms = (CollectionManagementService) s;
                }
            }
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pujarXML(String ruta, String nombre) {
        try {

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(new File(ruta));
            doc.normalizeDocument();

            XMLResource xml = (XMLResource) col.createResource(nombre, XMLResource.RESOURCE_TYPE);
            xml.setContentAsDOM(doc);

            col.storeResource(xml);

        } catch (SAXException | IOException | XMLDBException | ParserConfigurationException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XMLResource obtenirXML(String nombre) {

        XMLResource xml = null;
        try {

            xml = (XMLResource) col.getResource(nombre);
            
            Document d = (Document)xml.getContentAsDOM();
            System.out.println(d.getFirstChild().getTextContent());

        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }

        return xml;

    }

    public XMLResource borrarXML(String nombre) {

        XMLResource xml = null;
        try {

            xml = (XMLResource) col.getResource(nombre);

            col.removeResource(xml);

            System.out.println("Se borró el fichero XML con éxito");

        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }

        return xml;

    }

    public void generarBinario(String ruta, String nombre) {

        BinaryResource bin = null;
        try {

            bin = (BinaryResource) col.createResource(nombre, BinaryResource.RESOURCE_TYPE);

            File f = new File(ruta);

            bin.setContent(f);

            col.storeResource(bin);

        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void obtenerBinario(String ruta) {

        BinaryResource br;
        try {
            br = (BinaryResource) col.getResource(ruta);

            Files.write(Paths.get(ruta), (byte[]) br.getContent());

            col.storeResource(br);

        } catch (XMLDBException | IOException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
