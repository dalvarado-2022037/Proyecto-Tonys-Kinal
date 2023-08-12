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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import org.douglasalvarado.bean.TipoEmpleado;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class TipoEmpleadoController implements Initializable{
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CACELAR, NINGUNO};
    private operaciones  tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpresa;
    private Button boton;
    private boolean selecionar = true;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnEmpleado;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgModificar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgReportes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
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
        switch(tipoDeOperacion){
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
                tipoDeOperacion = operaciones.GUARDAR;
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                selecionar = true;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void seleccionarElemento(){
        if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
            if(selecionar){
                txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
                txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
            }
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Elimar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                selecionar = true;
                break;
            case NINGUNO:
                if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                            procedimiento.setInt(1, ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpresa.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un dato");
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReportes.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    boton = btnModificar;
                }else
                    JOptionPane.showMessageDialog(null, "Selecciona un dato a actualizar");
                break;
            case ACTUALIZAR:
                if (txtCodigoTipoEmpleado.getText().isEmpty() || txtDescripcion.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "No deje datos vacios");
                else{
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnModificar.setText("Modificar");
                    btnReporte.setText("Reportes");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnAgregar.setVisible(true);
                    btnEliminar.setVisible(true);
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReportes.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                break;
            case NINGUNO:
                System.out.println("Hola");
                limpiarControles();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        if (txtDescripcion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");
        }else{
            TipoEmpleado registro = new TipoEmpleado();
            registro.setDescripcion(txtDescripcion.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                listaTipoEmpresa.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoEmpleado.clear();
        txtDescripcion.clear();
        tblTipoEmpleado.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrinciapal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    public void ejectar(){   
        txtDescripcion.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
