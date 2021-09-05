package sample.MainController.Produtos;

import Services.ProdutoService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import helpers.AlertDialogModel;
import helpers.DefaultComponents;
import helpers.ExcluirDialogModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;
import models.Produto;
import sample.MainController.MainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class produtosController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<Produto> tableView;

    @FXML
    private TableColumn<Produto, Integer> idCol;

    @FXML
    private TableColumn<Produto, String> nomeCol;

    @FXML
    private TableColumn<Produto, String> vendasCol;

    @FXML
    private JFXButton btnNovo;

    @FXML
    private JFXButton btnEditar;

    @FXML
    private JFXButton btnExcluir;

    @FXML
    private JFXTextField edtNome;

    @FXML
    private JFXTextField edtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnImport;

    JFXDialog dialog;
    BorderPane border;
    FilteredList<Produto> filteredData;
    ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
    Produto produto;
    int selectedIndex;
    boolean isSelected = false;

    //connection

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recuperarUsuario();
        prepareTableView();
        setupComponentes();
    }


    //Metodos Iniciais
    private void recuperarUsuario() {
        //recuperar usuario
    }
    private void prepareTableView(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Produto>() {
            @Override
            public void changed(ObservableValue<? extends Produto> observable, Produto oldValue, Produto newValue) {
                if(tableView.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
                    produto = (Produto) tableView.getSelectionModel().getSelectedItem();
                    selectedIndex = selectionModel.getSelectedIndex();
                    isSelected = true;
                    System.out.println(produto.getNome());
                }
            }
        });
        try {
            recuperarProdutos();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    private void recuperarProdutos() throws SQLException {
        listaProdutos.clear();
        //recuperar todos os produtos
        ArrayList<Produto> recup = ProdutoService.findAll();
        for(int i = 0; i<recup.size(); i++){
            listaProdutos.add(recup.get(i));
        }
        System.out.println("Produtos recuperados com sucesso!");
        tableView.setItems(listaProdutos);
    }
    private void setupComponentes() {
        btnNovo.setOnAction((e) -> {
            novoProduto();
        });
        btnEditar.setOnAction((e) -> {
                if (isSelected == true) {
                    alertDialogEditarProduto();
                }else{
                 JFXDialog dialog = AlertDialogModel.alertDialogErro("Selecione um produto", stackPane);
                 dialog.show();
                }
        });
        btnExcluir.setOnAction((e) -> {
                JFXButton confirmar = new JFXButton("EXCLUIR");
                JFXButton cancelar = new JFXButton("CANCELAR");
                dialog = ExcluirDialogModel.alertDialogErro("DESEJA REALMENTE DELETAR O PRODUTO?", stackPane, confirmar, cancelar);
                dialog.show();
                confirmar.setOnAction((actionEvent) -> {
                    excluirProduto();
                });
                cancelar.setOnAction((actionEvent) ->{
                    dialog.close();;
                });
        });
        edtSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                pesquisarProduto();
            }
        });
        edtSearch.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        btnPrint.setOnAction((e) -> {
            File file = DefaultComponents.fileChooserSave(stackPane, "Excel files (*.xls)", "*.xls");
            if(file != null){
                gerarDocumentoXLS(file);
            }
        });
        btnImport.setOnAction((e) -> {
            intentImport();
        });
    }

    private void intentImport() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/importarProdutosScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    refreshTable();
                    System.out.println("Janela fechada");
                }
            });
        } catch (IOException exception){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void editarProduto() {
        String nome_produto = edtNome.getText().toUpperCase().trim();
        if(!(nome_produto.equals(""))){
            System.out.println("Iniciando update");
            switch (ProdutoService.update(new Produto(produto.getId(), nome_produto))){
                case 0:
                    dialog.close();
                    JFXDialog dialoginner = AlertDialogModel.alertDialogErro("Houve um problema ao atualizar o produto.", stackPane);
                    dialoginner.show();
                    break;
                case 2:
                    dialog.close();
                    refreshTable();
                    break;
            }
        }else{
            alertDialogAlert();
        }    }
    //Metodos Iniciais

    //Metodos de Negocios
    private void novoProduto(){
        alertDialogProdutos();
    }
    private boolean excluirProduto(){
        boolean state = false;
        if(isSelected != false){
            System.out.println("Item selecionado e: " + produto.getId());
            switch(ProdutoService.delete(produto.getId())){
                case 0 :
                    dialog.close();
                    dialog = AlertDialogModel.alertDialogErro("Houve um problema ao tentar excluir o produto", stackPane);
                    dialog.show();
                    break;
                case 1 :
                    dialog.close();
                    dialog = AlertDialogModel.alertDialogErro("Um produto associado a um pedido nao pode ser excluido", stackPane);
                    dialog.show();
                    break;
                case 2:
                    refreshTable();
                    dialog.close();
                    break;
            }
        }else{
            JFXDialog dialog = AlertDialogModel.alertDialogErro("Selecione um Produto", stackPane);
            dialog.show();
        }
        return state;
    }
    private void pesquisarProduto(){
        filteredData = new FilteredList<>(listaProdutos, b -> true);
        edtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(produto -> {
                if(newValue == null && newValue.isEmpty()){
                    return true;
                }

                String upperCaseFilter = newValue.toUpperCase().trim();
                if(produto.getNome().toUpperCase().indexOf(upperCaseFilter) != -1){
                    return true;
                }else
                    return false;
            });

            SortedList<Produto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
        });
    }
    private void inserirProduto(String nome_produto) throws SQLException {
        //inserir produto
        Produto produto = new Produto(null, nome_produto);
        String output = ProdutoService.insert(produto);
        if(!(output.equals(""))){
            refreshTable();
            dialog.close();
        }else{
            System.out.println("Houve um problema na insercao");
        }
        //retornar refreshtable
    }
    public void gerarDocumentoXLS(File file){
        System.out.println("Inicnado exportação");
        ObservableList<Produto> listaTabela = FXCollections.observableArrayList();
        int tableSize = tableView.getItems().size();
        for(int a = 0; a<tableSize; a++){
            System.out.println("Busca: " + a);
            System.out.println(tableSize);
            listaTabela.add(tableView.getItems().get(a));
        }
        try{
            WritableWorkbook planilha = Workbook.createWorkbook(file);
            // Adcionando o nome da aba
            WritableSheet aba = planilha.createSheet("Lista de Produtos", 0);
            //Cabeçalhos
            String cabecalho[] = new String[2];
            cabecalho[0] = "ID:";
            cabecalho[1] = "Nome do Produto:";
            // Cor de fundo das celular
            Colour bckColor = Colour.DARK_GREEN;
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setBackground(bckColor);
            // Cor e tipo de Fonte
            WritableFont fonte = new WritableFont(WritableFont.ARIAL);
            fonte.setColour(Colour.GOLD);
            cellFormat.setFont(fonte);

            // escrever o Header para o xls
            for(int i =0; i< cabecalho.length; i++){
                Label label = new Label(i, 0, cabecalho[i]);
                aba.addCell(label);
                WritableCell cell = aba.getWritableCell(i, 0);
                cell.setCellFormat(cellFormat);
            }

            for (int linha = 1; linha < listaTabela.size(); linha++) {
                Label label = new Label(0, linha, String.valueOf(listaTabela.get(linha).getId()));
                aba.addCell(label);
                label = new Label(1, linha, listaTabela.get(linha).getNome());
                //int count = DefaultComponents.countOfChar(listaTabela.get(linha).getNome());
                aba.setColumnView(1, 120);
                aba.addCell(label);
            }

            planilha.write();
            //fecha o arquivo
            planilha.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Fim");
    }
    //Metodos de Negocios

    //Objetos
    private void alertDialogAlert() {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton buttonConfirmar = new JFXButton("OK");
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);

        buttonConfirmar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            dialog.close();
        });
        dialogLayout.setPrefSize(250, 150);
        dialogLayout.setBody(
                borderPaneAlert()
        );
        dialogLayout.setActions(buttonConfirmar);
        dialog.show();
    }
    private void alertDialogProdutos() {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton buttonCancelar = new JFXButton("Cancelar");
        JFXButton buttonConfirmar = new JFXButton("Salvar");
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        buttonCancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) ->{
            dialog.close();
        });
        buttonConfirmar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String nome_produto = edtNome.getText().toUpperCase().trim();
            if(!(nome_produto.equals(""))){
                try {
                    inserirProduto(nome_produto);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }else{
                alertDialogAlert();
            }
        });
        dialogLayout.setPrefSize(250, 150);
        dialogLayout.setBody(
                borderPaneNovoProduto()
        );
        dialogLayout.setActions(buttonConfirmar,buttonCancelar);
        dialog.show();
    }
    private void alertDialogEditarProduto() {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton buttonCancelar = new JFXButton("Cancelar");
        JFXButton buttonConfirmar = new JFXButton("Salvar");
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        buttonCancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) ->{
            dialog.close();
        });
        buttonConfirmar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            editarProduto();
        });
        dialogLayout.setPrefSize(250, 150);
        dialogLayout.setBody(
                borderPaneNovoProduto()
        );
        dialogLayout.setActions(buttonConfirmar,buttonCancelar);
        dialog.show();
        edtNome.setText(produto.getNome());
    }

    public BorderPane borderPaneNovoProduto(){
        border = new BorderPane();
        edtNome = new JFXTextField();
        edtNome.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        edtNome.setStyle("-fx-background-color: #d3d3d3");
        Text text = new Text("Nome do Produto");
        text.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 15));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(text ,edtNome);
        border.setCenter(vbox);
        return border;
    }
    public BorderPane borderPaneAlert(){
        border = new BorderPane();
        Text texto = new Text();
        texto.setText("Preencha o campo de nome");
        texto.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        border.setCenter(texto);
        return border;
    }

    //Objetos

    //Metodos de Controle
    private void refreshTable(){
        try {
            recuperarProdutos();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    };
    //Metodos de Controle

}
