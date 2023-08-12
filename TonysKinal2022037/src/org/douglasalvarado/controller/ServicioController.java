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
import java.util.Timer;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.douglasalvarado.bean.Empresa;
import org.douglasalvarado.bean.Servicio;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;
import org.douglasalvarado.report.GenerarReporte;

public class ServicioController implements Initializable{
    private enum tipoDeOperaciones {NUEVO, ELIMINAR, ACTUALIZAR, NINGUNO};
    private tipoDeOperaciones operaciones = tipoDeOperaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    private Timer hora;
    private Button boton;
    private boolean selecionar = true;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtHoraServicio;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colTipo;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colLugar;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;    
    @FXML private ImageView imgEliminar;    
    @FXML private ImageView imgModificar;    
    @FXML private ImageView imgReporte;   
    @FXML private GridPane grpFecha;   
    @FXML private GridPane grpHora;   
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/douglasalvarado/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("tipoServicio"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("horaServicio"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("lugarServicio"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElemento(){
            
        if (tblServicios.getSelectionModel().getSelectedItems() != null) {
            if (selecionar) {
                txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
                txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
                txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                txtHoraServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
                fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            }
        }
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empresa(registro.getInt("codigoEmpresa"), 
                                        registro.getString("nombreEmpresa"), 
                                        registro.getString("direccion"), 
                                        registro.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                      resultado.getString("nombreEmpresa"),
                                      resultado.getString("direccion"),
                                      resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch (operaciones) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                btnModificar.setVisible(false);
                btnReporte.setVisible(false);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                operaciones = operaciones.NUEVO;
                boton = btnAgregar;
                selecionar = false;
                break;
            case NUEVO:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                operaciones = operaciones.NINGUNO;
                cargarDatos();
                selecionar = true;
                break;
        }
    }
    
    public void guardar(){
        if (txtTipoServicio.getText().isEmpty() || txtLugarServicio.getText().isEmpty() || 
                txtTelefonoContacto.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else if(!(txtTelefonoContacto.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en telefono");
        else if(cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "Seleccione un valor en empresa");
        else if(fecha.getSelectedDate() == null)
            JOptionPane.showMessageDialog(null, "Selecione una fecha");
        else if(!(txtHoraServicio.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$")))
            JOptionPane.showMessageDialog(null, "Formato de fecha: \nHH:MM:SS");
        else{
            Servicio registro = new Servicio();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(Time.valueOf(txtHoraServicio.getText()));
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearServicio(?,?,?,?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(2, registro.getTipoServicio());
                procedimiento.setTime(3, registro.getHoraServicio());
                procedimiento.setString(4, registro.getLugarServicio());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setInt(6, registro.getCodigoEmpresa());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void eliminar(){
        switch (operaciones) {
            case NUEVO:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Reporte");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                operaciones = operaciones.NINGUNO;
                cargarDatos();
                selecionar = true;
                break;
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItems() == null)
                    JOptionPane.showMessageDialog(null, "Selecione un dato");
                else{
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                            procedimiento.setInt(1, Integer.parseInt(txtCodigoServicio.getText()));
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else
                        limpiarControles();
                }
                break;
        }
    }
    
    public void editar(){
        switch (operaciones) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItems() != null) {
                    activarControles();
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    boton = btnModificar;
                    cmbCodigoEmpresa.setDisable(true);
                    operaciones = tipoDeOperaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Selecciones un dato a modificar");
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                cargarDatos();
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
        }
    }
    
    public void reporte(){
        switch (operaciones) {
            case NINGUNO:
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                cargarDatos();
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        if (txtTipoServicio.getText().isEmpty() || txtLugarServicio.getText().isEmpty() || 
                txtTelefonoContacto.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else if(!(txtTelefonoContacto.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en telefono");
        else if(cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "Seleccione un valor en empresa");
        else if(fecha.getSelectedDate() == null)
            JOptionPane.showMessageDialog(null, "Selecione una fecha");
        else if(!(txtHoraServicio.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$"))) 
            JOptionPane.showMessageDialog(null, "Formato de fecha: \nHH:MM:SS");
        else{
            Servicio registro = new Servicio();
            registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(Time.valueOf(txtHoraServicio.getText()));
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText().replaceAll(" ", ""));
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());           
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarServicio(?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getCodigoServicio());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(3, registro.getTipoServicio());
                procedimiento.setTime(4, registro.getHoraServicio());
                procedimiento.setString(5, registro.getLugarServicio());
                procedimiento.setString(6, registro.getTelefonoContacto());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void activarControles(){
        txtTipoServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtHoraServicio.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);        
        fecha.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtHoraServicio.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.clear();
        txtTipoServicio.clear();
        txtLugarServicio.clear();
        txtTelefonoContacto.clear();
        txtHoraServicio.clear();
        fecha.selectedDateProperty().set(null);
        cmbCodigoEmpresa.getSelectionModel().select(null);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ejectar(){   
        txtLugarServicio.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtTelefonoContacto.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtTipoServicio.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
