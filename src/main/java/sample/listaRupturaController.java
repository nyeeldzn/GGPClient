package sample;

import Services.ListaRupturaService;
import Services.ProdutoService;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.AlertDialogModel;
import helpers.DefaultComponents;
import helpers.ExcluirDialogModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static helpers.DefaultComponents.*;

public class listaRupturaController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<ListaRuptura> tableLista;
    @FXML
    private TableColumn<ListaRuptura, Long> idCol;
    @FXML
    private TableColumn<ListaRuptura, String> dataeCol;
    @FXML
    private TableColumn<ListaRuptura, String> descCol;
    @FXML
    private JFXButton btnNovo;
    @FXML
    private JFXButton btnEditar;
    @FXML
    private JFXButton btnExcluir;
    @FXML
    private JFXDatePicker pickerDataInicial;
    @FXML
    private JFXDatePicker pickerDataFinal;
    @FXML
    private JFXCheckBox checkBoxDatas;
    @FXML
    private JFXButton btnSearch;

    JFXTextField edtProduto;
    JFXDialog dialog;
    TableView<Produto> tableProdutosLista;
    JFXButton btnADD;

    String prod_nome;
    String dataInicial = null;
    String dataFinal = null;
    String dataAtual = null;
    Usuario user;
    ListaRuptura modelLista;
    boolean isSelectedLista;
    Produto produtoSelecionado;

    List<Produto> produtos = FXCollections.observableArrayList();
    ObservableList<ListaRuptura> listaRupturas = FXCollections.observableArrayList();
    ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComponents();
        recuperarusuario();
        verificarListaNoDia();
        recuperarProdutos();


    }




    //metodo iniciais
    private void setupComponents() {
        pickerDataInicial.setValue(
                nowOnDate()
        );
        pickerDataInicial.setOnAction( e -> dataInicial = onDate(pickerDataInicial));
        pickerDataInicial.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        pickerDataFinal.setValue(
                nowOnDate()
        );
        pickerDataFinal.setOnAction(e -> dataFinal = onDate(pickerDataFinal));
        pickerDataFinal.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dataeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate()));
        descCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDesc()));
        btnNovo.setOnAction((e) -> {
            boolean state = verificarListaNoDia();
            if(state == true){
                int larguraPadrao = 85;
                JFXButton btnConfirmar = button("SIM", "CHECK", larguraPadrao);
                JFXButton btnCancelar = button("NÃO", "TIMES", larguraPadrao);
                JFXDialog dialog = ExcluirDialogModel.alertDialogErro("Já existe uma lista criada no dia, deseja criar um nova?", stackPane, btnConfirmar, btnCancelar);
                btnConfirmar.setOnAction((actionEvent) -> {
                    dialog.close();
                    criarLista();
                });
                btnCancelar.setOnAction((actionEvent) -> {
                    dialog.close();
                });
                dialog.show();
            }else {
                criarLista();
            }
        });
        btnEditar.setOnAction((e) -> {
            try {
                alertDialogBuscaProdutosListaRuptura();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        btnExcluir.setOnAction((e) -> {
            if(isSelectedLista == true){
                excluirLista();
            }else{
                JFXDialog dialog = AlertDialogModel.alertDialogErro("Selecione uma lista", stackPane);
                dialog.show();
            }
        });
        tableLista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListaRuptura>() {
            @Override
            public void changed(ObservableValue<? extends ListaRuptura> observable, ListaRuptura oldValue, ListaRuptura newValue) {
                if(tableLista.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tableLista.getSelectionModel();
                    modelLista = (ListaRuptura) tableLista.getSelectionModel().getSelectedItem();
                    isSelectedLista = true;
                    System.out.println(modelLista.getId());
                }
            }
        });
        btnSearch.setOnAction((e) -> {
            if(checkBoxDatas.isSelected()){
                //buscar por datas
                listaRupturas.clear();
                recuperarListasPorData();
            }else{
                refreshTable();
            }
        });
    }
    private void recuperarusuario() {
        //recuperar usuario
    }
    private boolean verificarListaNoDia() {
        System.out.println("Verificando Lista do dia");
        boolean state = false;
        PedidoFindJsonHelper dBH = new PedidoFindJsonHelper(dataAtual, dataAtual);
        ObservableList<ListaRuptura> listaRupturasLocal = FXCollections.observableArrayList(ListaRupturaService.findAllByDate(dBH));

        System.out.println("Tamanho da lista: " + listaRupturasLocal.size());
            if (listaRupturasLocal.size() <= 0) {
                state = false;
                JFXButton btnOK = defaultButton("Criar");
                JFXButton btnNO = defaultButton("Não");
                JFXDialog dialog = ExcluirDialogModel.alertDialogErro("Não foi encontrada lista no dia, deseja criar uma nova?", stackPane,btnOK, btnNO );
                dialog.show();
                btnOK.setOnAction((e) ->{
                    criarLista();
                    dialog.close();
                });
                btnNO.setOnAction((e) -> {
                    dialog.close();
                });
            }else{
                //buscar todos
                state = true;
                recuperarTodosAsListas();
            }

        return state;
        }
    private void recuperarTodosAsListas() {
        listaRupturas.clear();
        listaRupturas = FXCollections.observableArrayList(ListaRupturaService.findAll());
        tableLista.setItems(listaRupturas);
    }
    private void recuperarListasPorData() {
        //recuperar lista rupturas por data
        listaRupturas.clear();
        PedidoFindJsonHelper dBH = new PedidoFindJsonHelper(dataInicial, dataFinal);
        listaRupturas = FXCollections.observableArrayList(ListaRupturaService.findAllByDate(dBH));
        tableLista.setItems(listaRupturas);
    }

    //metodos iniciais

    //metodos de negocios


    private void restartAdd() {
        edtProduto.clear();
        edtProduto.requestFocus();
    }

    private void recuperarProdutoCriado(String nome) throws SQLException {
        //recuperar produto por nome retornar id
        //produto = new Produto(id, nome_produto);
        //addproduto_pedido(produto.getId(), produto.getNome());
    }

    private void addproduto_pedido(Long id, String nome) {
        System.out.println("Produto de ID: " + id + "Nome: " + nome + " sendo adicionado ao pedido...");
        //inserir produto e caso ok retornar refreshtable
    }

    private void excluirLista(){
        JFXButton btnExcluir = defaultButton("SIM");
        JFXButton btnCancelar = defaultButton("NAO");

        JFXDialog dialog = ExcluirDialogModel.alertDialogErro("Deseja realmente excluir lista?", stackPane ,btnExcluir,  btnCancelar);
        dialog.show();
        btnCancelar.setOnAction((e) -> {
            dialog.close();
        });
        btnExcluir.setOnAction((e) -> {
            if(ListaRupturaService.delete(modelLista.getId()) == 2){
                refreshTable();
                dialog.close();
            }else{
                JFXDialog dialog2 = AlertDialogModel.alertDialogErro("Houve um problema na exclusao", stackPane);
                dialog2.show();
            }
        });

    }
    private void criarLista() {
        //inserir lista de ruptura, retornar refreshtable caso ok
        ListaRuptura list = new ListaRuptura(null, "Lista Padrao", dataAtual);
        ListaRupturaService.insert(list);
        recuperarTodosAsListas();
    }

    private void recuperarProdutos(){
        produtos.clear();
        produtos = FXCollections.observableArrayList(ProdutoService.findAll());
    }
    private void recuperarProdutosLista(){
        //recuperar produtos da lista pelo id da lista
        listaProdutos.clear();
        List<RupturaProduto> listaTemp = modelLista.getProdutoList();
        for(int i = 0; i < listaTemp.size(); i++){
            listaProdutos.add(listaTemp.get(i).getProduto());
        }
        tableProdutosLista.setItems(listaProdutos);
    }

    private LocalDate nowOnDate(){
        LocalDate localDate = LocalDate.now();
        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dataFinal = formatData(localDate.toString());
        dataInicial = formatData(localDate.toString());
        dataAtual = formatData(localDate.toString());
        System.out.println("Data Atual: " + dataInicial);
        System.out.println("Data Atual: " + dataFinal);
        return localDate;
    }
    private String onDate (JFXDatePicker picker){
        LocalDate localDate = picker.getValue();
        String dataFormatada = formatData(localDate.toString());
        System.out.println(dataFormatada);
        //dataInicial = formatData(localDate.toString());
        //datePicker.setPromptText(dataInicial);
        return dataFormatada;
    }
    public String formatData (String data){
        SimpleDateFormat sdf = null;
        Date d = null;
        try{
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            d = sdf.parse(data);
            sdf.applyPattern("yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(d);
    }
    private void autoCompleteTextField(String search){
        String complete = "";
        int start = search.length();
        int last = search.length();


        for(int i = 0; i<produtos.size(); i++){
            if(produtos.get(i).getNome().toUpperCase().startsWith(search.toUpperCase())){
                complete = produtos.get(i).getNome().toUpperCase();
                last = complete.length();
                break;
            }
        }
        if(last>start){
            edtProduto.setText(complete);
            edtProduto.positionCaret(last);
            edtProduto.selectPositionCaret(start);
        }



    }
    //metodos de negocios

    //objetos

    public void alertDialogBuscaProdutosListaRuptura() throws IOException {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialogLayout.setBody(
                formularioProdutosLista()
        );
        dialog.show();
        recuperarProdutosLista();
    }
    public AnchorPane formularioProdutosLista(){
        AnchorPane pane = new AnchorPane();
        VBox vboxPrincipal = defaultVBox();
        JFXButton btnCancelar = defaultButton("SAIR");
        JFXButton btnSalvar = buttonIcon("SALVAR", "SAVE", 150);
        btnADD = DefaultComponents.buttonIcon("", "PLUS", 50);
        tableProdutosLista = new TableView<>();
        TableColumn<Produto, String> idCol = new TableColumn<>();
        idCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId().toString()));
        idCol.setText("ID");
        TableColumn<Produto, String> nomeCol = new TableColumn<>();
        nomeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        nomeCol.setText("Nome do Produto");
        nomeCol.setPrefWidth(400);
        tableProdutosLista.getColumns().addAll(idCol, nomeCol);

        edtProduto = textFieldPadrao(500);
        edtProduto.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        setupEdtProduto();

        btnADD.setOnAction(e -> {
            listaProdutos.add(produtoSelecionado);
            restartAdd();
        });
        btnADD.setOnKeyPressed(e -> {
            listaProdutos.add(produtoSelecionado);
            restartAdd();
        });

        HBox row1 = defaultHBox();
        HBox row3 = defaultHBox();



        VBox vBox = defaultVBox();
        vBox.setSpacing(5);
        Text texto = defaultText("ADIÇÃO PRODUTO");
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.setStyle("-fx-background-color: navy; -fx-background-radius: 10");
        //vBox.setStyle("-fx-background-radius: 15");


        vBox.getChildren().addAll(texto, row1);
        row1.getChildren().addAll(
                edtProduto, btnADD
        );

        row3.getChildren().addAll(
                btnSalvar, btnCancelar
        );

        row3.setAlignment(Pos.CENTER_RIGHT);

        vboxPrincipal.getChildren().addAll(vBox, tableProdutosLista, row3);

        btnCancelar.setOnAction(e -> dialog.close());
        btnSalvar.setOnAction(e -> salvarLista());


        pane.getChildren().add(vboxPrincipal);
        return pane;
    }



    public JFXButton button(String texto, String glyph, double larguraPadrao){
        JFXButton button = new JFXButton(texto, FontIcon(glyph));
        button.setPrefSize(larguraPadrao, 50);
        button.setStyle("-fx-background-color: #d3d3d3");
        button.setAlignment(Pos.CENTER_LEFT);
        return button;
    }
    public FontAwesomeIconView FontIcon(String glyphName){
        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName(glyphName);
        icon.setSize("35.0");
        return icon;
    }

    private void setupEdtProduto() {
        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i<produtos.size(); i++){
            nomes.add(produtos.get(i).getNome());
        }

        JFXAutoCompletePopup<Produto> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setPrefWidth(500);
        autoCompletePopup.getSuggestions().addAll(produtos);

        autoCompletePopup.setSelectionHandler(event -> {
            edtProduto.setText(event.getObject().getNome());
            produtoSelecionado = event.getObject();
            btnADD.requestFocus();
            // you can do other actions here when text completed
        });

        // filtering options
        edtProduto.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.getNome().toLowerCase().contains(edtProduto.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || edtProduto.getText().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(edtProduto);
            }
        });


    }


    //objetos

    //metodos de controle

    private boolean salvarLista() {
        boolean state = false;
        List<RupturaProduto> produtos = ListaRuptura.produtoListToRupturaProduto(modelLista, listaProdutos);
        modelLista.setProdutoList(produtos);

        System.out.println("Lista enviada para atualizacao: \n" + modelLista);
        switch(ListaRupturaService.update(modelLista)){
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

    private void fecharJanela(){
        dialog.close();
    }

    private void refreshTable(){
        listaRupturas.clear();
        recuperarTodosAsListas();
    }
    //metodos de controle
}
