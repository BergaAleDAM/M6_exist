/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06uf3_exist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import org.w3c.dom.Node;

/**
 *
 * @author Jorge
 */
public class Consultes {

    private final XQConnection con;
    private XQExpression xqe;
    private XQPreparedExpression xqpe;

    public Consultes(XQConnection con) {
        this.con = con;
    }

    public List<Node> obtenirLlibres() {
        List<Node> libros = new ArrayList<>();
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc ('/m06uf3/libros.xml')//libro return $b/titulo";

            XQResultSequence rs = xqe.executeQuery(xq);
            while (rs.next()) {
                libros.add(rs.getItem().getNode());
            }
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
        return libros;
    }

    public Node cercarNom(String nom) {
        Node libro = null;
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc('/cosa/libros.xml')"
                    + "//libro where every $a in $b/titulo satisfies ($a = '" + nom + "') return $b";

            XQResultSequence rs = xqe.executeQuery(xq);
            rs.next();
            libro = rs.getItem().getNode();
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
        return libro;
    }

    public void afegirLlibre(String codigo, String categoria, String fecha_pub, String titulo, String ventas) {
        try {
            xqe = con.createExpression();
            String xq = "update insert "
                    + "    <libro codigo='" + codigo + "'>"
                    + "        <categoria>" + categoria + "</categoria>"
                    + "        <fecha_pub>" + fecha_pub + "</fecha_pub>"
                    + "        <titulo>" + titulo + "</titulo>"
                    + "        <ventas>" + ventas + "</ventas>"
                    + "    </libro>\n"
                    + "into doc('/m06uf3/libros.xml')/listadelibros";

            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void afegirAtribut(String atributo, String valor) {
        try {
            xqe = con.createExpression();
            String xq = "update insert attribute " + atributo + " {'" + valor + "'} into doc('/m06uf3/libros.xml')//libro";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void afegirEtiqueta(String etiqueta, String valor) {
        try {
            xqe = con.createExpression();
            String xq = "update insert <" + etiqueta + ">'" + valor + "'</" + etiqueta + "> into doc('/m06uf3/libros.xml')//libro";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void modificarPreuNode(String codigo, String precio) {
        try {
            xqe = con.createExpression();
            String xq = "update value doc('/m06uf3/libros.xml')//libro[@codigo='" + codigo + "']/preu with '" + precio + "'";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void eliminarLlibre(String codigo) {

        try {
            xqe = con.createExpression();
            String xq = "update delete doc('/m06uf3/libros.xml')//libro[@codigo='" + codigo + "']";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void eliminarEtiqueta(String etiqueta) {
        try {
            xqe = con.createExpression();
            String xq = "update delete doc('/m06uf3/libros.xml')//libro/" + etiqueta;
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void eliminarAtribut(String atributo) {
        try {
            xqe = con.createExpression();
            String xq = "update delete doc('/m06uf3/libros.xml')//libro/@" + atributo;
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void updateRename(String idioma) {

        try {

            xqe = con.createExpression();

            String[] espaniolo = {"NOMBRE_COMUN", "BOTANICA", "AREA", "LUMINOSIDAD", "PRESIO", "DISPONIBILIDAD"};
            String[] eng = {"COMMON", "BOTANICAL", "ZONE", "LIGHT", "PRICE", "AVAILABILITY"};
            String xq = null;

            int contador = 0;

            if (idioma.matches("spa")) {

                for (String elemento : eng) {

                    xq = "update rename doc('/cosa/plantes.xml')/CATALOG/PLANT/" + elemento + " as '" + espaniolo[contador] + "'";
                    System.out.println(xq);
                    contador++;
                    xqe.executeCommand(xq);

                }

            } else {

                for (String elemento : espaniolo) {

                    xq = "update rename doc('/cosa/plantes.xml')/CATALOG/PLANT/" + elemento + " as '" + eng[contador] + "'";
                    System.out.println(xq);
                    contador++;
                    xqe.executeCommand(xq);
                }

            }

        } catch (XQException ex) {
            Logger.getLogger(Consultes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void quitarDolar() {

        ArrayList<Node> libro = new ArrayList<>();
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc(\"/cosa/plantes.xml\")/CATALOG/PLANT/PRICE return update value $b with substring($b, 2)";
            System.out.println(xq);
            //xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Node> obtenirPlantes() {
        List<Node> plantes = new ArrayList<>();
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc ('/cosa/plantes.xml')//PLANT  return $b";

            XQResultSequence rs = xqe.executeQuery(xq);
            while (rs.next()) {
                plantes.add(rs.getItem().getNode());
            }
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
        return plantes;
    }

    public Node cercarPlantaNom(String nom) {
        Node libro = null;
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc('/cosa/plantes.xml')"
                    + "//PLANT where every $a in $b/COMMON satisfies ($a = '" + nom + "') return $b";

            XQResultSequence rs = xqe.executeQuery(xq);
            rs.next();
            libro = rs.getItem().getNode();
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
        return libro;
    }

    public void afegirPlanta(String nombreComun, String nombreCientifico, String zona, String luminosidad, double precio, String disponibilidad) {
        try {
            xqe = con.createExpression();
            String xq = "update insert "
                    + "    <PLANT>"
                    + "        <COMMON>" + nombreComun + "</COMMON>"
                    + "        <BOTANICAL>" + nombreCientifico + "</BOTANICAL>"
                    + "        <ZONE>" + zona + "</ZONE>"
                    + "        <LIGHT>" + luminosidad + "</LIGHT>"
                    + "        <PRICE>" + precio + "</PRICE>"
                    + "        <AVAILABILITY>" + disponibilidad + "</AVAILABILITY>"
                    + "    </PLANT>\n"
                    + "preceding doc('/cosa/plantes.xml')//PLANT[1]";

            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void afegirAtributDefecte(String atributo, String valor) {

        try {
            xqe = con.createExpression();
            String xq = "update insert attribute " + atributo + " {'" + valor + "'} into doc('/cosa/plantes.xml')//PLANT";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void afegirAtributPlantaZona(String element, String valor, String zona) {
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc('/cosa/plantes.xml')//PLANT where every $a in $b/ZONE satisfies ($a='" + zona + "')"
                    + "return update insert <" + element.toUpperCase() + "> {'" + valor + "'} </" + element.toUpperCase() + "> into $b";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public List<Node> cercarPlantesRang(double minim, double maxim) {
        List<Node> plantes = new ArrayList<>();
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc ('/cosa/plantes.xml')//PLANT "
                    + "where every $a in $b/PRICE satisfies($a >= '" + minim + "' and $a <= '" + maxim + "') return $b";

            XQResultSequence rs = xqe.executeQuery(xq);
            while (rs.next()) {
                plantes.add(rs.getItem().getNode());
            }

        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
        return plantes;
    }

    public List<Node> cercarPlantesZone(int zona) {
        List<Node> plantes = new ArrayList<>();
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc ('/cosa/plantes.xml')//PLANT "
                    + "where every $a in $b/ZONE satisfies($a = '" + zona + "') return $b";

            XQResultSequence rs = xqe.executeQuery(xq);
            while (rs.next()) {
                plantes.add(rs.getItem().getNode());
            }

        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
        return plantes;
    }

    public void modificarPreuPlanta(double preu, String common) {
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc ('/cosa/plantes.xml')//PLANT "
                    + "where every $a in $b/COMMON satisfies($a = '" + common + "') return update value $b/PRICE with '" + preu + "'";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void eliminarPerNom(String common) {
        try {
            xqe = con.createExpression();
            String xq = "for $b in doc('/cosa/plantes.xml')//PLANT where every $a in $b/COMMON satisfies($a = '" + common + "') return update delete $b";
            xqe.executeCommand(xq);
        } catch (XQException ex) {

            System.out.println(ex.getMessage());
        }
    }

}
