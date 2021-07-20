
package org.alejandrocarrillo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.alejandrocarrillo.system.Principal;



public class MenuPrincipalController implements Initializable {
        private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //aqui se pegan los metodos creados en principal
    public void ventanaDatosProgramador(){
        escenarioPrincipal.ventanaDatosProgramador();
        
    }
     public void ventanaEmpresas(){
         escenarioPrincipal.ventanaEmpresas();
     }
    public void ventanaPresupuestos(){
         escenarioPrincipal.ventanaPresupuestos();
     }
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    public void ventaTipoPlato(){
        escenarioPrincipal.ventaTipoPlato();
    }
    public void ventaPlato(){
        escenarioPrincipal.ventaPlato();
    }
    public void ventaProducto(){
        escenarioPrincipal.ventaProducto();
    }
    public void ventaHas_Empleados(){
        escenarioPrincipal.ventaHas_Empleados();
    }
    public void ventaProHasPlato(){
        escenarioPrincipal.ventaProHasPlato();
    }
    public void ventaSerHasPlato(){
        escenarioPrincipal.ventaSerHasPlato();
    }
}
