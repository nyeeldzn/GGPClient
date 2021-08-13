package sample;

import com.jfoenix.controls.*;
import helpers.AlertDialogModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.OrdemPedido;
import models.Usuario;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class funcionariosController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btnNovo;

    @FXML
    private JFXButton btnEditar;

    @FXML
    private JFXButton btnExcluir;

    @FXML
    private JFXTextField edtSearch;

    @FXML
    private TableView<Usuario> tableUsuario;

    @FXML
    private TableColumn<OrdemPedido, Integer> idCol;

    @FXML
    private TableColumn<OrdemPedido, String> nomeCol;

    @FXML
    private TableColumn<OrdemPedido, Integer> permCol;

    ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();


    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String query = null;
    Usuario modelUsuario;
    boolean selectedUsuario = false;
    JFXDialog dialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prepararTableView();
        setupComponents();
    }

    //metodos iniciais
    private void setupComponents() {
        btnNovo.setOnAction((e)-> {
                    try {
                        alertDialogUsuarioNovo();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                });
        btnEditar.setOnAction((e) -> {
            if (selectedUsuario == true && modelUsuario != null) {
                try {
                    alertDialogClientes();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Selecione um Usuario", stackPane);
                dialog.show();
            }
        });
    }
    private void recuperarFuncionarios() {
        //recuperar todos os usuarios
        //tableUsuario.setItems(listaUsuarios);
    }
    private void prepararTableView() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        permCol.setCellValueFactory(new PropertyValueFactory<>("priv"));
        tableUsuario.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Usuario>() {
            @Override
            public void changed(ObservableValue<? extends Usuario> observable, Usuario oldValue, Usuario newValue) {
                if(tableUsuario.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tableUsuario.getSelectionModel();
                    modelUsuario = (Usuario) tableUsuario.getSelectionModel().getSelectedItem();
                    selectedUsuario = true;
                    System.out.println(modelUsuario.getUsername());
                }
            }
        });
        recuperarFuncionarios();
    }
    //metodos iniciais

    //metodos de negocios
    private boolean insertUsuario(String query, String value, int id) throws SQLException {
        //inserir usuario
        return false;
    }
    //metodos de negocios


    //objetos
    private void alertDialogUsuarioNovo() throws IOException {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialogLayout.setBody(
                formularioUsuarioNovo()
        );
        dialog.show();
    }
    public AnchorPane formularioUsuarioNovo(){
        AnchorPane pane = new AnchorPane();
        VBox vboxPrincipal = defaultVBox();
        JFXButton btnSalvar = defaultButton("SALVAR USUARIO");
        JFXButton btnCancelar = defaultButton("CANCELAR");
        JFXTextField edtNome = textFieldPadrao(400);
        edtNome.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        JFXComboBox<String> cbPermissoes = new JFXComboBox();
        ObservableList<String> listaPermissoes = FXCollections.observableArrayList(
                "Visitante","Entregador","Operador","Admin"
        );
        cbPermissoes.setItems(listaPermissoes);
        cbPermissoes.getSelectionModel().select(0);
        JFXPasswordField edtSenha = passwordfieldPadrao(550);

        HBox row1 = defaultHBox();
        HBox row2 = defaultHBox();
        HBox row3 = defaultHBox();

        VBox R1C1 = defaultVBox();
        VBox R1C2 = defaultVBox();
        VBox R2C1 = defaultVBox();

        R1C1.getChildren().addAll(
                defaultText("NOME"),
                edtNome
        );
        R1C2.getChildren().addAll(
                defaultText("PERMISSÕES"),
                cbPermissoes
        );
        R2C1.getChildren().addAll(
                defaultText("SENHA"),
                edtSenha
        );

        row1.getChildren().addAll(
                R1C1,
                R1C2);

        row2.getChildren().addAll(
                R2C1
        );

        row3.getChildren().addAll(
                btnSalvar,
                btnCancelar
        );

        row3.setAlignment(Pos.CENTER_RIGHT);

        vboxPrincipal.getChildren().addAll(row1, row2, row3);

        btnSalvar.setOnAction((e) -> {
            if(!(edtNome.getText().equals("")) && !(edtSenha.getText().equals(""))){
                String nome = edtNome.getText().trim();
                int permissions = cbPermissoes.getSelectionModel().getSelectedIndex();
                String senha = edtSenha.getText().trim();
                Usuario newUser = new Usuario();
                //inserir usuario
                //caso ok retornar refresh table e dialog.close();
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Preencha todos os campos", stackPane);
                dialog.show();
            }
        });
        btnCancelar.setOnAction((event -> {
            dialog.close();
        }));
        pane.getChildren().add(vboxPrincipal);
        return pane;
    }
    private void alertDialogClientes() throws IOException {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialogLayout.setBody(
                formularioUsuarioEditar()
        );
        dialog.show();
    }
    public AnchorPane formularioUsuarioEditar(){
        AnchorPane pane = new AnchorPane();
        VBox vboxPrincipal = defaultVBox();
        JFXButton btnSalvar = defaultButton("SALVAR ALTERAÇÕES");
        JFXButton btnCancelar = defaultButton("CANCELAR");
        JFXTextField edtNome = textFieldPadrao(400);

        JFXComboBox<String> cbPermissoes = new JFXComboBox();
        ObservableList<String> listaPermissoes = FXCollections.observableArrayList(
                "Visitante","Entregador","Operador","Admin"
        );
        cbPermissoes.setItems(listaPermissoes);
        cbPermissoes.getSelectionModel().select(modelUsuario.getPriv());
        JFXPasswordField edtSenha = passwordfieldPadrao(550);

        HBox row1 = defaultHBox();
        HBox row2 = defaultHBox();
        HBox row3 = defaultHBox();

        VBox R1C1 = defaultVBox();
        VBox R1C2 = defaultVBox();
        VBox R2C1 = defaultVBox();

        R1C1.getChildren().addAll(
                defaultText("NOME"),
                edtNome
        );
        R1C2.getChildren().addAll(
                defaultText("PERMISSÕES"),
                cbPermissoes
        );
        R2C1.getChildren().addAll(
                defaultText("SENHA"),
                edtSenha
        );

        row1.getChildren().addAll(
                R1C1,
                R1C2);

        row2.getChildren().addAll(
                R2C1
        );

        row3.getChildren().addAll(
                btnSalvar,
                btnCancelar
        );

        row3.setAlignment(Pos.CENTER_RIGHT);

        vboxPrincipal.getChildren().addAll(row1, row2, row3);

        edtNome.setText(modelUsuario.getUsername());
        btnSalvar.setOnAction((e) -> {
            if(!(edtNome.getText().equals("")) && !(edtSenha.getText().equals(""))){
                String nome = edtNome.getText().trim();
                int permissions = cbPermissoes.getSelectionModel().getSelectedIndex();
                String senha = edtSenha.getText().trim();
                //Usuario newUser = new Usuario(modelUsuario.getId(),nome, StringUtil.Cripto(senha), permissions);
                //atualizar usuario caso ok retornar refreshtable e dialog.close();
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Preencha todos os campos", stackPane);
                dialog.show();
            }
        });
        btnCancelar.setOnAction((event -> {
            dialog.close();
        }));
        pane.getChildren().add(vboxPrincipal);
        return pane;
    }
    public JFXButton defaultButton(String texto){
        JFXButton button = new JFXButton();
        button.setStyle("-fx-background-color: lightgrey");
        button.setText(texto);
        return button;
    }
    public JFXPasswordField passwordfieldPadrao(double size){
        JFXPasswordField textField = new JFXPasswordField();
        textField.setStyle("-fx-background-color: lightgrey");
        textField.setPrefWidth(size);
        return textField;
    }
    public JFXTextField textFieldPadrao(double size){
        JFXTextField textField = new JFXTextField();
        textField.setStyle("-fx-background-color: lightgrey");
        textField.setPrefWidth(size);
        return textField;
    }
    public Text defaultText(String texto){
        Text text = new Text();
        text.setText(texto);
        text.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        return text;
    }
    public VBox defaultVBox(){
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
    public HBox defaultHBox(){
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }
    //objetos


    //metodos de controle
    private void refreshTable() {
        listaUsuarios.clear();
        recuperarFuncionarios();
    }
    //metodos de controle


}
