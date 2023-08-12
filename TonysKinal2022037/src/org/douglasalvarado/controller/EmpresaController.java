package org.douglasalvarado.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.douglasalvarado.bean.Empresa;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;
import org.douglasalvarado.report.GenerarReporte;


public class EmpresaController implements Initializable{
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones  tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenariPrincipal;
    private ObservableList<Empresa> listaEmpresa;
    private Button boton;
    private boolean selecionar = true;
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportes;
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
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
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
    
    public void seleccionarElemento()   {
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
            if(selecionar){
                txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
                txtDireccion.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
                txtTelefono.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
            }    
        }
    }
    
    public void nuevo(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReportes.setDisable(true);
                btnModificar.setVisible(false);
                btnReportes.setVisible(false);
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
                btnEliminar.setText("Elimiar");
                btnModificar.setDisable(false);
                btnReportes.setDisable(false);
                btnModificar.setVisible(true);
                btnReportes.setVisible(true);
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
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReportes.setDisable(false);
                btnModificar.setVisible(true);
                btnReportes.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                selecionar = true;
                break;
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try{
                            PreparedStatement procedimientoBusqueda = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPresupuesto(?)");
                            procedimientoBusqueda.setInt(1, Integer.parseInt(txtCodigoEmpresa.getText()));
                            ResultSet resultado = procedimientoBusqueda.executeQuery();
                            if(resultado.next()){
                                JOptionPane.showMessageDialog(null, "No se puede eliinar ya que esta ligado a otro");
                            }else{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                                procedimiento.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                                procedimiento.execute();
                                listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }   
                }else
                    JOptionPane.showMessageDialog(null, "Selecione un dato");
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    btnModificar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
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
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                break;
            case ACTUALIZAR:
                if (txtCodigoEmpresa.getText().isEmpty() || txtNombreEmpresa.getText().isEmpty()
                    || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "No deje datos vacios");
                }else if(!(txtTelefono.getText().replaceAll(" ", "")).matches("[0-9]*")){
                    JOptionPane.showMessageDialog(null, "Solo numeros en telefono porfavor");
                }else if ((txtTelefono.getText().replaceAll(" ", "")).length() <= 3){
                    JOptionPane.showMessageDialog(null, "Más de 3 números por favor");
                }else{
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnModificar.setText("Modificar");
                    btnReportes.setText("Reporte");
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
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;
            case ACTUALIZAR:
                btnModificar.setText("Modificar");
                btnReportes.setText("Reportes");
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
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresa.jasper", "Reporte de Empresa", parametros);
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setTelefono(txtTelefono.getText());
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){           
        if (txtNombreEmpresa.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");
        }else if ((txtTelefono.getText().replaceAll(" ", "")).matches("[0-9]*")){
            if((txtTelefono.getText().replaceAll(" ", "")).length() >= 9){
                JOptionPane.showMessageDialog(null, "Solo 8 números o menos y mas de 3 por favor");
            }else if ((txtTelefono.getText().replaceAll(" ", "")).length() <= 3){
                JOptionPane.showMessageDialog(null, "Más de 3 números por favor");
            }else{
                Empresa registro = new Empresa();
                //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
                registro.setNombreEmpresa(txtNombreEmpresa.getText());
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText().replaceAll(" ", ""));
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa(?,?,?)");
                    procedimiento.setString(1, registro.getNombreEmpresa());
                    procedimiento.setString(2, registro.getDireccion());
                    procedimiento.setString(3, registro.getTelefono());
                    procedimiento.execute();
                    listaEmpresa.add(registro);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else
            JOptionPane.showMessageDialog(null, "En telefono solo numeros");
    }
    
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        tblEmpresas.getSelectionModel().clearSelection();
    }

    public Principal getEscenariPrincipal() {
        return escenariPrincipal;
    }

    public void setEscenariPrincipal(Principal escenariPrincipal) {
        this.escenariPrincipal = escenariPrincipal;
    }
    
    public void menuPrincipal(){
        escenariPrincipal.menuPrincipal();
    }
    
    public void ejectar(){   
        txtTelefono.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtNombreEmpresa.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtDireccion.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
    
    public void ventanaPresupuesto(){
        escenariPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaServicio(){
        escenariPrincipal.ventanaServicio();
    }
}
