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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import org.douglasalvarado.bean.Login;
import org.douglasalvarado.bean.Usuario;
import org.douglasalvarado.db.Conexion;
import org.douglasalvarado.main.Principal;


public class LoginController implements Initializable {
    private Principal escenarioPricipanl;
    private ObservableList<Usuario> listaUsuario;
    
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private Button btnLogin;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public ObservableList<Usuario> getUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try{
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios()}");
            ResultSet resultado = procedimineto.executeQuery();
            while (resultado.next()) {
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                                      resultado.getString("nombreUsuario"),
                                      resultado.getString("apellidoUsuario"),
                                      resultado.getString("usuarioLogin"),
                                      resultado.getString("contrasena")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableArrayList(lista);
    }
    
    @FXML
    private void sesion(){
        Login login = new Login();
        int x = 0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUsuario.getText());
        login.setPasswordLogin(txtPassword.getText());
        while (x < getUsuario().size()) {
            String user = getUsuario().get(x).getUsuarioLogin();
            String pass = getUsuario().get(x).getContrasena();
            if (user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())) {
                JOptionPane.showMessageDialog(null, "Secion iniciada\n"+
                        getUsuario().get(x).getNombreUsuario()+" "+
                        getUsuario().get(x).getApellidoUsuario()+"\n"+
                        "Bienvenido(a)");
                escenarioPricipanl.menuPrincipal();
                x = getUsuario().size();
                bandera = true;
                //break;
            }
            x++;
        }
        if (bandera==false) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorectas");
        }
    }

    public Principal getEscenarioPricipanl() {
        return escenarioPricipanl;
    }

    public void setEscenarioPricipanl(Principal escenarioPricipanl) {
        this.escenarioPricipanl = escenarioPricipanl;
    }
    
    public void VentanaUsuario(){
        escenarioPricipanl.ventanaUsuario();
    }
    
    public void ejectar(){   
        txtUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnLogin.fire();
        });
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnLogin.fire();
        });
    }
}
