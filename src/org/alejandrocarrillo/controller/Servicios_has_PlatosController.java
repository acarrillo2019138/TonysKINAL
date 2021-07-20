
package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.bean.Platos;
import org.alejandrocarrillo.bean.Servicio;
import org.alejandrocarrillo.bean.Servicios_has_Platos;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class Servicios_has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList <Servicios_has_Platos> listaServicioHasPlato;
    private ObservableList <Servicio> listaServicio;
    private ObservableList <Platos> listaPlato;
    @FXML private ComboBox  cmbServ;
    @FXML private ComboBox  cmbPlatt;
    @FXML private TableView tblHasSP;
    @FXML private TableColumn colCservicio;
    @FXML private TableColumn colCpplatos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();
    cmbServ.setItems(getServicio());
    cmbPlatt.setItems(getPlatos());   
    }
    public void cargarDatos(){
        tblHasSP.setItems(getServicios_has_Platos());
        colCservicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("codigoServicio")); 
        colCpplatos.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("codigoPlato")); 
    }
    public void seleccionarElemento(){
         if(tblHasSP.getSelectionModel().getSelectedItem() !=null) {
             cmbServ.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblHasSP.getSelectionModel().getSelectedItem()).getCodigoServicio()));
             cmbPlatt.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblHasSP.getSelectionModel().getSelectedItem()).getCodigoPlato()));
         }
    }   
    public ObservableList<Servicios_has_Platos> getServicios_has_Platos(){
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Platos(resultado.getInt("codigoServicio"),
                                               resultado.getInt("codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicioHasPlato = FXCollections.observableArrayList(lista);
    }
 
     public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                       resultado.getDate("fechaServicio"),
                                       resultado.getString("tipoServicio"),
                                       resultado.getString("horaServicio"),
                                       resultado.getString("lugarServicio"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  listaServicio = FXCollections.observableArrayList(lista);
    }   
    public ObservableList<Platos>getPlatos(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance()
                 .getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Platos(resultado.getInt("codigoPlato")
                                   ,resultado.getInt("cantidad"),
                                   resultado.getString("nombrePlato"),
                                   resultado.getString("descripcionPlato"),
                                   resultado.getDouble("precioPlato"),
                                   resultado.getInt("TipoPlato_codigoTipoPlato")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    } 
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Servicio(
                                           registro.getInt("codigoServicio"),
                                           registro.getDate("fechaServicio"),
                                           registro.getString("tipoServicio"),
                                           registro.getString("horaServicio"),
                                           registro.getString("lugarServicio"),
                                           registro.getString("telefonoContacto"),
                                           registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    } 
   public Platos buscarPlato(int codigoPlato){
        Platos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Platos(registro.getInt("codigoPlato"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"),
                                        registro.getString("descripcionPlato"),
                                        registro.getDouble("precioPlato"),
                                        registro.getInt("TipoPlato_codigoTipoPlato"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
       
    
    
    
    
    
    
    
    
    
    
    
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
