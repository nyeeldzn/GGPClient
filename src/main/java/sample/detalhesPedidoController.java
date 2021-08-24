package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import helpers.AlertDialogModel;
import helpers.DefaultComponents;
import helpers.UserPrivilegiesVerify;
import helpers.intentData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.OrdemPedido;
import models.Produto;
import models.ProdutoPedido;
import models.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class  detalhesPedidoController implements Initializable {

    @FXML
    private JFXButton btnSair;

    @FXML
    private JFXButton btnImprimir;

    @FXML
    private JFXButton btnAlterarPolimorf;

    @FXML
    private JFXButton btnAtualizarLista;

    @FXML
    private TableView<ProdutoPedido> tableProdutos;

    @FXML
    private TableColumn<ProdutoPedido, String> nomeCol;

    @FXML
    private TableColumn<ProdutoPedido, Integer> qtdCol;

    @FXML
    private TextField edtNomeProduto;

    @FXML
    private TextField edtQTD;

    @FXML
    private Text textNome;

    @FXML
    private Text textEndereco;

    @FXML
    private Text textTelefone;

    @FXML
    private Text textData_Entrada;

    @FXML
    private Text textPagamento;

    @FXML
    private Text textEnvio;

    @FXML
    private JFXButton btnADD;

    private  JFXDialog dialog;

    private BorderPane border;

    private JFXTextField edtNome;

    @FXML
    private StackPane stackPane;

    String pedido_id;
    int table_index;
    String prod_id, prod_nome;
    Produto produto;
    OrdemPedido pedido;
    int selectedProduto;
    boolean isSelected = false;
    String horario_atual;
    double bHeight;
    Usuario user;

    ArrayList<Produto> produtos = new ArrayList();
    ObservableList<ProdutoPedido> produtosPedido = FXCollections.observableArrayList();

    String query = null;
    String pedido_Produto_query = null;
    Connection connection = null;
    PreparedStatement preparedStatementProduto = null;
    PreparedStatement preparedStatementOrdem = null;

    PreparedStatement preparedStatementPedidoProduto = null;

    ResultSet resultSet = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recuperarUsuario();
        try {
            recuperarIntentProdUid();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            recuperarProdutosPedido();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        setupComponentes();

    }

    private void recuperarUsuario() {
        //recuperar usuario
    }

    private void setupEdt() {
        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i<produtos.size(); i++){
            nomes.add(produtos.get(i).getNome());
        }

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setPrefWidth(500);
        autoCompletePopup.getSuggestions().addAll(nomes);

        autoCompletePopup.setSelectionHandler(event -> {
            edtNomeProduto.setText(event.getObject());
            edtQTD.requestFocus();
            // you can do other actions here when text completed
        });

        // filtering options
        edtNomeProduto.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(edtNomeProduto.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || edtNomeProduto.getText().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(edtNomeProduto);
            }
        });
    }

    //Metodos Iniciais
    private void setupComponentes() {
        setupEdt();
        edtQTD.setText("1");
        edtNomeProduto.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        edtQTD.setOnKeyPressed((e) ->{
            switch (e.getCode()){
                case ENTER:
                    btnADD.requestFocus();
                    break;
            }
        });
        btnADD.setOnKeyPressed((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 2) == true){
                switch (e.getCode()){
                    case ENTER:
                        salvarProduto();
                        break;
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                dialog.show();
            }

        });
        switch (table_index){
            case 1:
                btnAlterarPolimorf.setVisible(true);
                break;
            case 2:
                btnAlterarPolimorf.setVisible(true);
                break;
            case 3:
                btnAlterarPolimorf.setVisible(false);
                break;

        }
        btnAlterarPolimorf.setOnAction((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 1) == true){
                if(table_index != 3){
                    alterarStatusPedido();
                }else{
                    JFXDialog dialog = AlertDialogModel.alertDialogErro("Não é permitadas, alterações no pedido finalizado", stackPane);
                    dialog.show();
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                dialog.show();
            }
        });
        btnADD.setOnAction((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 2) == true){
                if(table_index != 3){
                    salvarProduto();
                }else{
                    JFXDialog dialog = AlertDialogModel.alertDialogErro("Não é permitidas alterações, no pedido finalizado", stackPane);
                    dialog.show();
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                dialog.show();
            }
        });
        btnSair.setOnAction((e) -> {
            fecharJanela();
        });
        btnAtualizarLista.setOnAction((e) -> {
            try {
                recuperarProdutosPedido();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
        btnImprimir.setOnAction((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 1) == true){
                File file = DefaultComponents.fileChooserSave(stackPane, "PDF files (*.pdf)", "*.pdf");
                if(file != null){
                    criarPDF(file);
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                dialog.show();
            }
        });
        tableProdutos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProdutoPedido>() {
            @Override
            public void changed(ObservableValue<? extends ProdutoPedido> observable, ProdutoPedido oldValue, ProdutoPedido newValue) {
                if(tableProdutos.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tableProdutos.getSelectionModel();
                    ProdutoPedido produto = tableProdutos.getSelectionModel().getSelectedItem();
                    selectedProduto = produto.getIndex();
                    isSelected = true;
                }
            }
        });
        tableProdutos.setOnKeyPressed((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 2) == true){
                switch (e.getCode()) {
                    case DELETE:
                        if(isSelected == true){
                            if(table_index != 3){
                                removerProduto();
                            }else{
                                JFXDialog dialog = AlertDialogModel.alertDialogErro("Não é permitida alterações no pedido finalizado", stackPane);
                                dialog.show();
                            }
                        }else{

                        }
                        break;
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                dialog.show();
            }
        });

    }

    private void removerProduto() {
        //excluir produto
    }

    private void criarPDF(File file) {
        Document document = new Document();
        int size = produtosPedido.size();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
            document.open();

            // adicionando um parágrafo no documento
            document.add(new Paragraph("=============================="));
            document.add(new Paragraph("                              "));
            document.add(new Paragraph("        Gerenciador de Pedidos    "));
            document.add(new Paragraph("              SunOnRails          "));
            document.add(new Paragraph("                              "));
            document.add(new Paragraph("=========Dados do Cliente========"));
            document.add(new Paragraph("-Nome:        " + pedido.getCliente().getNome()));
            document.add(new Paragraph("-Endereço:    " + pedido.getCliente().getEndereco()));
            document.add(new Paragraph("-Telefone:    " + pedido.getCliente().getTelefone()));
            document.add(new Paragraph("-F. de Pag.:  " + pedido.getForma_pagamento()));
            document.add(new Paragraph("-D. Entrada:    " + pedido.getEntradaDate()));
            document.add(new Paragraph("-H. Triagem:    " + pedido.getTriagemHora()));
            document.add(new Paragraph("=============================="));
            document.add(new Paragraph("  Qtd.                   Item no.   "));
            document.add(new Paragraph("=============================="));
            for(int i = 0; i < size; i++){
            document.add(new Paragraph(" " + produtosPedido.get(i).getQtd() +" || " + produtosPedido.get(i).getNome()));
            }
            document.add(new Paragraph("=============================="));
            document.add(new Paragraph("-Operador: " + pedido.getOperador().getUsername()));
            document.add(new Paragraph("-Entregador: " + pedido.getEntregador()));
            document.add(new Paragraph("-F. Pedido: " + pedido.getFonte_pedido()));
            document.add(new Paragraph("=============================="));

        }catch (DocumentException | FileNotFoundException de) {
            de.printStackTrace();
        }
        document.close();
    }

    private void recuperarIntentProdUid() throws SQLException {
        String id;
        int table;
        intentData intent = intentData.getINSTANCE();
        id = intent.getDadosteste();
        table = intent.getTableIndex();
        pedido_id = id;
        table_index = table;
        System.out.println("Dado recebido: " + pedido_id);

        recuperarProdutos();
        recuperarDadosPedido();
    }
    private void recuperarProdutos() throws SQLException {
        produtos.clear();
        //recuperar todos os pedidos
    }
    private void recuperarProdutosPedido() throws SQLException {
        produtosPedido.clear();
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        qtdCol.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        //recuperar produtos do pedido
        tableProdutos.setItems(produtosPedido);
    }
    private void recuperarProdutoCriado(String nome) throws SQLException {
        //recuperar pedido criado por nome
        addproduto_pedido(Math.toIntExact(produto.getId()), produto.getNome());
    }
    private void recuperarDadosPedido() throws SQLException {
        //recuperar dados do pedido
        setarDados();
    }
    //Metodos Iniciais

    //Metodos de Negocios
    private void addproduto_pedido(int id, String nome) {
        int quantidade = Integer.parseInt(edtQTD.getText());
        //adicionar produto
    }
    private void salvarProduto(){
        prod_nome = edtNomeProduto.getText().toUpperCase().trim();

        if(prod_nome.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os Dados!");
            alert.showAndWait();
        }else{
            for(int i = 1; i<produtos.size(); i++){
                int id = Math.toIntExact(produtos.get(i).getId());
                String nomerecup = produtos.get(i).getNome().toUpperCase().trim();

                System.out.println(nomerecup);
                System.out.println(prod_nome);
                System.out.println(i);

                System.out.println(produtos.size());

                if(nomerecup.equals(prod_nome)){
                    System.out.println("Finalizando for, adcionando produto.");
                    i = produtos.size();
                    System.out.println("ArraySize " + produtos.size() + "For size " + i);
                    addproduto_pedido(id, nomerecup.toUpperCase().trim());
                }else if(!(nomerecup.equals(prod_nome))){
                    if(i == produtos.size() - 1 && !(prod_nome.equals(nomerecup))){
                        System.out.println("Criando novo produto, finalizando for");
                        i = i + 1;
                        insertProduto();
                    }
                }
            }

        }
    }
    private void insertProduto() {
        //inserir produto
        if(true){
            try {
                recuperarProdutoCriado(prod_nome);
                restartAdd();
            }catch (SQLException ex){
                ex.printStackTrace();
            }

        }

    }
    private void alertDialogProdutos(int id, String query) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton buttonCancelar = new JFXButton("Cancelar");
        JFXButton buttonConfirmar = new JFXButton("Salvar");
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        buttonCancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) ->{
            dialog.close();
        });
        buttonConfirmar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String nome_caixa = edtNome.getText().toUpperCase().trim();
            if(!(nome_caixa.equals(""))){
                //boolean state = db_crud.metodoAtualizarPedido(id, nome_caixa,query);
                if(true){
                    fecharJanela();
                }
            }else{
                //alertDialogAlert();
            }
        });
        dialogLayout.setPrefSize(250, 150);
        dialogLayout.setBody(
                borderPaneNovoProduto()
        );
        dialogLayout.setActions(buttonConfirmar,buttonCancelar);
        dialog.show();
    }

    public BorderPane borderPaneNovoProduto(){
        border = new BorderPane();
        edtNome = new JFXTextField();
        edtNome.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        edtNome.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-border-color: black");
        Text text = new Text("Favor informar o nome do Caixa Responsavel");
        text.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 15));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(text ,edtNome);
        border.setCenter(vbox);
        return border;
    }

    private void alterarStatusPedido() {
        int count;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        horario_atual = dateFormat.format(date);

        //alterar status do pedido
            }
    //Metodos de Negocios

    //Metodos de Controle
    private void restartAdd() throws SQLException {
        System.out.println("Reiniciando Componentes");
        edtNomeProduto.clear();
        edtQTD.clear();
        edtQTD.setText("1");
        edtNomeProduto.requestFocus();
        recuperarProdutos();
        recuperarProdutosPedido();
    }
    private void setarDados() {
        //setar dados nos textfields
    }
    private void fecharJanela(){
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    //Metodos de Controle
}
