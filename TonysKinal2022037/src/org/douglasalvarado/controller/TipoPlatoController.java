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
import org.douglasalvarado.bean.TipoPlato;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class TipoPlatoController implements Initializable{
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;
    private Button boton;
    private boolean selecionar = true;
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtNombreProducto;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colNombrePlato;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgModificar;
    @FXML private ImageView imgReportes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionPlato"));
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTiposPlatos()");
            ResultSet resultado = procedimineto.executeQuery();
            while (resultado.next()) {                
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                        resultado.getString("descripcionPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
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
        if (selecionar) {
            if (tblTipoPlato.getSelectionModel().getSelectedItem() != null) {
                txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
                txtNombreProducto.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            }
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
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
                tipoDeOperacion = operaciones.NINGUNO;
                selecionar = true;
                break;
            case NINGUNO:
                if (tblTipoPlato.getSelectionModel().getSelectedItems() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement proccedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                            proccedimiento.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            proccedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
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
                if (tblTipoPlato.getSelectionModel().getSelectedItem() != null) {
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
                    JOptionPane.showMessageDialog(null, "Selecione un dato a actualizar");
                break;
            case ACTUALIZAR:
                if (txtCodigoTipoPlato.getText().isEmpty() || txtNombreProducto.getText().isEmpty()) 
                    JOptionPane.showMessageDialog(null, "No deje datos vacios");
                else{
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
                    imgReportes.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void reportes(){
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
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case NINGUNO:
                limpiarControles();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
            registro.setDescripcionPlato(txtNombreProducto.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionPlato());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        if (txtNombreProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");
        }else{
            TipoPlato registro = new TipoPlato();
            registro.setDescripcionPlato(txtNombreProducto.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
                procedimiento.setString(1, registro.getDescripcionPlato());
                procedimiento.execute();
                listaTipoPlato.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtNombreProducto.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.clear();
        txtNombreProducto.clear();
        tblTipoPlato.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    
    public void ejectar(){   
        txtNombreProducto.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
