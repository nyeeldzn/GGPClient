package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.AlertDialogModel;
import helpers.HTTPRequest.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Usuario;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginLogoutController implements Initializable {


    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXTextField edtUsername;

    @FXML
    private JFXPasswordField edtPassword;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupStackPane();
        edtUsername.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        edtUsername.requestFocus();
        edtUsername.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtPassword.requestFocus();
                    break;
            }
        });
        edtPassword.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    btnLogin.requestFocus();
                    break;
            }
        });

        btnLogin.setOnAction((e) -> {
            authMethod();
        });
        btnLogin.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    authMethod();
                    break;
            }
        });

        btnClose.setOnAction((e) -> {
            Stage stage = (Stage) stackPane.getScene().getWindow();
            // do what you have to do
            stage.close();
        });

        stackPane.setOnMousePressed(pressEvent -> {
            stackPane.setOnMouseDragged(dragEvent -> {
                System.out.println("Movendo a Janela");
                Stage stage = (Stage) stackPane.getScene().getWindow();
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });


    }

    private void setupStackPane() {
        // new Image(url)
        Image image = new Image(getClass().getResource("/loginbg.jpeg").toExternalForm());
// new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
// new BackgroundImage(image, repeatX, repeatY, position, size)
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
// new Background(images...)
        Background background = new Background(backgroundImage);
        borderPane.setBackground(background);
    }


    private void authMethod() {
        //retirar exclamacoes quando implementar o metodo de login
        if (edtUsername.getText().isEmpty()) {
        JFXDialog dialog = AlertDialogModel.alertDialogErro("Preencha o campo de usuario", stackPane);
        dialog.show();
        }else{
            if(edtPassword.getText().isEmpty()){
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Preencha o campo de senha", stackPane);
                dialog.show();
            }else{
                String username = edtUsername.getText().toUpperCase().trim();
                String pass = edtPassword.getText().trim();
                Usuario user = Login.login(new Usuario(null,username,pass,""));
                if(user != null ){
                    System.out.println("Bem vindo " + user.getUsername());
                }else{
                    System.out.println("Houve um problema");
                }
                //iniciarHome();
            }
        }
    }

    private void iniciarHome() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Scene cenaAtual = btnLogin.getScene();
        Stage stageAtual = (Stage) cenaAtual.getWindow();


        cenaAtual = new Scene(parent);

        //stageAtual.setTitle("Gerenciador de Pedidos - Supermercados Gramari Eireli");
        stageAtual.setScene(cenaAtual);

    }
}
