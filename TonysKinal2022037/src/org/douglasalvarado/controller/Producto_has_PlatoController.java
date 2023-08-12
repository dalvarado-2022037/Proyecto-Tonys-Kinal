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
import javax.swing.JOptionPane;
import org.douglasalvarado.bean.Plato;
import org.douglasalvarado.bean.Producto;
import org.douglasalvarado.bean.Productos_has_Platos;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class Producto_has_PlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum TipoDeOperaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private TipoDeOperaciones operaciones = TipoDeOperaciones.NINGUNO;
    private ObservableList<Productos_has_Platos> listaProductos_has_Platos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    private Button boton;
    private boolean seleccionar = true;
    
    @FXML private TextField txtCodigoProductos_codigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblProductosHasPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colPlato;
    @FXML private TableColumn colProducto;
    @FXML private Button btnAgregar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControler();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProductos());
    }
    
    public void cargarDatos(){
        tblProductosHasPlatos.setItems(getProductos_Has_Platos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("Productos_codigoProducto"));
        colPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoPlato"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoProducto"));
    }
    
    public ObservableList<Productos_has_Platos> getProductos_Has_Platos(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos_has_Platos(resultado.getInt("Productos_codigoProducto"), 
                                                   resultado.getInt("codigoPlato"), 
                                                   resultado.getInt("codigoProducto")));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos_has_Platos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> listaDatos = new ArrayList<Plato>(); 
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {                
                listaDatos.add(new Plato(resultado.getInt("codigoPlato"), 
                                         resultado.getInt("cantidadPlato"), 
                                         resultado.getString("nombrePlato"), 
                                         resultado.getString("descripcionPlato"), 
                                         resultado.getDouble("precioPlato"), 
                                         resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(listaDatos);
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
    
    public void seleccionarElemento(){
        if (seleccionar) {
            if (tblProductosHasPlatos.getSelectionModel().getSelectedItems() != null) {
                txtCodigoProductos_codigoProducto.setText(
                        String.valueOf(((Productos_has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
                cmbCodigoPlato.getSelectionModel().select(
                        buscarPlato(((Productos_has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                cmbCodigoProducto.getSelectionModel().select(
                        buscarProducto(((Productos_has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            }
        }
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                                      registro.getInt("cantidadPlato"),
                                      registro.getString("nombrePlato"),
                                      registro.getString("descripcionPlato"),
                                      registro.getDouble("precioPlato"),
                                      registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"),
                                      registro.getString("nombreProducto"),
                                      registro.getInt("catidad"));
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
                btnReporte.setText("Cancelar");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                operaciones = TipoDeOperaciones.GUARDAR;
                boton = btnAgregar;
                seleccionar = false;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControler();
                btnAgregar.setText("Agregar");
                btnReporte.setText("Reporte");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                operaciones = TipoDeOperaciones.NINGUNO;
                cargarDatos();
                seleccionar = true;
                break;
        }
    }
    
    public void guardar(){
        if (cmbCodigoPlato.getSelectionModel().getSelectedItem() == null ||
             cmbCodigoPlato.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else{
            Productos_has_Platos registro = new Productos_has_Platos();
            registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
            registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearProductos_has_Plato(?,?)");
                procedimiento.setInt(1, registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCodigoProducto());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reporte(){
        switch(operaciones){
            case GUARDAR:
                limpiarControles();
                desactivarControler();
                btnAgregar.setText("Agregar");
                btnReporte.setText("Reporte");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                operaciones = TipoDeOperaciones.NINGUNO;
                seleccionar = true;
                break;
            case NINGUNO:
                limpiarControles();
                break;
        }
    }
    
    public void limpiarControles(){
        txtCodigoProductos_codigoProducto.clear();
        cmbCodigoPlato.getSelectionModel().select(null);
        cmbCodigoProducto.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    
    public void desactivarControler(){
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
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
