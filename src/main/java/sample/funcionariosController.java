package sample;

import Services.UsuarioService;
import com.jfoenix.controls.*;
import helpers.AlertDialogModel;
import helpers.LoadingPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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

import java.net.URL;
import java.util.List;
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

    Usuario modelUsuario;
    boolean selectedUsuario = false;
    JFXDialog dialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prepararTableView();
        recuperarDados();
        setupComponents();
    }

    private void recuperarDados() {
        new Service<List<Usuario>>(){
            JFXDialog loading = LoadingPane.SimpleLoading(stackPane);
            @Override
            public void start() {
                loading.show();
                super.start();
            }

            @Override
            protected Task<List<Usuario>> createTask() {
                return new Task<List<Usuario>>() {
                    @Override
                    protected List<Usuario> call() throws Exception {
                        listaUsuarios = FXCollections.observableArrayList(UsuarioService.findAll());
                        return listaUsuarios;
                    }
                };
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                tableUsuario.setItems(listaUsuarios);
                loading.close();
            }

        }.start();

    }

    //metodos iniciais
    private void setupComponents() {
        btnNovo.setOnAction(e-> alertDialogUsuarioNovo());

        btnEditar.setOnAction((e) -> {
            if (selectedUsuario == true && modelUsuario != null) {
                alertDialogClientes();
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Selecione um Usuario", stackPane);
                dialog.show();
            }
        });
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
    }
    //metodos iniciais

    //metodos de negocios

    //metodos de negocios


    //objetos
    private void alertDialogUsuarioNovo(){
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
                "Visitante","Operador","Admin"
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

        btnSalvar.setOnAction((e) ->{
            if(!(edtNome.getText().equals("")) && !(edtSenha.getText().equals(""))){
                    new Service<Usuario>(){
                        JFXDialog loading = LoadingPane.SimpleLoading(stackPane);

                        @Override
                        public void start() {
                            loading.show();
                            super.start();
                        }

                        @Override
                        protected Task<Usuario> createTask() {
                            return new Task<Usuario>() {
                                @Override
                                protected Usuario call() throws Exception {
                                    String username = edtNome.getText().toUpperCase().trim();
                                    String pass = edtSenha.getText().toUpperCase().trim();
                                    String permissions = cbPermissoes.getSelectionModel().getSelectedItem();

                                    Usuario newUsr = UsuarioService.insert(new Usuario(null, username, pass, permissions)) ;
                                    return newUsr;
                                }
                            };
                        }

                        @Override
                        protected void succeeded() {
                            super.succeeded();
                            loading.close();
                            dialog.close();
                            refreshTable();
                        }

                    }.start();
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
    private void alertDialogClientes(){
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
                "Visitante","Operador","Admin"
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
                String permission = cbPermissoes.getSelectionModel().getSelectedItem();
                String senha = edtSenha.getText().trim();
                Usuario newUser = new Usuario(modelUsuario.getId(),nome, senha, permission);
                    new Service<Usuario>(){
                        JFXDialog loading = LoadingPane.SimpleLoading(stackPane);

                        @Override
                        public void start() {
                            super.start();
                            loading.show();
                        }

                        @Override
                        protected Task<Usuario> createTask() {
                            return new Task<Usuario>() {
                                @Override
                                protected Usuario call() throws Exception {
                                    Usuario usr = new Usuario();
                                    if(UsuarioService.update(newUser) == 2){
                                        usr = UsuarioService.getById(newUser.getId());
                                    }
                                    return usr;
                                }
                            };
                        }

                        @Override
                        protected void succeeded() {
                            super.succeeded();
                            refreshTable();
                            loading.close();
                            dialog.close();
                        }

                    }.start();
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
        recuperarDados();
    }
    //metodos de controle


}
