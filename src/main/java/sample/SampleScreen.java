package sample;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import models.Bairro;
import models.Cliente;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SampleScreen implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    private JFXTextField textField = new JFXTextField();
    private ObservableList<Cliente> array = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        array.add(new Cliente(null,"Daniel","",new Bairro(null, "Teste"),"",""));
        array.add(new Cliente(null,"Floriza","",new Bairro(null, "Teste"),"",""));
        array.add(new Cliente(null,"Fernando","",new Bairro(null, "Teste"),"",""));
        array.add(new Cliente(null,"Rafael","",new Bairro(null, "Teste"),"",""));
        array.add(new Cliente(null,"Joao","",new Bairro(null, "Teste"),"", ""));

        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i<array.size(); i++){
            nomes.add(array.get(i).getNome());
        }

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(nomes);

        autoCompletePopup.setSelectionHandler(event -> {
            textField.setText(event.getObject());

            // you can do other actions here when text completed
        });

        // filtering options
        textField.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(textField.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || textField.getText().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(textField);
            }
        });


        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);
        borderPane.setCenter(textField);
        AnchorPane.getChildren().add(stackPane);
    }
}
