package org.douglasalvarado.controller;

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
import org.douglasalvarado.bean.Presupuesto;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;
import org.douglasalvarado.report.GenerarReporte;


public class PresupuestoController implements Initializable {
    private Principal escenariPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto; 
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    private Button boton;
    private boolean selecionar = true;
    private final String fondo = ("/org/douglasalvarado/image/FondoReporte.jpg");
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbTipoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgModificar;
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/douglasalvarado/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbTipoEmpresa.setItems(getEmpresa());
        desacivarControler();
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElemento(){
        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
            if(selecionar){ 
                txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
                fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
                txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
                cmbTipoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            }
        }
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
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
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                          resultado.getDate("fechaSolicitud"),
                                          resultado.getDouble("cantidadPresupuesto"),
                                          resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
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
        switch (tipoDeOperaciones) {
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
                tipoDeOperaciones = operaciones.GUARDAR;
                boton = btnAgregar;
                selecionar = false;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desacivarControler();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimiar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                selecionar = true;
                break;
        }
    }
    
    public void guardar(){
        if (txtCantidadPresupuesto.getText().isEmpty() || fecha.selectedDateProperty().getValue() == null )
            JOptionPane.showMessageDialog(null, "No deje datos vacios");
        else if(!(txtCantidadPresupuesto.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Usar solo numeros por favor");
        else{
            Presupuesto registro = new Presupuesto();
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Integer.parseInt(txtCantidadPresupuesto.getText().replaceAll(" ", "")));
            registro.setCodigoEmpresa(((Empresa)cmbTipoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearPresupuesto(?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(2, registro.getCantidadPresupuesto());
                procedimiento.setInt(3, registro.getCodigoEmpresa());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }   
    }
    
    public void eliminar(){
        switch (tipoDeOperaciones) {
            case GUARDAR:
                limpiarControles();
                desacivarControler();          
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimiar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                selecionar = true;
                break;
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else
                        limpiarControles();
                }else
                    JOptionPane.showMessageDialog(null, "Selecione un dato");
            break;
        }
    }
    
    public void editar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    activarControles();
                    cmbTipoEmpresa.setDisable(true);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    boton = btnModificar;
                }else
                    JOptionPane.showMessageDialog(null, "Selecione un dato");
                break;
            case ACTUALIZAR:
                if (txtCodigoPresupuesto.getText().isEmpty() || txtCantidadPresupuesto.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "No deje datos vacios");
                else if(!(txtCodigoPresupuesto.getText().replaceAll(" ", "").matches("[0-9]*")))
                    JOptionPane.showMessageDialog(null, "Solo numeros en cantidad porfavor");
                else{
                    actualizar();
                    limpiarControles();
                    desacivarControler();
                    btnModificar.setText("Modificar");
                    btnReporte.setText("Reporte");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnAgregar.setVisible(true);
                    btnEliminar.setVisible(true);
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                    cargarDatos();
                    tipoDeOperaciones = operaciones.NINGUNO;
                }
                break;
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;
            case ACTUALIZAR:
                limpiarControles();
                desacivarControler();
                btnModificar.setText("Modificar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        if (tblPresupuestos.getSelectionModel().getSelectedItems() != null) {
            Map parametros = new HashMap();
            int codEmpresa = Integer.valueOf(((Empresa)cmbTipoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            //int codEmpresa = (((Empresa)cmbTipoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            parametros.put("codEmpresa", codEmpresa);
            GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte de Empresa", parametros);
        }else 
            JOptionPane.showMessageDialog(null, "Selecione un dato");
    }
    
    public void actualizar(){
        try {
            Presupuesto registro = new Presupuesto();
            registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarPresupuesto(?,?,?)");
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void desacivarControler(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        cmbTipoEmpresa.setDisable(true);
        fecha.setDisable(true);
    }
    
    public void activarControles(){
        txtCantidadPresupuesto.setEditable(true);
        cmbTipoEmpresa.setDisable(false);
        fecha.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.clear();
        txtCantidadPresupuesto.clear();
        cmbTipoEmpresa.getSelectionModel().select(null);
        fecha.selectedDateProperty().set(null);
    }
    
    public Principal getEscenariPrincipal() {
        return escenariPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenariPrincipal) {
        this.escenariPrincipal = escenariPrincipal;
    }
    
    public void empresa(){
        escenariPrincipal.ventanaEmpresa();
    }
    
     public void ejectar(){   
        txtCantidadPresupuesto.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
