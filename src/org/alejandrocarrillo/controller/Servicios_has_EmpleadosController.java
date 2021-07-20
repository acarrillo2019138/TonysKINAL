
package org.alejandrocarrillo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alejandrocarrillo.bean.Empleados;
import org.alejandrocarrillo.bean.Servicio;
import org.alejandrocarrillo.bean.Servicios_has_Empleados;
import org.alejandrocarrillo.db.Conexion;
import org.alejandrocarrillo.system.Principal;


public class Servicios_has_EmpleadosController implements Initializable {
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios_has_Empleados> listaServicioHasEmpleado;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleados> listaEmpleado;
    private DatePicker fecha;
    @FXML private TextField txtcodHas;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private GridPane grpFechaEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colcodHas;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/alejandrocarrillo/resource/DatePicker.css");
        grpFechaEvento.add(fecha, 3, 0);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
    }
   
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServicios_has_Empleados());
        colcodHas.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codhasServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("lugarEvento"));
    }
     
     public void seleccionarElemento(){
         if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
        txtcodHas.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodhasServicio()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        fecha.selectedDateProperty().set(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtHoraEvento.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
        txtLugarEvento.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));      
    }}
     
     public ObservableList<Servicios_has_Empleados> getServicios_has_Empleados(){
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Empleados(resultado.getInt("codhasServicio"),
                                                     resultado.getInt("codigoServicio"),
                                                     resultado.getInt("codigoEmpleado"),
                                                     resultado.getDate("fechaEvento"),
                                                     resultado.getString("horaEvento"),
                                                     resultado.getString("lugarEvento")));               
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicioHasEmpleado = FXCollections.observableArrayList(lista);
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
     
      public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
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
    
      public ObservableList <Empleados> getEmpleado(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados(resultado.getInt  ("codigoEmpleado"),
                                       resultado.getInt   ("numeroEmpleado"),
                                       resultado.getString("apellidosEmpleado"),
                                       resultado.getString("nombresEmpleado"),
                                       resultado.getString("direccionEmpleado"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getString("gradoCocinero"),
                                       resultado.getInt   ("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    } 
      
        public Empleados buscarEmpleado(int codigoEmpleado){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleados(registro.getInt("codigoEmpleado"),
                                       registro.getInt("numeroEmpleado"),
                                       registro.getString("apellidosEmpleado"),
                                       registro.getString("nombresEmpleado"),
                                       registro.getString("direccionEmpleado"),
                                       registro.getString("telefonoContacto"),
                                       registro.getString("gradoCocinero"),
                                       registro.getInt("codigoTipoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         return resultado;
    }
    

public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                //deseleccionarElemento();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tblServiciosHasEmpleados.setDisable(false);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
               if(cmbCodigoServicio.getSelectionModel().getSelectedItem() == null){
                JOptionPane.showMessageDialog(null, "Ingrese un Codigo de Servicio");   
            }else if(cmbCodigoEmpleado.getSelectionModel().getSelectedItem() == null){
                JOptionPane.showMessageDialog(null, "Ingrese un Codigo de Empleado");
            }else if(fecha.getSelectedDate()== null){
                JOptionPane.showMessageDialog(null, "Ingrese una Fecha");
            }else if(txtHoraEvento.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese la Hora");
            }else if(txtLugarEvento.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Ingrese un Lugar");
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
        Servicios_has_Empleados registro = new Servicios_has_Empleados();
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleados(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2, registro.getCodigoEmpleado());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4, registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());
           
            procedimiento.execute();
            listaServicioHasEmpleado.add(registro);
            
                cargarDatos();
        } catch (Exception e) {
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
            if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
                int confirmacion = JOptionPane.showConfirmDialog(null,"Esta Seguro de eliminar","Eliminar Servicio Has Empleados",JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(confirmacion == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleados(?)}");
                        procedimiento.setInt(1, ((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodhasServicio());
                        procedimiento.execute();
                        listaServicioHasEmpleado.remove(tblServiciosHasEmpleados.getSelectionModel().getSelectedIndex());
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
            if (tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleados(?,?,?,?)}");
             Servicios_has_Empleados registro =(Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem();
        
             registro.setFechaEvento((fecha.getSelectedDate()));
             registro.setHoraEvento((txtHoraEvento.getText()));
             registro.setLugarEvento((txtLugarEvento.getText()));
             procedimiento.setInt(1, registro.getCodhasServicio());
             procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
             procedimiento.setString(3, registro.getHoraEvento());
             procedimiento.setString(4, registro.getLugarEvento());
             procedimiento.execute();
                cargarDatos();
        }catch(Exception e){
             e.printStackTrace();
        }
    }

 public void limpiarControles(){
        txtcodHas.setText("");
        cmbCodigoServicio.getSelectionModel().select(null);
        cmbCodigoEmpleado.getSelectionModel().select(null);
        fecha.selectedDateProperty().set(null);
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
    }
   
    public void desactivarControles(){
        txtcodHas.setEditable(false);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        grpFechaEvento.setDisable(false);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
    }
   
    public void activarControles(){
        txtcodHas.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoEmpleado.setEditable(false);
        grpFechaEvento.setDisable(false);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
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
