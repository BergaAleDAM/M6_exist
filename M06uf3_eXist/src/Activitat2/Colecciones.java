package Activitat2;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

public class Colecciones {

    Collection col;
    ConfigConnexio cc = new ConfigConnexio();
    Service[] serveis;
    CollectionManagementService cms;

    public Colecciones() {
        this.col = cc.iniciar();
        buscarCollectionManagement();
    }
    
    
    public void nomColeccioActual(){
        
        try {
            System.out.println(col.getName());
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void nomColeccioPare(){
        try {
            System.out.println(col.getParentCollection().getName());
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String[] llistatColeccionsFilles(){
        try {
            return col.listChildCollections();
        } catch (XMLDBException ex) {
            Logger.getLogger(Colecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void crearColeccio(String nombre){
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
    
    public boolean cercarEnColeccio(String coleccio ,String recurso){
        
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
    
}
