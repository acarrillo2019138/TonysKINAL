
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
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrocarrillo.bean.TipoEmpleado;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class TipoEmpleadoController implements Initializable  {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};//enumeradores
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML private TextField   txtCodigoEmpleado;
    @FXML private TextField   txtDescripcion;
    @FXML private TableView   tblTipoEmple;
    @FXML private TableColumn colCodEmple;
    @FXML private TableColumn colDescrip;
    @FXML private Button      btnNuevo;
    @FXML private Button      btnEliminar;
    @FXML private Button      btnEditar;
    @FXML private Button      btnReporte;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    public void cargarDatos(){
    tblTipoEmple.setItems(getTipoEmpleado());
    colCodEmple.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer >("codigoTipoEmpleado"));
    colDescrip.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,String >("descripcion"));
    }
    
    
    public ObservableList<TipoEmpleado>getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                resultado.getString("descripcion")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        //es un elemento selecionado para eliminar
        if(tblTipoEmple.getSelectionModel().getSelectedItem() !=null ){
        txtCodigoEmpleado.setText(String.valueOf(((TipoEmpleado)
                    tblTipoEmple.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        
        txtDescripcion.setText(((TipoEmpleado)
                    tblTipoEmple.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
    
    public void nuevo(){ //se vaa pegar al button
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
               if(txtDescripcion.getText().equals("")){
                 JOptionPane.showMessageDialog(null, "Ingrese una Descripcion");
             }else{
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
        TipoEmpleado registro = new TipoEmpleado ();
        registro.setDescripcion(txtDescripcion.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);           
        }catch (Exception e){
            e.printStackTrace();
        }
    }
  
    
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                        registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         return resultado;
    }
    
    
    
    //---------------------------
    public void eliminar(){
        switch (tipoDeOperacion){
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
                cargarDatos();
                break;
            default:
                if (tblTipoEmple.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?","Eliminar Tipo de Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                        procedimiento.setInt(1,((TipoEmpleado)tblTipoEmple.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                        listaTipoEmpleado.remove(tblTipoEmple.getSelectionModel().getSelectedIndex());
                        procedimiento.execute();
                        limpiarControles();
                        tblTipoEmple.getSelectionModel().clearSelection();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }  
    }
    //NOVENA CLASE 
    public void editar(){
    switch(tipoDeOperacion){
        case NINGUNO:
            if (tblTipoEmple.getSelectionModel().getSelectedItem() != null){
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            btnNuevo.setDisable(true);
            btnEliminar.setDisable(true);
            activarControles();
            tipoDeOperacion = operaciones.ACTUALIZAR;
            }else {
                JOptionPane.showMessageDialog(null,"Debe selecionar un elemento");
            }  
                break;
        case ACTUALIZAR:
            actualizar();
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
        
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
                TipoEmpleado registro = (TipoEmpleado)tblTipoEmple.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtDescripcion.getText());
                procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
                procedimiento.setString(2, registro.getDescripcion());
                
                procedimiento.execute();
                cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
        
    }
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtDescripcion.setEditable(true);
        
    }
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtDescripcion.setText("");
        
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
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
}



