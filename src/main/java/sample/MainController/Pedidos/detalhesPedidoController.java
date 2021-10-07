package sample.MainController.Pedidos;

import Services.OrderProductService;
import Services.PedidoService;
import Services.ProdutoService;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import helpers.AlertDialogModel;
import helpers.DefaultComponents;
import helpers.UserPrivilegiesVerify;
import helpers.intentData;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
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
import models.OrderProduct;
import models.Produto;
import models.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class  detalhesPedidoController implements Initializable {

    @FXML
    private JFXButton btnSair;

    @FXML
    private JFXButton btnSalvar;

    @FXML
    private JFXButton btnImprimir;

    @FXML
    private JFXButton btnAlterarPolimorf;

    @FXML
    private JFXButton btnAtualizarLista;

    @FXML
    private TableView<OrderProduct> tableProdutos;

    @FXML
    private TableColumn<OrderProduct, String> nomeCol;

    @FXML
    private TableColumn<OrderProduct, Integer> qtdCol;

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

    Long pedido_id;
    int table_index;
    String prod_id, prod_nome;
    Produto produto;
    OrdemPedido pedido = null;
    Long selectedProduto;
    boolean isSelected = false;
    String horario_atual;
    double bHeight;
    Usuario user;
    Produto produtoAtual = null;

    ArrayList<Produto> produtos = new ArrayList();
    ObservableList<OrderProduct> produtosPedido = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recuperarUsuario();
        try {
            recuperarIntentProdUid();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }


        recuperarProdutosPedido();


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

            edtNomeProduto.setOnMouseClicked(e -> {
                if(table_index == 3){
                    JFXDialog dialog = AlertDialogModel.alertDialogErro("Nao e permitido alterar um pedido finalizado!", stackPane);
                    dialog.show();
                }
            });
            edtNomeProduto.setOnKeyPressed(e -> {
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Nao e permitido alterar um pedido finalizado!", stackPane);
                if(table_index == 3){
                    dialog.show();
                }
            });

            JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
            autoCompletePopup.setPrefWidth(500);
            autoCompletePopup.getSuggestions().addAll(nomes);

            autoCompletePopup.setSelectionHandler(event -> {
                edtNomeProduto.setText(event.getObject());
                System.out.println("Buscando Produto selecionado: " + event.getObject().toUpperCase());
                produtoAtual = ProdutoService.getByNome(event.getObject().toUpperCase()).get(0);
                System.out.println("Produto selecionado[recuperado]: " + produtoAtual.getNome());
                if(table_index != 3){
                    edtQTD.requestFocus();
                }else{
                    JFXDialog dialog = AlertDialogModel.alertDialogErro("Nao e permitido alterar um pedido finalizado!", stackPane);
                    dialog.show();
                }
                // you can do other actions here when text completed
            });

            edtNomeProduto.setOnKeyPressed((e) -> {
                switch (e.getCode()){
                    case ENTER:
                        produtoAtual = verificarProdutoExistente(edtNomeProduto.getText());
                        btnADD.requestFocus();
                        break;
                }
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
                    if(table_index != 3){
                        btnADD.requestFocus();
                    }
                    break;
            }
        });
        btnADD.setOnKeyPressed((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 2) == true){
                switch (e.getCode()){
                    case ENTER:
                        adicionarProdutoLista();
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
                btnSalvar.setVisible(true);
                btnADD.setVisible(true);
                break;
            case 2:
                btnAlterarPolimorf.setVisible(true);
                btnSalvar.setVisible(true);
                break;
            case 3:
                btnAlterarPolimorf.setVisible(false);
                btnSalvar.setVisible(false);
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
                    adicionarProdutoLista();
                }else{
                    JFXDialog dialog = AlertDialogModel.alertDialogErro("Não é permitidas alterações, no pedido finalizado", stackPane);
                    dialog.show();
                }
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                dialog.show();
            }
        });
        btnSalvar.setOnAction((e) -> salvarPedido());

        btnSair.setOnAction((e) -> {
            fecharJanela();
        });
        btnAtualizarLista.setOnAction((e) -> recuperarProdutosPedido());
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
        tableProdutos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderProduct>() {
            @Override
            public void changed(ObservableValue<? extends OrderProduct> observable, OrderProduct oldValue, OrderProduct newValue) {
                if(tableProdutos.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tableProdutos.getSelectionModel();
                    OrderProduct produto = tableProdutos.getSelectionModel().getSelectedItem();
                    selectedProduto = produto.getId();
                    isSelected = true;
                }
            }
        });

        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("REMOVER PRODUTO");
        cm.getItems().add(mi1);
        tableProdutos.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    cm.show(tableProdutos, t.getScreenX(), t.getScreenY());
                    mi1.setOnAction((e) -> {
                        OrderProduct selProd = tableProdutos.getSelectionModel().getSelectedItem();
                        removerProduto(selProd);
                        //System.out.println("ID do pedido selecionado: " + tableProdutos.getSelectionModel().getSelectedItem().getId());
                    });
                }
            }
        });

        tableProdutos.setOnKeyPressed((e) -> {
            if(UserPrivilegiesVerify.permissaoVerBotao(user, 2) == true){
                switch (e.getCode()) {
                    case DELETE:
                        if(isSelected == true){
                            if(table_index != 3){
                                OrderProduct selProd = tableProdutos.getSelectionModel().getSelectedItem();
                                removerProduto(selProd);
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

    private Boolean salvarPedido() {
        boolean state = false;
        List<OrderProduct> newProdutosList = new ArrayList<>();
        for(int i = 0; i<produtosPedido.size(); i++){
            System.out.println("Adicionando produto: do pedido: " +
                    // produtosPedido.get(i).getPedido().getId() +
                    " com id: " + produtosPedido.get(i).getId() +
                    " Produto de id: " + produtosPedido.get(i).getProduto().getId() +
                    " Produto de nome: " + produtosPedido.get(i).getProduto().getNome());
            newProdutosList.add(produtosPedido.get(i));
        }
        pedido.setProdutos(newProdutosList);
        System.out.println("Pedido Enviado para atualizacao: \n" + pedido);
        switch(PedidoService.update(pedido)){
            case 0:
                JFXDialog d = AlertDialogModel.alertDialogErro("Houve um problema ao salvar o pedido", stackPane);
                d.show();
                state = false;
                break;
            case 1:
                JFXDialog a = AlertDialogModel.alertDialogErro("Houve um problema ao salvar o pedido", stackPane);
                a.show();
                state = false;
                break;
            case 2:
                fecharJanela();
                state = true;
                break;
        }
        return state;
    }

    private void removerProduto(OrderProduct selProd) {
        //excluir produto
        System.out.println(produtosPedido);
        produtosPedido.remove(selProd);
        System.out.println(produtosPedido);

        switch (OrderProductService.deleteObjectByObject(selProd)){
            case 0:
                AlertDialogModel.alertDialogErro("Houve um problema ao remover o item", stackPane);
                break;
            case 1:
                AlertDialogModel.alertDialogErro("Houve um problema ao remover o item", stackPane);
                break;
            case 2:
                restartAdd();
                break;
        }
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
           // document.add(new Paragraph(" " + produtosPedido.get(i).getQtd() +" || " + produtosPedido.get(i).getNome()));
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
        pedido_id = Long.valueOf(id);
        table_index = table;
        System.out.println("Dado recebido: " + pedido_id);

        recuperarDadosPedido();
        recuperarProdutos();
    }
    private void recuperarProdutos(){
        produtos.clear();
        produtos = ProdutoService.findAll();
        //recuperar todos os pedidos
    }
    private void recuperarProdutosPedido(){
        produtosPedido.clear();
        nomeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduto().getNome()) );
        qtdCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        //recuperar produtos do pedido

        produtosPedido = pedido.getProdutos();
        tableProdutos.setItems(produtosPedido);
    }
    private void recuperarProdutoCriado(String nome) throws SQLException {
        //recuperar pedido criado por nome
        addproduto_pedido(Math.toIntExact(produto.getId()), produto.getNome());
    }
    private void recuperarDadosPedido(){
        //recuperar dados do pedido
        pedido = PedidoService.getById(pedido_id);
        System.out.println("Pedido Recuperado: \n" + pedido);
        if(pedido != null){
            setData(pedido);
        }else{
            JFXDialog dialog = AlertDialogModel.alertDialogErro("Houve um problema ao carregar o pedido, tente novamente mais tarde",stackPane);
            dialog.show();
        }
    }

    private void setData(OrdemPedido pedido) {
        textNome.setText(pedido.getCliente().getNome());
        SimpleDateFormat fDate = new SimpleDateFormat("yyyy/MM/dd");
        textData_Entrada.setText(fDate.format(pedido.getEntradaDate()));
        textEndereco.setText(pedido.getCliente().getEndereco() + ", " + pedido.getCliente().getBairro().getNome());
        textTelefone.setText(pedido.getCliente().getTelefone());
        textPagamento.setText(pedido.getForma_pagamento());
    }
    //Metodos Iniciais

    //Metodos de Negocios
    private Produto verificarProdutoExistente(String text) {
        AtomicReference<Produto> produto = new AtomicReference<Produto>();
        List<Produto> prodList = ProdutoService.getByNome(text);
        if(prodList.size() <=0){
            System.out.println("Produto Inexistente");
            JFXButton btnOK = DefaultComponents.defaultButton("CRIAR");
            btnOK.setOnAction(e -> {
                String output = ProdutoService.insert(new Produto(null, text));
                if(!(output.equals(""))){
                    produto.set(jsonProdToObj(output));
                    dialog.close();
                    recuperarProdutos();
                }else{
                    JFXDialog dialogErro = AlertDialogModel.alertDialogErro("Houve um problema ao criar o produto, tente novamente.", stackPane);
                    dialogErro.show();
                }
            });
            dialog = AlertDialogModel.alertDialogAction("Produto nao encontrado, deseja criar um novo?",stackPane,btnOK);
            dialog.show();
        }else{
            System.out.println("Produto existente");
            produto.set(prodList.get(0));
        }
        return produto.get();
    }


    private Produto jsonProdToObj(String text){
        Gson gson = new Gson();
        return gson.fromJson(new String(text.getBytes()), Produto.class);
    }

    private void addproduto_pedido(int id, String nome) {
        int quantidade = Integer.parseInt(edtQTD.getText());
        //adicionar produto
    }
    private void adicionarProdutoLista(){
        prod_nome = edtNomeProduto.getText().toUpperCase().trim();

        if(prod_nome.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os Dados!");
            alert.showAndWait();
        }else{
            System.out.println("Adicionando produto a lista atual");
            produtosPedido.add(produtoAtual.toOrderProduct(pedido, Integer.parseInt(edtQTD.getText().trim())));
            restartAdd();
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

        switch (pedido.getStatus()){
            case 1 :
                pedido.setStatus(2);
                pedido.setTriagemHora(date);
                break;
            case 2 :
                pedido.setStatus(3);
                pedido.setCheckoutHora(date);
                break;
            case 3 :
                pedido.setStatus(4);
                pedido.setEnviadoHora(date);
                break;
            case 4 :
                pedido.setStatus(5);
                pedido.setFinalizadoHora(date);
                break;
        }
        Boolean aBoolean = salvarPedido();
        if (aBoolean == true) {
            System.out.println("Pedido Atualizado com Sucesso!");
        } else if (aBoolean == false) {
            JFXDialog a = AlertDialogModel.alertDialogErro("Houve um problema ao atualizar o Status do Pedido", stackPane);
            a.show();
        } else if (aBoolean == false) {
            JFXDialog b = AlertDialogModel.alertDialogErro("Houve um problema ao atualizar o Status do Pedido", stackPane);
            b.show();
        }
    }
    //Metodos de Negocios

    //Metodos de Controle
    private void restartAdd() {
        System.out.println("Reiniciando Componentes");
        edtNomeProduto.clear();
        edtQTD.clear();
        edtQTD.setText("1");
        edtNomeProduto.requestFocus();
        tableProdutos.setItems(produtosPedido);
    }

    private void fecharJanela(){
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    //Metodos de Controle
}
