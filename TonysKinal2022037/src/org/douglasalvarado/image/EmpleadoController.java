/*package org.douglasalvarado.image;

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
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class EmpleadoController implements Initializable{
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empleado> listaEmpleado;
    private ArrayList<String> listaTipoEmpleados  = new ArrayList<String>();
    private ArrayList<Integer> idListaTipoEmpleados = new ArrayList<Integer>();
    private Button boton;
    private boolean selecionar = true;
    @FXML private TextField txtCodigocEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
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
    @FXML private ComboBox<String> cmbTipoEmpleado;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        datosComboBox();        
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("codigoTipoEmpleado"));
    }
    
    public void datosComboBox(){
        try {
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados()");
            ResultSet resultado = procedimineto.executeQuery();
            while (resultado.next()) {                
                idListaTipoEmpleados.add(resultado.getInt("codigoTipoEmpleado"));
                listaTipoEmpleados.add(resultado.getString("descripcion"));
            }
            cmbTipoEmpleado.setItems(FXCollections.observableArrayList(listaTipoEmpleados));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados()");
                ResultSet resultado = procedimiento.executeQuery();
                PreparedStatement procedimientoTipo = Conexion.getInstance().getConexion().prepareCall("call sp_BucarTipoEmpleado(?)");
                while (resultado.next()) {
                    procedimientoTipo.setInt(1, resultado.getInt("codigoTipoEmpleado"));
                    ResultSet resultadoTipo = procedimientoTipo.executeQuery();
                    if (resultadoTipo.next()) {
                        lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                                                resultado.getInt("numeroEmpleado"),
                                                resultado.getString("apellidosEmpleado"),
                                                resultado.getString("nombresEmpleado"),
                                                resultado.getString("direccionEmpleado"),
                                                resultado.getString("telefonoContacto"),
                                                resultado.getString("gradoCocinero"),
                                                resultadoTipo.getString("descripcion")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return listaEmpleado = FXCollections.observableArrayList(lista);
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
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
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
    
    public void seleccionarElemento(){
        if (tblEmpleado.getSelectionModel().getSelectedItem() != null) {
            if(selecionar){
                txtCodigocEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
                txtNombresEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombresEmpleado());
                txtApellidosEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
                txtDireccionEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
                txtTelefonoContacto.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                txtGradoCocinero.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getGradoCocinero());
                cmbTipoEmpleado.getSelectionModel().select(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            }
        }
    }
    
    public void elimininar(){
        switch (tipoDeOperaciones) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
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
                if (tblEmpleado.getSelectionModel().getSelectedItem() !=  null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un dato");
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void editar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblEmpleado.getSelectionModel().getSelectedItem() != null) {
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    activarControles();
                    cmbTipoEmpleado.setDisable(true);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
                break;
            case ACTUALIZAR:
                if (txtCodigocEmpleado.getText().isEmpty() || txtNumeroEmpleado.getText().isEmpty() ||
                    txtNombresEmpleado.getText().isEmpty() || txtApellidosEmpleado.getText().isEmpty() ||
                    txtDireccionEmpleado.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() ||
                    txtGradoCocinero.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "No deje datis vacios");
                else if(!(txtNumeroEmpleado.getText().replaceAll(" ", "")).matches("[0-9]*"))
                    JOptionPane.showMessageDialog(null, "Solo números en Número de empleado");
                else if(!(txtTelefonoContacto.getText().replaceAll(" ", "")).matches("[0-9]*"))
                    JOptionPane.showMessageDialog(null, "Solo números en Telefono de empleado");
                else if ((txtTelefonoContacto.getText().replaceAll(" ", "")).length() <= 3)
                    JOptionPane.showMessageDialog(null, "Más de 3 números por favor");
                else{
                    actualizar();
                    limpiarControles();
                    desactivarControles();
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
            default:
                throw new AssertionError();
        }
    }
    
    public void reporte(){
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                btnModificar.setText("Modificar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            case NINGUNO:
                limpiarControles();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void actualizar(){
        if (txtNumeroEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() ||
            txtApellidosEmpleado.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() ||
            txtDireccionEmpleado.getText().isEmpty() || txtGradoCocinero.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Llenar todos los datos");
        else if(!(txtNumeroEmpleado.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo números por favor en número empleado");
        else if(!(txtTelefonoContacto.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo números por favor en telefono");
        else if((txtTelefonoContacto.getText().replaceAll(" ", "")).length() >= 9)
            JOptionPane.showMessageDialog(null, "Solo 8 números por favor");
        else if(cmbTipoEmpleado.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "Seleccione un tipo de empleado");
        else{
            try {
                PreparedStatement procedimiento = 
                        Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarEmpleado(?,?,?,?,?,?,?)");
                Empleado registro = (Empleado)tblEmpleado.getSelectionModel().getSelectedItem();
                registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                registro.setNombresEmpleado(txtApellidosEmpleado.getText());
                registro.setApellidosEmpleado(txtNombresEmpleado.getText());
                registro.setTelefonoContacto(txtTelefonoContacto.getText());
                registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
                registro.setGradoCocinero(txtGradoCocinero.getText());
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
    
    public void guardar(){
        if (txtNumeroEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() ||
            txtApellidosEmpleado.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() ||
            txtDireccionEmpleado.getText().isEmpty() || txtGradoCocinero.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Llenar todos los datos");
        else if(!(txtNumeroEmpleado.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo números por favor en número empleado");
        else if(!(txtTelefonoContacto.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo números por favor en telefono");
        else if((txtTelefonoContacto.getText().replaceAll(" ", "")).length() >= 9)
            JOptionPane.showMessageDialog(null, "Solo 8 números por favor");
        else if(cmbTipoEmpleado.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "Seleccione un tipo de empleado");
        else{
            Empleado registro = new Empleado();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText().replaceAll(" ", ""));
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(String.valueOf(idListaTipoEmpleados.get(cmbTipoEmpleado.getSelectionModel().getSelectedIndex())));                
            try {
                PreparedStatement procedimiento = 
                    Conexion.getInstance().getConexion().prepareCall("call sp_CrearEmpleado(?,?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getNumeroEmpleado());
                procedimiento.setString(2, registro.getApellidosEmpleado());
                procedimiento.setString(3, registro.getNombresEmpleado());
                procedimiento.setString(4, registro.getDireccionEmpleado());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setString(6, registro.getGradoCocinero());
                procedimiento.setInt(7, Integer.parseInt(registro.getCodigoTipoEmpleado()));
                procedimiento.execute();
                listaEmpleado.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtCodigocEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbTipoEmpleado.setDisable(true);
    }
    
    public void activarControles(){
        txtNumeroEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbTipoEmpleado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigocEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtNombresEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtTelefonoContacto.clear();
        txtDireccionEmpleado.clear();
        txtGradoCocinero.clear();
        tblEmpleado.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenaroPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenaroPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ejectar(){   
        txtNombresEmpleado.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
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
*/