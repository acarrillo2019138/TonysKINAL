
package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrocarrillo.bean.Platos;
import org.alejandrocarrillo.bean.TipoPlato;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class PlatosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Platos> listaPlato;
    private ObservableList <TipoPlato>listaTipoPlato;
    @FXML private TextField   txtcodPlato ;
    @FXML private TextField   txtcanti    ;
    @FXML private TextField   txtnamepla  ;
    @FXML private TextField   txtdescripp ;
    @FXML private TextField   txtprecio   ;
    @FXML private ComboBox    cmbtipop    ;
    @FXML private TableView   tblplatos   ;
    @FXML private TableColumn colcodpla   ;
    @FXML private TableColumn colcanti    ;
    @FXML private TableColumn colnamepla  ;
    @FXML private TableColumn coldescrippp;
    @FXML private TableColumn colprecc    ;
    @FXML private TableColumn coltipp     ;
    @FXML private Button      btnNuevo    ;
    @FXML private Button      btnEliminar ;
    @FXML private Button      btnEditar   ;
    @FXML private Button      btnReporte  ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbtipop.setItems(getTipoPlato());
    }
    
    public void cargarDatos(){
        tblplatos.setItems(getPlatos());
        colcodpla.setCellValueFactory(new PropertyValueFactory<Platos,Integer>("codigoPlato"));
        colcanti.setCellValueFactory(new PropertyValueFactory<Platos,Integer>("cantidad"));
        colnamepla.setCellValueFactory(new PropertyValueFactory<Platos,String>("nombrePlato"));
        coldescrippp.setCellValueFactory(new PropertyValueFactory<Platos,String>("descripcionPlato"));
        colprecc.setCellValueFactory(new PropertyValueFactory<Platos,Double>("precioPlato"));
        coltipp.setCellValueFactory(new PropertyValueFactory<Platos,Integer>("TipoPlato_codigoTipoPlato")); 
    }
    
    public void seleccionarElemento(){
    if(tblplatos.getSelectionModel().getSelectedItem() !=null ){
    txtcodPlato.setText(String.valueOf(((Platos)tblplatos.
                        getSelectionModel().getSelectedItem()).getCodigoPlato()));
    txtcanti.setText(String.valueOf(((Platos)tblplatos.getSelectionModel().
                                               getSelectedItem()).getCantidad()));
    txtnamepla.setText(((Platos)tblplatos.getSelectionModel().
                                             getSelectedItem()).getNombrePlato());
    txtdescripp.setText(((Platos)tblplatos.getSelectionModel().
                                             getSelectedItem()).getDescripcionPlato());
    txtprecio.setText(String.valueOf(((Platos)
                tblplatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
    cmbtipop.getSelectionModel().select(buscarTipoPlato(((Platos)
                tblplatos.getSelectionModel().getSelectedItem()).getTipoPlato_codigoTipoPlato()));
        }
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
            if(txtcanti.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad");
             }else if(txtnamepla.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese un Nombre"); 
             }else if(txtdescripp.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese una Descripcion");
             }else if(txtprecio.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese un Precio"); 
             }else if(cmbtipop.getSelectionModel().getSelectedItem() == null){
                JOptionPane.showMessageDialog(null, "Ingrese un Codigo Tipo Plato"); 
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
        Platos registro = new Platos ();
         registro.setCantidad(Integer.parseInt(txtcanti.getText()));
         registro.setNombrePlato(txtnamepla.getText());
         registro.setDescripcionPlato(txtdescripp.getText());
         registro.setPrecioPlato(Double.parseDouble(txtprecio.getText()));
         registro.setTipoPlato_codigoTipoPlato(((TipoPlato)cmbtipop.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
        PreparedStatement procedimiento = Conexion.getInstance()
            .getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
            procedimiento.setInt   (1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt   (5, registro.getTipoPlato_codigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
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
                if (tblplatos.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null,
                    "¿ Está seguro de eliminar este registro ?","  Eliminar  Plato !!! ",
                        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().
                               getConexion().prepareCall("{call sp_EliminarPlato(?)}");
                        procedimiento.setInt(1,((Platos)tblplatos.getSelectionModel()
                                                 .getSelectedItem()).getCodigoPlato());
                        listaPlato.remove(tblplatos.getSelectionModel().getSelectedIndex());
                        procedimiento.execute();
                        limpiarControles();
                        tblplatos.getSelectionModel().clearSelection();
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
            if (tblplatos.getSelectionModel().getSelectedItem() != null){
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
                                   getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?)}");
                Platos registro = (Platos)tblplatos.getSelectionModel().getSelectedItem();
                registro.setCantidad(Integer.parseInt(txtcanti .getText()));
                registro.setNombrePlato(txtnamepla.getText());
                registro.setDescripcionPlato(txtdescripp.getText());
                registro.setPrecioPlato(Double.parseDouble(txtprecio.getText()));
                registro.setTipoPlato_codigoTipoPlato(((TipoPlato)cmbtipop.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                procedimiento.setInt(   1, registro.getCodigoPlato());
                procedimiento.setInt(   2, registro.getCantidad());
                procedimiento.setString(3, registro.getNombrePlato());
                procedimiento.setString(4, registro.getDescripcionPlato());
                procedimiento.setDouble(5, registro.getPrecioPlato());
                procedimiento.execute();
                cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
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
    public void desactivarControles(){
        txtcodPlato  .setEditable(false);
        txtcanti     .setEditable(false);
        txtnamepla   .setEditable(false);
        txtdescripp  .setEditable(false);
        txtprecio    .setEditable(false);
        cmbtipop     .setDisable(false);
        
    }
    
    public void activarControles(){
        txtcodPlato.setEditable(false);
        txtcanti   .setEditable(true);
        txtnamepla .setEditable(true);
        txtdescripp.setEditable(true);
        txtprecio  .setEditable(true);
        cmbtipop   .setEditable(false);
        
    }
    
     public void limpiarControles(){
       txtcodPlato  .setText("");
       txtcanti     .setText("");
       txtnamepla   .setText("");
       txtdescripp  .setText("");
       txtprecio    .setText("");
       cmbtipop     .getSelectionModel().clearSelection();
    }
    
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventaTipoPlato(){
        escenarioPrincipal.ventaTipoPlato();
    }
    
}
