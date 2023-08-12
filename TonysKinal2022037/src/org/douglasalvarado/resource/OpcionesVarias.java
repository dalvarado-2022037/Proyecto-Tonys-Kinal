package org.douglasalvarado.resource;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OpcionesVarias implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgModificar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgReporte;
    
    public void ninguno(){
        btnAgregar.setText("Guardar");
        btnEliminar.setText("Cancelar");
        btnModificar.setDisable(true);
        btnReporte.setDisable(true);
        btnModificar.setVisible(false);
        btnReporte.setVisible(false);
        imgAgregar.setImage(new Image("/org/douglasalvarado/image/guardar.png"));
        imgEliminar.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
    }
    public void guardar(){
        btnAgregar.setText("Agregar");
        btnEliminar.setText("Elimiar");
        btnModificar.setDisable(false);
        btnReporte.setDisable(false);
        btnModificar.setVisible(true);
        btnReporte.setVisible(true);
        imgAgregar.setImage(new Image("/org/douglasalvarado/image/Add.png"));
        imgEliminar.setImage(new Image("/org/douglasalvarado/image/Delete.png"));
    }
    
    public void editar(){
        btnModificar.setText("Actualizar");
        btnReporte.setText("Cancelar");
        btnAgregar.setDisable(true);
        btnEliminar.setDisable(true);
        btnAgregar.setVisible(false);
        btnEliminar.setVisible(false);
        imgModificar.setImage(new Image("/org/douglasalvarado/image/actualizar.png"));
        imgReporte.setImage(new Image("/org/douglasalvarado/image/cancelar.png"));
    }
    public void finalEditar(){
        btnModificar.setText("Modificar");
        btnReporte.setText("Reporte");
        btnAgregar.setDisable(false);
        btnEliminar.setDisable(false);
        btnAgregar.setVisible(true);
        btnEliminar.setVisible(true);
        imgModificar.setImage(new Image("/org/douglasalvarado/image/Update.png"));
        imgReporte.setImage(new Image("/org/douglasalvarado/image/Report.png"));
    }
}
