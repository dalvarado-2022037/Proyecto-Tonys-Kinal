package org.douglasalvarado.controller;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import org.douglasalvarado.bean.Empleado;
import org.douglasalvarado.bean.TipoEmpleado;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class EmpleadoController implements Initializable{
    private enum tipoDeOperaciones {NUEVO,ELIMINAR,ACTUALIZAR,NINGUNO};
    private tipoDeOperaciones operaciones = tipoDeOperaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpresa;
    private Button boton;
    private boolean seleccionar = true;
    
    @FXML private TextField txtCodigocEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbTipoEmpleado;
    @FXML private TableView tblEmpleado;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colTipoEmpleado;
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
        cmbTipoEmpleado.setItems(getTipoEmpleado());
    }
    
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory <Empleado, Integer>("numeroEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("codigoTipoEmpleado"));
    }
    
    public void seleccionarElemento(){
        if (tblEmpleado.getSelectionModel().getSelectedItems() != null) {
            if (seleccionar) {
                txtCodigocEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
                txtNombresEmpleado.setText((((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
                txtApellidosEmpleado.setText((((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
                txtDireccionEmpleado.setText((((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
                txtTelefonoContacto.setText((((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
                txtGradoCocinero.setText((((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getGradoCocinero()));
                cmbTipoEmpleado.getSelectionModel().select(buscarEmpleado((((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado())));
            }
        }
    }
    
    public TipoEmpleado buscarEmpleado(int busqueda){
        TipoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BucarTipoEmpleado(?)");
            procedimiento.setInt(1, busqueda);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {                
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"), 
                                             registro.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados()");
            ResultSet resultado = procedimineto.executeQuery();
            while (resultado.next()) {                
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                           resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch (operaciones) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guadar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                btnModificar.setVisible(false);
                btnReporte.setVisible(false);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                operaciones = tipoDeOperaciones.NUEVO;
                boton = btnAgregar;
                seleccionar = false;
                break;
            case NUEVO:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimiar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                operaciones = tipoDeOperaciones.NINGUNO;
                cargarDatos();
                seleccionar = true;
                break;
        }
    }
    
    public void guardar(){
        if (txtNumeroEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() ||
                txtApellidosEmpleado.getText().isEmpty() || txtDireccionEmpleado.getText().isEmpty() ||
                txtGradoCocinero.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else if(!(txtNumeroEmpleado.getText().matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en Numero de Empleado");
        else if(!(txtTelefonoContacto.getText().matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en Telefono de Empleado");
        else if(cmbTipoEmpleado.getSelectionModel().isEmpty())
            JOptionPane.showMessageDialog(null, "Selecione un tipo de empleado");
        else{
            Empleado registro = new Empleado();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearEmpleado(?,?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getNumeroEmpleado());
                procedimiento.setString(2, registro.getApellidosEmpleado());
                procedimiento.setString(3, registro.getNombresEmpleado());
                procedimiento.setString(4, registro.getDireccionEmpleado());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setString(6, registro.getGradoCocinero());
                procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void eliminar(){
        switch (operaciones) {
            case NINGUNO:
                if (tblEmpleado.getSelectionModel().getSelectedItems() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, (((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else
                        limpiarControles();
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un dato a eliminar");
                break;
            case NUEVO:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimiar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                operaciones = tipoDeOperaciones.NINGUNO;
                seleccionar = true;
                break;
        }
    }
    
    public void editar(){
        switch (operaciones) {
            case NINGUNO:
                if (tblEmpleado.getSelectionModel().getSelectedItems() != null) {
                    activarControles(); 
                    cmbTipoEmpleado.setDisable(true);
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    operaciones = tipoDeOperaciones.ACTUALIZAR;
                    boton = btnModificar;
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un dato a modificar");
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                cargarDatos();
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
        }
    }
    
    public void reporte(){
        switch (operaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnModificar.setText("Modificar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
            case NINGUNO:
                limpiarControles();
                break;
        }
    }
    
    public void actualizar(){
        if (txtNumeroEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() ||
                txtApellidosEmpleado.getText().isEmpty() || txtDireccionEmpleado.getText().isEmpty() ||
                txtGradoCocinero.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else if(!(txtNumeroEmpleado.getText().matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en Numero de Empleado");
        else if(!(txtTelefonoContacto.getText().matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en Telefono de Empleado");
        else{
            Empleado registro = new Empleado();
            registro.setCodigoEmpleado(Integer.parseInt(txtCodigocEmpleado.getText()));
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarEmpleado(?,?,?,?,?,?,?);");
                procedimiento.setInt(1, registro.getCodigoEmpleado());
                procedimiento.setInt(2, registro.getNumeroEmpleado());
                procedimiento.setString(3, registro.getApellidosEmpleado());
                procedimiento.setString(4, registro.getNombresEmpleado());
                procedimiento.setString(5, registro.getDireccionEmpleado());
                procedimiento.setString(6, registro.getTelefonoContacto());
                procedimiento.setString(7, registro.getGradoCocinero());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void limpiarControles(){
        txtCodigocEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtGradoCocinero.clear();
        txtNombresEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtTelefonoContacto.clear();
        cmbTipoEmpleado.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        txtNumeroEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbTipoEmpleado.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigocEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbTipoEmpleado.setDisable(true);
    }
    
    public Principal getEscearioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrinciplar(Principal esenarioPrincipal){
        this.escenarioPrincipal = esenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ejectar(){   
        txtApellidosEmpleado.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtDireccionEmpleado.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtGradoCocinero.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtNombresEmpleado.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtNumeroEmpleado.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtTelefonoContacto.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
