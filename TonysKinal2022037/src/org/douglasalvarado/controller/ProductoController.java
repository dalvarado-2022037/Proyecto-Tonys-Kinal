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
import org.douglasalvarado.bean.Producto;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;

public class ProductoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;
    private Button boton;
    private boolean selecionar = true;
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
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
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    
    public ObservableList<Producto> getProductos(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {                
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("catidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarCotroles();
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
                selecionar = false;
                boton = btnAgregar;
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
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                selecionar = true;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void seleccionarElemento(){
        if (tblProductos.getSelectionModel().getSelectedItem() != null) {
            if(selecionar){    
                txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
                txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
            }
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agreagrar");
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
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProductos(?)");
                            procedimiento.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else
                        limpiarControles();
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un dato");
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    btnModificar.setText("Actualiar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    activarCotroles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    boton = btnModificar;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                break;
            case ACTUALIZAR:
                if (txtCodigoProducto.getText().isEmpty() || txtNombreProducto.getText().isEmpty() ||
                    txtCantidad.getText().isEmpty()) 
                    JOptionPane.showMessageDialog(null, "No deje datos vacios");
                else if(!(txtCantidad.getText().replaceAll(" ", "").matches("[0-9]*"))){
                    JOptionPane.showMessageDialog(null, "En cantidad solo números");
                }
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
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void reporte(){
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnAgregar.setVisible(true);
                btnEliminar.setVisible(true);
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                break;
            case NINGUNO:
                break;
            default:
                throw new AssertionError();
        }
                
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarProducto(?,?,?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText().replaceAll(" ","")));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        if (txtNombreProducto.getText().isEmpty() || txtCantidad.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");
        else if(!(txtCantidad.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "En cantidad solo números");
        else{
            Producto registro = new Producto();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText().replaceAll(" ","")));
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearProducto(?,?)");
                procedimiento.setString(1, registro.getNombreProducto());
                procedimiento.setInt(2, registro.getCantidad());
                procedimiento.execute();
                listaProducto.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
    
    public void activarCotroles(){
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidad.clear();
        tblProductos.getSelectionModel().clearSelection();
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
    
    public void ejectar(){   
        txtCantidad.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtNombreProducto.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
