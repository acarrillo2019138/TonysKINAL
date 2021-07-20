
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.alejandrocarrillo.bean.Productos;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class ProductosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};//enumeradores
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos> listaProducto;
    @FXML private TextField   txtcodProdu   ;
    @FXML private TextField   txtnameProduc ;
    @FXML private TextField   txtcantProduc ;
    @FXML private TableView   tblproductos  ;
    @FXML private TableColumn colcodProduc  ;
    @FXML private TableColumn colnameProduc ;
    @FXML private TableColumn colcantProduc ;
    @FXML private Button      btnNuevo;
    @FXML private Button      btnEliminar;
    @FXML private Button      btnEditar;
    @FXML private Button      btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }

    public void cargarDatos(){
    tblproductos.setItems(getProductos());
    colcodProduc  .setCellValueFactory(new PropertyValueFactory<Productos,Integer >("codigoProducto"));
    colnameProduc .setCellValueFactory(new PropertyValueFactory<Productos,String >("nombreProducto"));
    colcantProduc  .setCellValueFactory(new PropertyValueFactory<Productos,String >("cantidad"));
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
    
    public void seleccionarElemento(){
        if(tblproductos.getSelectionModel().getSelectedItem() !=null ){
        txtcodProdu   .setText(String.valueOf(((Productos)
                    tblproductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtnameProduc .setText(((Productos)
                    tblproductos.getSelectionModel().getSelectedItem()).getNombreProducto());
         txtcantProduc    .setText(String.valueOf(((Productos)
                    tblproductos.getSelectionModel().getSelectedItem()).getCantidad()));
        }
    }
    
    public void nuevo(){ 
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                case GUARDAR:
                    if(txtnameProduc.getText().equals("")){
                 JOptionPane.showMessageDialog(null, "Ingrese un nombre");
             }else if(txtcantProduc.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad");
            }             
            else{
               try{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEliminar.setDisable(false);
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;  
                }catch(Exception e){
                  e.printStackTrace();
                }
             }}}
    
    public void guardar(){
        Productos registro = new Productos ();
        registro.setNombreProducto(txtnameProduc .getText());
        registro.setCantidad(Integer.parseInt(txtcantProduc .getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);           
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
        case GUARDAR:
             desactivarControles();
             limpiarControles();
             btnNuevo.setText("Nuevo");
             btnNuevo.setDisable(false);
             btnEliminar.setText("Eliminar");
             btnEliminar.setDisable(false);
             btnEditar.setDisable(false);
             btnReporte.setDisable(false);
             tipoDeOperacion = operaciones.NINGUNO;
            break;
        default:
            if(tblproductos.getSelectionModel().getSelectedItem() != null){
                int confirmacion = JOptionPane.showConfirmDialog(null,"Esta Seguro de eliminar","Eliminar Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(confirmacion == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                        procedimiento.setInt(1, ((Productos)tblproductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                        procedimiento.execute();
                        listaProducto.remove(tblproductos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        tblproductos.getSelectionModel().clearSelection();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un Dato");
            }         
            break;
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblproductos.getSelectionModel().getSelectedItem() != null){   
                    btnEditar.setText("Actualizar");      
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();            
                    tipoDeOperacion = operaciones.ACTUALIZAR; 
                }else{
                   JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false); 
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }    
    }
    
     public void actualizar(){
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?)}");
             Productos registro =(Productos)tblproductos.getSelectionModel().getSelectedItem();
             registro.setNombreProducto(txtnameProduc .getText());
             registro.setCantidad (Integer.parseInt(txtcantProduc .getText()));
             procedimiento.setInt(1, registro.getCodigoProducto());
             procedimiento.setString(2, registro.getNombreProducto());
             procedimiento.setInt(3, registro.getCantidad());
             procedimiento.execute();
        }catch(Exception e){
             e.printStackTrace();
        }
    }
    
    
    
      
      
    
    
    
    
    
    
    
    
    
    
    public void desactivarControles(){
        txtcodProdu   .setEditable(false);
        txtnameProduc .setEditable(false);
        txtcantProduc  .setEditable(false);
        
    }
    public void activarControles(){
        txtcodProdu.setEditable(false);
        txtnameProduc.setEditable(true);
        txtcantProduc.setEditable(true);
        
    }
    public void limpiarControles(){
        txtcodProdu.setText("");
        txtnameProduc.setText("");
        txtcantProduc.setText("");
        
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
