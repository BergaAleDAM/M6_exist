package m06uf3_exist;

import java.util.List;
import org.w3c.dom.Node;

public class NewMain {

    public static void main(String[] args) {
        
       ConfigConnexio cc = new ConfigConnexio();
        Consultes cs = new Consultes(cc.getCon());
        
        //cs.updateRename("spa");
        
        //cs.quitarDolar();
    
        /*List<Node> plantes = cs.obtenirPlantes();
        for (Node planta : plantes) {
            System.out.println(planta.getTextContent());
        }
        */
        //System.out.println(cs.cercarPlantaPerNom("Bloodroot").getTextContent());
        
        //cs.afegirAtributDefecte("Floripondio", "Floripondioso");
    
        //cs.afegirPlanta("Commonname", "Cientificoname", "1", "Ioquese", 12.1, "063055");
        
        
        //cs.afegirAtributPlantaZona("Elemento", "Valor", "4");
        
        
        //cs.cercarPlantesRang(10, 13);
        
    
        //cs.cercarPlantesZone(4);
   
        
        cs.modificarPreuPlanta(16, "Commonname");
    }
    
    

}
