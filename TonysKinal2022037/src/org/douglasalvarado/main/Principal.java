/*
    Douglas Humberto Alvarado Morales
    2022037
    IN5AV
    Fecha de cración:
        28-03-2023
    Fecha de modificaciones:
        28-03-2023
        11-04-2023
        12-04-2023
        17-04-2023
*/
package org.douglasalvarado.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.douglasalvarado.controller.EmpleadoController;
import org.douglasalvarado.controller.EmpresaController;
import org.douglasalvarado.controller.LoginController;
import org.douglasalvarado.controller.MenuPrincipalController;
import org.douglasalvarado.controller.PlatoController;
import org.douglasalvarado.controller.PresupuestoController;
import org.douglasalvarado.controller.ProductoController;
import org.douglasalvarado.controller.Producto_has_PlatoController;
import org.douglasalvarado.controller.ProgramadorController;
import org.douglasalvarado.controller.ServicioController;
import org.douglasalvarado.controller.Servicios_has_EmpleadosController;
import org.douglasalvarado.controller.Servicios_has_PlatosController;
import org.douglasalvarado.controller.TipoEmpleadoController;
import org.douglasalvarado.controller.TipoPlatoController;
import org.douglasalvarado.controller.UsuarioController;

/**
 *
 * @author informatica
 */
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/douglasalvarado/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/douglasalvarado/image/LogoSimplificado.png"));
        ventanaLogin();
        escenarioPrincipal.show();
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 553, 627);
            menu.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try {
            ProgramadorController progra = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 520, 287);
            progra.setEsenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try {
            EmpresaController empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml", 564, 433);
            empresa.setEscenariPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try {
            TipoEmpleadoController tipoE = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml", 564, 418);
            tipoE.setEscenarioPrinciapal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try {
            TipoPlatoController tipoP = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml", 564, 404);
            tipoP.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try {
            ProductoController producto = (ProductoController)cambiarEscena("ProductosView.fxml", 564, 390);
            producto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try {
            ServicioController servicio = (ServicioController)cambiarEscena("ServicioView.fxml", 740, 390);
            servicio.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml", 638, 390);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    public void ventanaEmpleado(){
        try {
            EmpleadoController empleado = (EmpleadoController)cambiarEscena("EmpleadoView.fxml", 1001, 474);
            empleado.setEscenarioPrinciplar(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatoController plato = (PlatoController)cambiarEscena("PlatoView.fxml", 659, 390);
            plato.setEscenarioPrincipal(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try {
            LoginController loginController = (LoginController)cambiarEscena("LoginView.fxml", 600, 447);
            loginController.setEscenarioPricipanl(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuarioController = (UsuarioController)cambiarEscena("UsuarioView.fxml", 564, 390);
            usuarioController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto_has_PlatoController(){
        try{
            Producto_has_PlatoController php = (Producto_has_PlatoController)cambiarEscena("Productos_has_PlatosView.fxml", 564, 390);
            php.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicios_has_Platos(){
        try{
            Servicios_has_PlatosController shp = (Servicios_has_PlatosController)cambiarEscena("Servicios_has_PlatosView.fxml", 564, 390);
            shp.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicios_has_Empleados(){
        try{
            Servicios_has_EmpleadosController she = (Servicios_has_EmpleadosController)cambiarEscena("Servicios_has_EmpleadosView.fxml", 654, 390);
            she.setEscenarioPrincipal(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML  = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml); 
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
}
