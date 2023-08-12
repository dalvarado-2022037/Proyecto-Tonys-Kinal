package org.douglasalvarado.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.douglasalvarado.main.Principal;



public class ProgramadorController implements Initializable{
    private Principal esenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }

    public Principal getEsenarioPrincipal() {
        return esenarioPrincipal;
    }

    public void setEsenarioPrincipal(Principal esenarioPrincipal) {
        this.esenarioPrincipal = esenarioPrincipal;
    }
    
    public void menuPrincipal(){
        esenarioPrincipal.menuPrincipal();
    }
}
