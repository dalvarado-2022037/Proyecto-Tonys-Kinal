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
import org.douglasalvarado.bean.Plato;
import org.douglasalvarado.bean.TipoPlato;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;

public class PlatoController implements Initializable{
    private enum tipoDeOperaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private tipoDeOperaciones operaciones = tipoDeOperaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
    private Button boton;
    
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePLato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox cmbTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colTipo;
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
        cmbTipoPlato.setItems(getTipoPlato());
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
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("cantidadPlato"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Plato,String>("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato,String>("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato,Double>("precioPlato"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Plato,String>("codigoTipoPlato"));
    }
    
    public void selecionarElemento(){
        if (tblPlatos.getSelectionModel().getSelectedItems() != null) {
            txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidadPlato()));
            txtNombrePLato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
            txtDescripcion.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato()));
            txtPrecio.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        }
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
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try {
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTiploPlatos(?)");
            procedimineto.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimineto.executeQuery();
            while (registro.next()) {
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"), 
                                         registro.getString("descripcionPlato"));
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
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                btnModificar.setVisible(false);
                btnReporte.setVisible(false);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                operaciones = tipoDeOperaciones.GUARDAR;
                boton = btnAgregar;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                cargarDatos();
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        if (txtCantidad.getText().isEmpty() || txtDescripcion.getText().isEmpty() || 
                txtNombrePLato.getText().isEmpty() || txtPrecio.getText().isEmpty() || cmbTipoPlato.getSelectionModel().getSelectedItem()==null)
            JOptionPane.showMessageDialog(null, "No dejar parametros vacios");
        else if(!(txtCantidad.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en cantidad");
        else if(!(txtPrecio.getText().replaceAll(" ", "").matches("[0.00-9.99]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en precio");
        else{
            Plato registro = new Plato();
            registro.setCantidadPlato(Integer.parseInt(txtCantidad.getText()));
            registro.setDescripcionPlato(txtDescripcion.getText());
            registro.setNombrePlato(txtNombrePLato.getText());
            registro.setPrecioPlato(Integer.parseInt(txtPrecio.getText()));
            registro.setCodigoTipoPlato(((TipoPlato)cmbTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()); 
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_CrearPlato(?,?,?,?,?)");
                procedimiento.setInt(1, registro.getCantidadPlato());
                procedimiento.setString(2, registro.getNombrePlato());
                procedimiento.setString(3, registro.getDescripcionPlato());
                procedimiento.setDouble(4, registro.getPrecioPlato());
                procedimiento.setInt(5, registro.getCodigoTipoPlato());
                procedimiento.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void eliminar(){
        switch (operaciones) {
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setVisible(true);
                btnReporte.setVisible(true);
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItems() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == 0) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                            procedimiento.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else
                        limpiarControles();
                }else
                    JOptionPane.showMessageDialog(null, "Selecione un dato a eliminar");
                break;
        }
    }
    
    public void editar(){
        switch (operaciones) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() !=null) {
                    activarControles();
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnAgregar.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnModificar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                    operaciones = tipoDeOperaciones.ACTUALIZAR;
                    boton = btnModificar;
                    cmbTipoPlato.setDisable(true);
                }else
                    JOptionPane.showMessageDialog(null, "Selecione un dato a modificar");
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
                imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
                imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
                cargarDatos();
                operaciones = tipoDeOperaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        if (txtCodigoPlato.getText().isEmpty() || txtCantidad.getText().isEmpty() || txtDescripcion.getText().isEmpty() || 
                txtNombrePLato.getText().isEmpty() || txtPrecio.getText().isEmpty() || cmbTipoPlato.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "No dejar parametros vacios");
        else if(!(txtCantidad.getText().replaceAll(" ", "").matches("[0-9]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en cantidad");
        else if(!(txtPrecio.getText().replaceAll(" ", "").matches("[0.00-9.99]*")))
            JOptionPane.showMessageDialog(null, "Solo numero en precio");
        else{
            Plato registro = new Plato();
            registro.setCodigoPlato(Integer.parseInt(txtCodigoPlato.getText()));
            registro.setCantidadPlato(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePLato.getText());
            registro.setDescripcionPlato(txtDescripcion.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarPlato(?,?,?,?,?)");
                procedimiento.setInt(1, registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCantidadPlato());
                procedimiento.setString(3, registro.getNombrePlato());
                procedimiento.setString(4, registro.getDescripcionPlato());
                procedimiento.setDouble(5, registro.getPrecioPlato());
                procedimiento.execute();
                System.out.println("Cambio");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reporte(){
        switch (operaciones) {
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
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
                JOptionPane.showMessageDialog(null, "Trabajando");
                break;
        }
    }
    
    public void limpiarControles(){
        txtCodigoPlato.clear();
        txtCantidad.clear();
        txtDescripcion.clear();
        txtNombrePLato.clear();
        txtPrecio.clear();
        cmbTipoPlato.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        txtCantidad.setEditable(true);
        txtDescripcion.setEditable(true);
        txtNombrePLato.setEditable(true);
        txtPrecio.setEditable(true);
        cmbTipoPlato.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtDescripcion.setEditable(false);
        txtNombrePLato.setEditable(false);
        txtPrecio.setEditable(false);
        cmbTipoPlato.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ejectar(){   
        txtCantidad.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtDescripcion.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtNombrePLato.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
        txtPrecio.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                boton.fire();
        });
    }
}
