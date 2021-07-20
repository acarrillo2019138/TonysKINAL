
package org.alejandrocarrillo.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alejandrocarrillo.controller.DatosPersonalesController;
import org.alejandrocarrillo.controller.EmpleadosController;

import org.alejandrocarrillo.controller.EmpresaController;
import org.alejandrocarrillo.controller.MenuPrincipalController;
import org.alejandrocarrillo.controller.PlatosController;
import org.alejandrocarrillo.controller.PresupuestoController;
import org.alejandrocarrillo.controller.ProductosController;
import org.alejandrocarrillo.controller.Productos_has_PlatosController;
import org.alejandrocarrillo.controller.ServicioController;
import org.alejandrocarrillo.controller.Servicios_has_EmpleadosController;
import org.alejandrocarrillo.controller.Servicios_has_PlatosController;
import org.alejandrocarrillo.controller.TipoEmpleadoController;
import org.alejandrocarrillo.controller.TipoPlatoController;




public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/alejandrocarrillo/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal)
        throws Exception{
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tonys´s Kinal App");
        // icono 
        escenarioPrincipal.getIcons().add(new Image("/org/alejandrocarrillo/image/fuego.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/alejandrocarrillo/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        menuPrincipal();//clase 2 MySQL
        escenarioPrincipal.show();
    }
         public void menuPrincipal(){
             try{
                  MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",867,655);
                  menuPrincipal.setEscenarioPrincipal(this);
         }catch(Exception e){
                e.printStackTrace();
            }
         }
         public void ventanaDatosProgramador(){
         try{
             DatosPersonalesController datosPersonales =(DatosPersonalesController)cambiarEscena("DatosPersonalesView.fxml",897,706);
             datosPersonales.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
         
         public void ventanaEmpresas(){
         try{
             EmpresaController empresaController =(EmpresaController)cambiarEscena("EmpresaView.fxml",1113,748);
             empresaController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
         
         public void ventanaPresupuestos(){
             try{
             PresupuestoController presupuesto =(PresupuestoController)cambiarEscena("PresupuestosView.fxml",1030,731);
             presupuesto.setEscenarioPrincipal(this);
             
             }catch (Exception e){
                 e.printStackTrace();
             }}
         public void ventanaTipoEmpleado(){
         try{
             TipoEmpleadoController tipoEmple =(TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",633,463);
             tipoEmple.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }}
         public void ventanaEmpleado(){
         try{
             EmpleadosController empleado =(EmpleadosController)cambiarEscena("EmpleadosView.fxml",1321,700);
             empleado.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }}
         public void ventanaServicio(){
         try{
             ServicioController servicio =(ServicioController)cambiarEscena("ServiciosView.fxml",1105,633);
             servicio.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }}
        public void ventaTipoPlato(){
        try{
             TipoPlatoController tipoPlato =(TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",701,813);
             tipoPlato.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
         }
        public void ventaPlato(){
        try{
             PlatosController plato =(PlatosController)cambiarEscena("PlatosView.fxml",1035,676);
             plato.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
        }}
        public void ventaProducto(){
        try{
             ProductosController producto =(ProductosController)cambiarEscena("ProductosView.fxml",924,481);
             producto.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
        }
        public void ventaHas_Empleados(){
        try{
             Servicios_has_EmpleadosController hasEmpleados =(Servicios_has_EmpleadosController)cambiarEscena("Servicios_has_EmpleadosView.fxml",1157,651);
             hasEmpleados.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
        }
        public void ventaProHasPlato(){
        try{
             Productos_has_PlatosController hasPlatos =(Productos_has_PlatosController)cambiarEscena("Productos_has_PlatosView.fxml",738,447);
             hasPlatos.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
        }
       public void ventaSerHasPlato(){
        try{
             Servicios_has_PlatosController hasPlat =(Servicios_has_PlatosController)cambiarEscena("Servicios_has_PlatosView.fxml",738,447);
             hasPlat.setEscenarioPrincipal(this);
         }catch (Exception e){
             e.printStackTrace();
         }
        }
             
         

    public static void main(String[] args) {
        launch(args);
    }
    //clase 2 MySQL
    public Initializable cambiarEscena (String fxml,int ancho, int alto )throws  Exception{
    
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();//el cargardor es una variable y se instancia algo nuevo despues del loader
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);//el imput es para que almacene datos en el brouce o
         //hace lecturas de windows a java el imput
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());//nueva instacia y nuevo objeto 
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));//ubicacion fisica del programa
        escena = new Scene ((AnchorPane)cargadorFXML.load(archivo),ancho,alto);// se casteo a tipo escena 
        escenarioPrincipal.setScene(escena); //levanta la escena 
        escenarioPrincipal.sizeToScene();    // hace que el teatro se adecue a tamaño de escenas  
        resultado = (Initializable)cargadorFXML.getController();//aqui se casteo fxml a initializable
        
    return resultado;
    //clase 2 MySQL
    }
    
    
    
    
}
