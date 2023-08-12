package org.douglasalvarado.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.douglasalvarado.bean.Empleado;
import org.douglasalvarado.bean.Servicio;
import org.douglasalvarado.bean.Servicios_has_Empleados;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class Servicios_has_EmpleadosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum TipoDeOperaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private TipoDeOperaciones operaciones = TipoDeOperaciones.NINGUNO;
    private ObservableList<Servicios_has_Empleados> listaServicios_has_Empleados;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private DatePicker fecha;
    private Button boton;
    private boolean selecionar = true;
    
    @FXML private TextField txtCodigoGeneral;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbServicio;
    @FXML private ComboBox cmbEmpleado;
    @FXML private TableView tblServicios_has_Empleados;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colLugar;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colServicio;
    @FXML private TableColumn colEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private Button btnActualizar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgActualizar;
    @FXML private GridPane grpFaltantes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/douglasalvarado/resource/TonysKinal.css");
        grpFaltantes.add(fecha,3,0);
        desactivarControles();
        cmbEmpleado.setItems(getEmpleados());
        cmbServicio.setItems(getServicios());
    }
    
    public void cargarDatos(){
        tblServicios_has_Empleados.setItems(getServicios_has_Empleados());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("Servicios_codigoServicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Date>("fechaEvento"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("lugarEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Time>("horaEvento"));
        colServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("codigoServicio"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("codigoEmpleado"));
    }
    
    public ObservableList<Servicios_has_Empleados> getServicios_has_Empleados(){
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Empleados()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {                
                lista.add(new Servicios_has_Empleados(resultado.getInt("Servicios_codigoServicio"), 
                                                      resultado.getInt("codigoServicio"), 
                                                      resultado.getInt("codigoEmpleado"), 
                                                      resultado.getDate("fechaEvento"), 
                                                      resultado.getTime("horaEvento"), 
                                                      resultado.getString("lugarEvento")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios_has_Empleados = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicio> getServicios(){
        ArrayList<Servicio> listaDatos = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaDatos.add(new Servicio(resultado.getInt("codigoServicio"),
                                            resultado.getDate("fechaServicio"),
                                            resultado.getString("tipoServicio"),
                                            resultado.getTime("horaServicio"),
                                            resultado.getString("lugarServicio"),
                                            resultado.getString("telefonoContacto"),
                                            resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(listaDatos);
    }
    
    public ObservableList<Empleado> getEmpleados(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimineto.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                                       resultado.getInt("numeroEmpleado"),
                                       resultado.getString("apellidosEmpleado"),
                                       resultado.getString("nombresEmpleado"),
                                       resultado.getString("direccionEmpleado"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getString("gradoCocinero"),
                                       resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if (selecionar) {
            if (tblServicios_has_Empleados.getSelectionModel().getSelectedItems() != null) {
                txtCodigoGeneral.setText(String.valueOf((
                    (Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                txtLugarEvento.setText(
                    ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento());
                txtHoraEvento.setText(String.valueOf(
                    ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
                cmbEmpleado.getSelectionModel().select(buscarEmpleado(
                    ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                cmbServicio.getSelectionModel().select(buscarServicio(
                    ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fecha.selectedDateProperty().set(
                    ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            }
        }
    }
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                         registro.getDate("fechaServicio"),
                                         registro.getString("tipoServicio"),
                                         registro.getTime("horaServicio"),
                                         registro.getString("lugarServicio"),
                                         registro.getString("telefonoContacto"),
                                         registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                         registro.getInt("numeroEmpleado"),
                                         registro.getString("apellidosEmpleado"),
                                         registro.getString("nombresEmpleado"),
                                         registro.getString("direccionEmpleado"),
                                         registro.getString("telefonoContacto"),
                                         registro.getString("gradoCocinero"),
                                         registro.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void nuevo(){
        switch (operaciones) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnActualizar.setDisable(true);
                btnReporte.setDisable(true);
                btnActualizar.setVisible(false);
                btnReporte.setVisible(false);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                operaciones = TipoDeOperaciones.GUARDAR;
                boton = btnAgregar;
                selecionar = false;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimiar");
                btnActualizar.setDisable(false);
                btnReporte.setDisable(false);
                btnActualizar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                cargarDatos();
                operaciones = TipoDeOperaciones.NINGUNO;
                selecionar = true;
                break;
        }
    }
    
    public void eliminar(){
        switch(operaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimiar");
                btnActualizar.setDisable(false);
                btnReporte.setDisable(false);
                btnActualizar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                operaciones = TipoDeOperaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblServicios_has_Empleados.getSelectionModel().getSelectedItems() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Servicios_has_Empleados",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios_has_Empleado(?)");
                            procedimiento.setInt(1, ((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaServicios_has_Empleados.remove(tblServicios_has_Empleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Selecione un dato");
                break;
        }
    }
    
    public void guardar(){
        if (txtLugarEvento.getText().isEmpty() || fecha.selectedDateProperty().getValue() == null || 
                cmbEmpleado.getSelectionModel().getSelectedItem() == null  || cmbServicio.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else if(!(txtHoraEvento.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$")))
            JOptionPane.showMessageDialog(null, "Formato de la fecha:\nHH:MM:SS");
        else{
            Servicios_has_Empleados registro = new Servicios_has_Empleados();
            registro.setCodigoEmpleado(((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            registro.setCodigoServicio(((Servicio)cmbServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(Time.valueOf(txtHoraEvento.getText()));
            registro.setLugarEvento(txtLugarEvento.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearServicios_has_Empleado(?,?,?,?,?)");
                procedimiento.setInt(1, registro.getCodigoServicio());
                procedimiento.setInt(2, registro.getCodigoEmpleado());
                procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setTime(4, registro.getHoraEvento());
                procedimiento.setString(5, registro.getLugarEvento());
                procedimiento.execute();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void editar(){
        switch(operaciones){
            case NINGUNO:
                if (tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null) {
                    btnActualizar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    imgActualizar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    activarControles();
                    cmbEmpleado.setDisable(false);
                    cmbServicio.setDisable(false);
                    operaciones = TipoDeOperaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                break;
            case ACTUALIZAR:
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnActualizar.setText("Modificar");
                    btnReporte.setText("Reporte");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnAgregar.setVisible(true);
                    btnEliminar.setVisible(true);
                    imgActualizar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                    cargarDatos();
                    operaciones = TipoDeOperaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        if (txtLugarEvento.getText().isEmpty() || txtHoraEvento.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else if(!(txtHoraEvento.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$")))
            JOptionPane.showMessageDialog(null, "Formato de la fecha:\nHH:MM:SS");
        else{
            Servicios_has_Empleados registro = new Servicios_has_Empleados();
            registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoGeneral.getText()));
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(Time.valueOf(txtHoraEvento.getText()));
            registro.setLugarEvento(txtLugarEvento.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarServicios_has_Empleado(?,?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setTime(3, registro.getHoraEvento());
                procedimiento.setString(4, registro.getLugarEvento());
                procedimiento.execute();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reporte(){
        switch (operaciones) {
            case NINGUNO:
                limpiarControles();
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnActualizar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgActualizar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                operaciones = TipoDeOperaciones.NINGUNO;
            break;
        }
    }
    
    public void limpiarControles(){
        txtCodigoGeneral.clear();
        txtLugarEvento.clear();
        txtHoraEvento.clear();
        fecha.selectedDateProperty().set(null);
        cmbEmpleado.getSelectionModel().select(null);
        cmbServicio.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        txtLugarEvento.setEditable(true);
        fecha.setDisable(false);
        txtHoraEvento.setEditable(true);
        cmbEmpleado.setDisable(false);
        cmbServicio.setDisable(false);
    }   
    
    public void desactivarControles(){
        txtLugarEvento.setEditable(false);
        fecha.setDisable(true);
        txtHoraEvento.setEditable(false);
        cmbEmpleado.setDisable(true);
        cmbServicio.setDisable(true);
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
