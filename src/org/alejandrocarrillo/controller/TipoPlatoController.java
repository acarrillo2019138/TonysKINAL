
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
import org.alejandrocarrillo.bean.TipoPlato;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class TipoPlatoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <TipoPlato> listaTipoPlato;
    @FXML private TextField txtcodTipoPlato;
    @FXML private TextField txtdescrip     ;
    @FXML private TableView tbltipoPlato   ;
    @FXML private TableColumn colcodTplato ;
    @FXML private TableColumn coldescrip   ;
    @FXML private Button      btnNuevo     ;
    @FXML private Button      btnEliminar  ;
    @FXML private Button      btnEditar    ;
    @FXML private Button      btnReporte   ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tbltipoPlato.setItems(getTipoPlato());
        colcodTplato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        coldescrip.setCellValueFactory  (new PropertyValueFactory<TipoPlato,String>     ("descripcion"));
    }
    
    public ObservableList<TipoPlato>getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance()
                 .getConexion().prepareCall("{call sp_ListarTipoPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato")
                                 ,resultado.getString("descripcion")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
    if(tbltipoPlato.getSelectionModel().getSelectedItem() !=null ){
    txtcodTipoPlato.setText(String.valueOf(((TipoPlato)tbltipoPlato.
      getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    txtdescrip.setText(((TipoPlato)tbltipoPlato.getSelectionModel().
                               getSelectedItem()).getDescripcion());
    
        }
    }
    
      public void nuevo(){         
          switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText    ("Guardar");
                btnEliminar.setDisable  (false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable     (true);
                btnReporte.setDisable    (true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                case GUARDAR:
            if(txtdescrip.getText().equals("")){
                 JOptionPane.showMessageDialog(null, "Ingrese una descripcion");   
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
        TipoPlato registro = new TipoPlato ();
        registro.setDescripcion(txtdescrip.getText());
        try{
        PreparedStatement procedimiento = Conexion.getInstance()
            .getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance()
            .getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
            resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                      registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         return resultado;
    }
    
    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText      ("Nuevo");
                btnNuevo.setDisable     (false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable  (false);
                btnEditar.setDisable    (false);
                btnReporte.setDisable   (false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
            default:
                if (tbltipoPlato.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,
                    "¿ Está seguro de eliminar este registro ?","Eliminar Tipo de Plato ",
                        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().
                               getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                        procedimiento.setInt(1,((TipoPlato)tbltipoPlato.getSelectionModel()
                                                 .getSelectedItem()).getCodigoTipoPlato());
                        listaTipoPlato.remove(tbltipoPlato.getSelectionModel().getSelectedIndex());
                        procedimiento.execute();
                        limpiarControles();
                        tbltipoPlato.getSelectionModel().clearSelection();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }  
    }
    
    public void editar(){
    switch(tipoDeOperacion){
        case NINGUNO:
            if (tbltipoPlato.getSelectionModel().getSelectedItem() != null){
            btnEditar.setText("Actualizar");
            btnReporte.setText ("Cancelar");
            btnNuevo.setDisable      (true);
            btnEliminar.setDisable   (true);
            activarControles();
            tipoDeOperacion = operaciones.ACTUALIZAR;
            }else {
                JOptionPane.showMessageDialog(null,"Debe selecionar un elemento");
            }  
                break;
        case ACTUALIZAR:
            actualizar();
            btnEditar.setText  ("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setDisable   (false);
            btnEliminar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
            cargarDatos();
            break;
        }     
    }
    
    public void actualizar(){
        try{
                PreparedStatement procedimiento = Conexion.getInstance().
                                   getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
                TipoPlato registro = (TipoPlato)tbltipoPlato.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtdescrip.getText());
                procedimiento.setInt(1, registro.getCodigoTipoPlato());
                procedimiento.setString(2, registro.getDescripcion());
                procedimiento.execute();
                cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtcodTipoPlato.setEditable(false);
        txtdescrip.setEditable     (false);
        
    }
    public void activarControles(){
        txtcodTipoPlato.setEditable(false);
        txtdescrip.setEditable      (true);
        
    }
    public void limpiarControles(){
        txtcodTipoPlato.setText("");
        txtdescrip.setText     ("");
        
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
    public void ventaPlato(){
        escenarioPrincipal.ventaPlato();
    }
   
}
