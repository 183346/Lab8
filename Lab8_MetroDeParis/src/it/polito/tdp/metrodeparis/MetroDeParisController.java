package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.bean.Fermata;
import it.polito.tdp.db.MetroDAO;
import it.polito.tdp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MetroDeParisController {
	
	Model model = new Model();
	
	

    public void setModel(Model model) {
		 	this.model = model;
		 	MetroDAO dao = new MetroDAO();
		 	this.comboPartenza.getItems().addAll(dao.getAllFermata());
		 	this.comboArrivo.getItems().addAll(dao.getAllFermata());
	}

	@FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="comboPartenza"
    private ComboBox<Fermata> comboPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistanza"
    private Button btnDistanza; // Value injected by FXMLLoader

    @FXML // fx:id="comboArrivo"
    private ComboBox<Fermata> comboArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML
    void doDistanza(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.comboPartenza.getValue()==null){this.txtResult.appendText("selezionare una stazione di partenza");return;}
    	if(this.comboArrivo.getValue()==null){this.txtResult.appendText("selezionare una stazione di partenza");return;}
    	Fermata partenza = this.comboPartenza.getValue();
    	Fermata arrivo = this.comboArrivo.getValue();
    	String result = model.calcolaDistanza(partenza,arrivo);
    	if(result.equals("")){this.txtResult.appendText("Nessun risultato");}
    	this.txtResult.appendText(result);
     }

    @FXML
    void doPercorso(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert comboPartenza != null : "fx:id=\"comboPartenza\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnDistanza != null : "fx:id=\"btnDistanza\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert comboArrivo != null : "fx:id=\"comboArrivo\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'MetroDeParis.fxml'.";

    }
}

