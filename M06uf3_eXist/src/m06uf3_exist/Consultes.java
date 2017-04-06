/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06uf3_exist;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import org.w3c.dom.Node;

/**
 *
 * Declaramos las variables con las que vamos a trabajar en un futuro
 *
 * @author Alguien
 */
public class Consultes {

    private final XQConnection con;
    private XQExpression xqe;
    private XQPreparedExpression xqpe;

    /**
     * Decimos que la conexion será la que le pasemos por parámetro
     *
     * @param con
     */
    public Consultes(XQConnection con) {
        this.con = con;
    }

    /*
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
     */
    /**
     *
     *
     * Creamos el comando de borrar un atricuto que se marca con la @ y se
     * ejecuta el comando
     *
     * @param atributo
     */
    void eliminarAtribut(String atributo) {
        try {
            xqe = con.createExpression();
            String xq = "update delete doc('/m06uf3/libros.xml')//libro/@" + atributo;
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     * creamos dos listas de idiomas y dependiendo del idioma que le hayamos
     * pasado por parametro renombrará cada etiqueta con la del idioma que
     * corresponde
     *
     * @param idioma
     */
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

    /**
     * 
     * Creamos una sentencia que pase por cada PRICE dentre de plantes y le quite los simbolos de dolar
     * 
     * 
     */
    public void quitarDolar() {

        try {
            xqe = con.createExpression();
            String xq = "for $b in doc(\"/cosa/plantes.xml\")/CATALOG/PLANT/PRICE return update value $b with substring($b, 2)";
            
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * 
     * Creamos la sentencia que pase por cada planta y mientras tenga elementos lo metemos en una lista 
     * de nodos para devolver los que hay
     * 
     * @return 
     */
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

    /**
     * 
     * Creamos la sentencia que por cada PLANT si su etiqueta COMMON es igual a lo que le pasamos por parametro
     * nos devuelve el nodo de la planta que le corresponde
     * 
     * @param nom
     * @return 
     */
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

    /**
     * 
     * Hacemos una sentencia que crea una planta con los nombrs que le hemos pasado por parametro
     * 
     * @param nombreComun
     * @param nombreCientifico
     * @param zona
     * @param luminosidad
     * @param precio
     * @param disponibilidad 
     */
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

    /**
     * 
     * Con los parametros que le pasamos por parametro dentro de cada PLANT asignamos un atributo con el nombre
     * y el valor que hayamos pasado
     * 
     * @param atributo
     * @param valor 
     */
    public void afegirAtributDefecte(String atributo, String valor) {

        try {
            xqe = con.createExpression();
            String xq = "update insert attribute " + atributo + " {'" + valor + "'} into doc('/cosa/plantes.xml')//PLANT";
            xqe.executeCommand(xq);
        } catch (XQException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * 
     * Establecemos una sentencia que por cada PLANT/ZONE sea igual a la zona que le hayamos pasado por
     * parametro haga un insert de un nuevo nodo con un valor tambien pasados por parametros
     * 
     * 
     * @param element
     * @param valor
     * @param zona 
     */
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

    /**
     * 
     * Busca entre todas las plantas que tengan un PRICE entre los dos numeros que le hemos pasado por
     * parametro y los va metiendo en una lista de nodos
     * 
     * @param minim
     * @param maxim
     * @return 
     */
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

    /**
     * 
     * busca entre todas las zonas y si alguna es igual a la de la planta por la que esta pasando la
     * mete en una lista de nodes
     * 
     * @param zona
     * @return 
     */
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

    /**
     * 
     * Hacemos la sentencia que por cada nombre que coincida con el pasado por parametro
     * actualice el precio 
     * 
     * @param preu
     * @param common 
     */
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

    /**
     * 
     * Buscamos entre todas las plantas y si su COMMON coincide con el pasado por
     * parametro se selimina con el delete
     * 
     * @param common 
     */
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
