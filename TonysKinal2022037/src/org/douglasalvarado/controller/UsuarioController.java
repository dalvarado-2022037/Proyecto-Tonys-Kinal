package org.douglasalvarado.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.douglasalvarado.bean.Usuario;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class UsuarioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum Operaciones {NINGUNO, GUARDAR};
    private Operaciones TipoDeOperaciones = Operaciones.NINGUNO;
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void nuevo(){
        switch (TipoDeOperaciones) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelarUsuario.png"));
                TipoDeOperaciones = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/addUser.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                TipoDeOperaciones = Operaciones.NINGUNO;
                ventanaLogin();
                break;
        }
    }
    
    public void cancelar(){
        switch (TipoDeOperaciones){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                imgAgregar.setImage(new Image("/org/douglasalvarado/image/addUser.png"));
                imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
                TipoDeOperaciones = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                limpiarControles();
                break;
        }
    }
    
    public void guardar(){
        if (txtNombreUsuario.getText().isEmpty() || txtApellidoUsuario.getText().isEmpty() ||
                txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "No dejar datos vacios");
        else{
            Usuario registro = new Usuario();
            registro.setNombreUsuario(txtNombreUsuario.getText());
            registro.setApellidoUsuario(txtApellidoUsuario.getText());
            registro.setUsuarioLogin(txtUsuario.getText());
            registro.setContrasena(txtPassword.getText());
            try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_CrearUsuario(?,?,?,?)}");
               procedimiento.setString(1, registro.getNombreUsuario());
               procedimiento.setString(2, registro.getApellidoUsuario());
               procedimiento.setString(3, registro.getUsuarioLogin());
               procedimiento.setString(4, registro.getContrasena());
               procedimiento.execute();
               String contra = txtPassword.getText();
               String ecnript = DigestUtils.md5Hex(contra);
               System.out.println(ecnript);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
        
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ejectar(){
        txtNombreUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnAgregar.fire();
        });
        txtApellidoUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnAgregar.fire();
        });
        txtUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnAgregar.fire();
        });
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnAgregar.fire();
        });
    }
}
