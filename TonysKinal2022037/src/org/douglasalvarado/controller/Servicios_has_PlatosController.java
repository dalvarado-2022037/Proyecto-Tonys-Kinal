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
import org.douglasalvarado.bean.Servicio;
import org.douglasalvarado.bean.Servicios_has_Platos;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class Servicios_has_PlatosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum TipoDeOperaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private TipoDeOperaciones operaciones = TipoDeOperaciones.NINGUNO;
    private ObservableList<Servicios_has_Platos> listaServicios_has_Platos;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Plato> listaPlato;
    private Button boton;
    private boolean selecionar = true;
    
    @FXML private TextField txtServicios_codigoServicio;
    @FXML private ComboBox cmbPlato;
    @FXML private ComboBox cmbServicio;
    @FXML private TableView tblServicios_has_Platos;
    @FXML private TableColumn colCodigoGeneral;
    @FXML private TableColumn colPlato;
    @FXML private TableColumn colServicio;
    @FXML private Button btnAgregar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbPlato.setItems(getPlato());
        cmbServicio.setItems(getServicios());
    }
    
    public void cargarDatos(){
        tblServicios_has_Platos.setItems(getServicios_has_Platos());
        colCodigoGeneral.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("Servicios_codigoServicio"));
        colPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("codigoPlato"));
        colServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("codigoServicio"));
    }
    
    public ObservableList<Servicios_has_Platos> getServicios_has_Platos(){
        ArrayList<Servicios_has_Platos> registro = new ArrayList<Servicios_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro.add(new Servicios_has_Platos(resultado.getInt("Servicios_codigoServicio"), 
                                                      resultado.getInt("codigoPlato"),
                                                      resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios_has_Platos = FXCollections.observableArrayList(registro);
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
    
    public void seleccionarElemento(){
        if (selecionar) {
            if (tblServicios_has_Platos.getSelectionModel().getSelectedItems() != null) {
                txtServicios_codigoServicio.setText(
                        String.valueOf(((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                cmbPlato.getSelectionModel().select(buscarPlato(
                        ((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbServicio.getSelectionModel().select(buscarServicio(
                        ((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
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
                selecionar = false;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnReporte.setText("Reporte");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                operaciones = TipoDeOperaciones.NINGUNO;
                selecionar = true;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        if (cmbPlato.getSelectionModel().getSelectedItem() == null ||
                cmbServicio.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else{
            try{
                Servicios_has_Platos registro = new Servicios_has_Platos();
                registro.setCodigoPlato(((Plato)cmbPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                registro.setCodigoServicio(((Servicio)cmbServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearServicios_has_Plato(?,?)");
                procedimiento.setInt(1, registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCodigoServicio());
                procedimiento.execute();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reporte(){
        switch(operaciones){
            case GUARDAR: 
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnReporte.setText("Reporte");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                operaciones = TipoDeOperaciones.NINGUNO;
                selecionar = true;
                break;
            case NINGUNO:
                limpiarControles();
                break;
        }
    }
    
    public void limpiarControles(){
        txtServicios_codigoServicio.clear();
        cmbPlato.getSelectionModel().select(null);
        cmbServicio.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        cmbPlato.setDisable(false);
        cmbServicio.setDisable(false);
    }
    
    public void desactivarControles(){
        txtServicios_codigoServicio.setEditable(false);
        cmbPlato.setDisable(true);
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
