package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	String role = this.boxRuolo.getValue();
    	if(role=="") {
    		this.txtResult.setText("Devi inserire un ruolo!");
    		return;
    	}
    	this.txtResult.appendText(this.model.artistiAdiacenti(role));
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	String stringaId = this.txtArtista.getText();
    	int id = 0;
    	try {
    		id = Integer.parseInt(stringaId);
    	}
    	catch(NumberFormatException nbe) {
    		this.txtResult.setText("Inserisci l'identificativo di un artista!");
    		return;
    	}
    	
    	this.txtResult.appendText(this.model.cercaCammino(id));
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String role = this.boxRuolo.getValue();
    	if(role=="") {
    		this.txtResult.setText("Devi inserire un ruolo!");
    		return;
    	}
    	this.txtResult.setText(this.model.creaGrafo(role));
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRole());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
