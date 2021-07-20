
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
import org.alejandrocarrillo.bean.Productos;
import org.alejandrocarrillo.bean.Productos_has_Platos;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class Productos_has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList <Productos_has_Platos> listaPro_has_Pla;
    private ObservableList <Productos> listaProducto;
    private ObservableList <Platos> listaPlato;
    @FXML private ComboBox  cmbProdu;
    @FXML private ComboBox  cmbPlat;
    @FXML private TableView tblHasPP;
    @FXML private TableColumn colCproductos;
    @FXML private TableColumn colCplatos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();
    cmbProdu.setItems(getProductos());
    cmbPlat.setItems(getPlatos());
    }
    
    public void cargarDatos(){
        tblHasPP.setItems(getProductos_has_Platos());
        colCproductos.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos,Integer>("codigoProducto")); 
        colCplatos.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos,Integer>("codigoPlato")); 
    }
    public void seleccionarElemento(){
         if(tblHasPP.getSelectionModel().getSelectedItem() !=null) {
             cmbProdu.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblHasPP.getSelectionModel().getSelectedItem()).getCodigoProducto()));
             cmbPlat.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblHasPP.getSelectionModel().getSelectedItem()).getCodigoPlato()));
         }
    }
    
    public ObservableList<Productos_has_Platos> getProductos_has_Platos(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Productos_has_Platos(resultado.getInt("codigoProducto"),
                                                   resultado.getInt("codigoPlato")));
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPro_has_Pla=FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Productos>getProductos(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Productos(resultado.getInt("codigoProducto"),
                                       resultado.getString("nombreProducto"),
                                       resultado.getInt("cantidad")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
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
    
    public Productos buscarProducto(int codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(registro.getInt("codigoProducto"),
                                         registro.getString("nombreProducto"),
                                         registro.getInt("cantidad"));
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
