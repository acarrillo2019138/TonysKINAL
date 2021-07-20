
package org.alejandrocarrillo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alejandrocarrillo.bean.Empresa;
import org.alejandrocarrillo.bean.Presupuesto;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.report.GenerarReporte;
import org.alejandrocarrillo.system.Principal;

public class PresupuestoController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Presupuesto> listaPresupuesto;
    private DatePicker fechaS;
    
    @FXML private TextField txtCodigoPre;
    @FXML private TextField txtCantidadPre;
    @FXML private Label lblCodigoPre;
    @FXML private GridPane grpfechaSolicitada;
    @FXML private ComboBox cmbCodEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPre;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colCantidadPre;
    @FXML private TableColumn colCodEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fechaS = new DatePicker(Locale.ENGLISH);
        fechaS.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fechaS.getCalendarView().todayButtonTextProperty().set("Today");
        fechaS.getCalendarView().setShowWeeks(false);
        fechaS.getStylesheets().add("/org/alejandrocarrillo/resource/DatePicker.css");
        grpfechaSolicitada.add(fechaS, 0, 0);
        cmbCodEmpresa.setItems(getEmpresa());
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPre.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPre.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElementos(){
        if(tblPresupuestos.getSelectionModel().getSelectedItem() !=null){
        txtCodigoPre.setText(String.valueOf(((Presupuesto)
                tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fechaS.selectedDateProperty().set(((Presupuesto)
                tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPre.setText(String.valueOf(((Presupuesto)
                tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)
                tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }
    }

    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Presupuesto(  resultado.getInt("codigoPresupuesto"),
                                            resultado.getDate("fechaSolicitud"),
                                            resultado.getDouble("cantidadPresupuesto"),
                                            resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setVisible(true);
                btnNuevo.setText("Guardar");
                btnEliminar.setVisible(true);
                btnEliminar.setText("Cancelar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                desactivarID();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
               if((txtCantidadPre.getText().equals("")) ){
                 JOptionPane.showMessageDialog(null, "Ingrese una Cantidad");
             }else if(cmbCodEmpresa.getSelectionModel().getSelectedItem() == null){
                 JOptionPane.showMessageDialog(null, "Ingrese un codigo Empresa");
             }else if(fechaS.getSelectedDate() == null){
                 JOptionPane.showMessageDialog(null, "Ingrese una Fecha");
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
             }
             
     }
    }
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fechaS.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPre.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                activarControles();
                activarID();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion =operaciones.NINGUNO;
                break;
            default:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de querer eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1, ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            tblPresupuestos.getSelectionModel().clearSelection();
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                       
                }else{
                    JOptionPane.showConfirmDialog(null, "Debe seleccionar un elemento primero", "Advertencia", JOptionPane.CANCEL_OPTION);
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnNuevo.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento primero");
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?)}");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fechaS.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPre.getText()));
            registro.setCodigoEmpresa(((Empresa)cmbCodEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    
    public void generarReporte(){
	switch(tipoDeOperacion){
		case NINGUNO:
			imprimirReporte();
			break;
	}
}
    
public void imprimirReporte(){
    if(tblPresupuestos.getSelectionModel().getSelectedItem() !=null){
	Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
	//int codEmpresa = Integer.valueOf(((Empresa)cmbCodEmpresa.getSelectionModel().getSelectionItem().getCodigoEmpresa());
	parametros.put("codEmpresa",codEmpresa);
	GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto",parametros);
                        }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento primero");
                }
                
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                activarBotones();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;

        }
    }
    
    public void activarBotones(){
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);

        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
    }

    public void desactivarBotones(){
        btnNuevo.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar.setVisible(false);
        btnReporte.setVisible(false);
    }
    
    public void activarID(){
        txtCodigoPre.setVisible(true);
        lblCodigoPre.setVisible(true);
    }

    public void desactivarID(){
        txtCodigoPre.setVisible(false);
        lblCodigoPre.setVisible(false);
    }
    
    
    public ObservableList<Empresa> getEmpresa(){
        toString();
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empresa ( resultado.getInt("codigoEmpresa"),
                                            resultado.getString("nombreEmpresa"),
                                            resultado.getString("direccion"),
                                            resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void limpiarControles(){
       txtCodigoPre.setText("");
       txtCantidadPre.setText("");
       cmbCodEmpresa.getSelectionModel().clearSelection();
    }
    
    public void desactivarControles(){
        txtCodigoPre.setEditable(false);
        txtCantidadPre.setEditable(false);
        cmbCodEmpresa.setDisable(false);
        grpfechaSolicitada.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoPre.setEditable(false);
        txtCantidadPre.setEditable(true);
        cmbCodEmpresa.setEditable(false);
        grpfechaSolicitada.setDisable(false);
    }
    
    public Principal getEscenarioPrincpal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal ){
        this.escenarioPrincipal = escenarioPrincipal;
    }
   
    public void ventanaEmpresas(){
         escenarioPrincipal.ventanaEmpresas();
     }
    
}