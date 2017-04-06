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

/**
 * 
 * Esta es la clase sobre la que trabajaremos 
 * tiene una coleccion que servirá para ejecutar algunos
 * metodos sobre esta. 
 * Tiene una configConexion que nos vale para iniciarla mas adelante
 * Contiene tambien una lista de servicios 
 * y una Collection Management Service que ejecutara metodos tambien pero 
 * sobre el manejo de las colecciones
 * 
 * @author ALUMNEDAM
 */
public class Colecciones {

    Collection col;
    ConfigConnexio cc = new ConfigConnexio();
    Service[] serveis;
    CollectionManagementService cms;

    /**
     * 
     * 
     * Decimos que nuestra coleccion será el configColeccion iniciar y el collection
     * management service lo declaramos
     */
    public Colecciones() {
        this.col = cc.iniciar();
        buscarCollectionManagement();
    }

    /**
     * 
     * Averiguamos el nombre de la coleccion en la que nos encontramos 
     * 
     */
    public void nomColeccioActual() {

        try {
            System.out.println(col.getName());
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Averiguamos la coleccion padre en la que nos encontramos
     * 
     */
    public void nomColeccioPare() {
        try {
            System.out.println(col.getParentCollection().getName());
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 
     * Instanciamos una lista y si podemos con el listChildCollections rellenamos la lista
     * con todos los hijos que tiene la coleccion en la que nos encontramos
     * 
     * @return 
     */
    public String[] llistatColeccionsFilles() {
        String[] lista = null;
        
        try {
            lista= col.listChildCollections();
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    
    /**
     * 
     * Le pasamos por parametros un nombre que tendra la nueva coleccion desde el cms
     * 
     * 
     * @param nombre 
     */
    public void crearColeccio(String nombre) {
        try {
            cms.createCollection(nombre);
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * 
     * Eliminamos una coleccion por el nombre del parametro que le pasamos
     * 
     * @param nombre 
     */
    public void eliminarCollecio(String nombre) {
        try {
            cms.removeCollection(nombre);
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * devolvemos un boolean conforme hemos encontrado o no un recurso en la coleccion
     * simmulamos entrar en la coleccion que pasamos por parametro
     * 
     * e intentamos obtener el recurso por el nombre que le hemos pasado por parametro
     * 
     * @param coleccio
     * @param recurso
     * @return 
     */
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

    /*
    busqueda, crear/eliminar coleccion , insertar nodo, modificacion, meter recurso a coleccion, meter atributos
    */
    
    
    /**
     * 
     * rellena la lista de servicios con la cantidad de servicios que hay en col
     * 
     * busca hasta que uno de estos sea CollectionManagementService en cuyo caso lo asigna al creado
     * ya anteriormente
     * 
     * 
     */
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

    /**
     * 
     * Para subir un xml hacemos la instancia del document builder y le hacemos un parse de 
     * FILE con la ruta para normalizarlo
     * 
     * Instanciamos un recurso xml creandolo en col con su nombre y lo rellenamos con el documento
     * normalizado
     * 
     * y guardamos en col
     * 
     * @param ruta
     * @param nombre 
     */
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

    /**
     * 
     * Craemos un recurso xml cogiendolo de Colecction con el getResource y el nombre que le
     * hemos pasado por parametro y devolvemos el dom de ese recurso
     * 
     * @param nombre
     * @return 
     */
    public Document obtenirXML(String nombre) {

        XMLResource xml = null;
        try {

            xml = (XMLResource) col.getResource(nombre);
            
            Document d = (Document)xml.getContentAsDOM();
            return d;

        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

        

    }

    /**
     * 
     * buscamos el recurso por el nobmre que le hemos pasado por parametro
     * y lo borramos desde el metodo correspondiente
     * 
     * @param nombre 
     */
    public void borrarXML(String nombre) {

        XMLResource xml = null;
        try {

            xml = (XMLResource) col.getResource(nombre);

            col.removeResource(xml);

            System.out.println("Se borró el fichero XML con éxito");

        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 
     * PAra generar un recurso binario lo instanciamos y decimos que clase de recurso es
     * establecemos un File con la ruta que hemos pasado por parametro y decimos que el contenido
     * sera el File para despues almacenarlo
     * 
     * @param ruta
     * @param nombre 
     */
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

    /**
     * 
     * Declaramos un binary Resource y obtenemos el recurso de la ruta que hayamos
     * pedido.
     * Despues escribimos sobre un file el contenido de este binary resource
     * 
     * y lo almacenamos en la coleccio
     * 
     * @param ruta 
     * @param nombre 
     */
    public void obtenerBinario(String ruta, String nombre) {

        BinaryResource br;
        try {
            br = (BinaryResource) col.getResource(nombre);

            Files.write(Paths.get(ruta), (byte[]) br.getContent());

            col.storeResource(br);

        } catch (XMLDBException | IOException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
