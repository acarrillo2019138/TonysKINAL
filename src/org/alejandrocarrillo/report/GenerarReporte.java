
package org.alejandrocarrillo.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.util.JRLoader;//hace lectura del reporte
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;
import org.alejandrocarrillo.db.Conexion;

public class GenerarReporte {
    public static void mostrarReporte(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);//genera reporte
            JasperPrint  reporteImpreso =  JasperFillManager.fillReport(reporteMaestro,
                                           parametros, Conexion.getInstance().getConexion());// actua o guarda el reporte
            JasperViewer visor          = new JasperViewer(reporteImpreso,false);//imprimereporte a la vista de los modulos
                visor.setTitle(titulo);//permiso a variable para que pueda actuar todo lo typeado ya 
                visor.setVisible(true);
        }catch (Exception e){
                e.printStackTrace();
        }    
    }
    
}
