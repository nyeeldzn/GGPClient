package sample.MainController.Clientes;

import Services.BairroService;
import Services.ClienteService;
import Services.PedidoService;
import com.jfoenix.controls.*;
import helpers.*;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.*;
import models.*;
import sample.MainController.MainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class clientesController implements Initializable {

        @FXML
        private StackPane stackPane;

        @FXML
        private TableView<Cliente> tableView;

        @FXML
        private TableColumn<Cliente, Integer> idCol;

        @FXML
        private TableColumn<Cliente, String> nomeCol;

        @FXML
        private TableColumn<Cliente, String> endCol;

        @FXML
        private TableColumn<Cliente, String> telCol;


        @FXML
        private JFXButton btnEditar;

        @FXML
        private JFXButton btnExcluir;

        @FXML
        private JFXButton btnDetalhes;

        @FXML
        private JFXButton btnPrintClientes;

        @FXML
        private JFXTextField edtSearch;

        @FXML
        private JFXButton btnImport;

        private JFXDatePicker datePickerInicial;
        private JFXDatePicker datePickerFinal;
        private JFXComboBox comboBoxFiltroStatus;
        TableView<OrdemPedido> tableViewPedidos;
        FilteredList<Cliente> filteredData;


        ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
        ObservableList<String> listaComboBox = FXCollections.observableArrayList(
          "Pedidos Pendentes","Pedidos em Triagem","Pedidos Finalizados", "Todos"
        );
        ObservableList<String> listaQuerys = FXCollections.observableArrayList(
                "Ordem_De_Pedido","Ordem_De_Pedido_Triagem","Ordem_De_Pedido_Finalizado"
        );
        String dataInicial = "";
        String dataFinal = "";

        ObservableList<Bairro> bairros = FXCollections.observableArrayList();

        ObservableList<OrdemPedido> listaPedidos = FXCollections.observableArrayList();
        ObservableList<OrdemPedido> listaPedidosTriagem = FXCollections.observableArrayList();
        ObservableList<OrdemPedido> listaPedidosFinalizados = FXCollections.observableArrayList();
        ObservableList<OrdemPedido> listaPedidosTodos = FXCollections.observableArrayList();

        String query = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        Cliente cliente = null;
        boolean selectedCliente = false;
        int cb_selectedIndex = 0;
        Usuario user;

        JFXDialog dialog;
        @Override
        public void initialize(URL location, ResourceBundle resources) {
                recuperarUsuario();
                setupComponentes();
                prepararTableView();
        }



    //metodos iniciais
            private void recuperarUsuario() {
                //recuperar usuario atual
            }
            private void setupComponentes(){
                  btnImport.setOnAction((e) -> {
                      intentImport();
                  });
                  btnExcluir.setOnAction((e) -> {
                      if(UserPrivilegiesVerify.permissaoVerBotao(user, "admin") == true){
                          alertDialogExclusao();
                      }else{
                          JFXDialog dialog = AlertDialogModel.alertDialogErro("Você não tem permissão para isso.",stackPane);
                          dialog.show();
                      }
                  });
                  btnEditar.setOnAction((e) ->{
                      if(selectedCliente == true){
                              alertDialogClientes();
                      }else if(selectedCliente == false){
                          System.out.println("Selecione um cliente");
                          JFXDialog dialogErro = AlertDialogModel.alertDialogErro("Selecione um Cliente",stackPane);
                          dialogErro.show();
                      }
                  });
                  btnDetalhes.setOnAction((e) -> {
                  if(selectedCliente == true){
                      try {
                          alertDialogDetalhes();
                          comboBoxFiltroStatus.getSelectionModel().select(0);
                          comboBoxFiltroStatus.valueProperty().addListener(new ChangeListener() {
                              @Override
                              public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                  verificarComboBox();
                              }
                          });
                      } catch (IOException exception) {
                          exception.printStackTrace();
                      }
                  }else if(selectedCliente == false){
                      System.out.println("Selecione um cliente");
                      JFXDialog dialogErro = AlertDialogModel.alertDialogErro("Selecione um Cliente",stackPane);
                      dialogErro.show();
                  }


              });
                  edtSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        pesquisarCliente();
                    }
                });
                  edtSearch.setTextFormatter(new TextFormatter<Object>((change) -> {
                    change.setText(change.getText().toUpperCase());
                    return change;
                }));

                  btnPrintClientes.setOnAction((e) ->{
                      if(UserPrivilegiesVerify.permissaoVerBotao(user, "admin") == true){
                          File file = DefaultComponents.fileChooserSave(stackPane, "DOCUMENTO EXCEL (*.XLS)", "*.xls");
                          gerarDocumentoXLS(file);
                      }
                  });

          }
            private void prepararTableView(){
                        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                        nomeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
                        endCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEndereco() + c.getValue().getBairro().getNome()));
                        telCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));
                        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cliente>() {
                            @Override
                            public void changed(ObservableValue<? extends Cliente> observable, Cliente oldValue, Cliente newValue) {
                                if(tableView.getSelectionModel().getSelectedItem() != null){
                                    TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
                                    cliente = (Cliente) tableView.getSelectionModel().getSelectedItem();
                                    selectedCliente = true;
                                    System.out.println(cliente.getNome());
                                }
                            }
                        });
                        try {
                                recuperarClientes();
                        } catch (SQLException exception) {
                                exception.printStackTrace();
                        }
                }
        //metodos iniciais


        //metodos de negocios
            private void iniciarDetalhesScreen(OrdemPedido pedido) {
            System.out.println("Iniciando If Statment");
            intentData intent = intentData.getINSTANCE();
            int selectedTable = 0;
            System.out.println("Pedido de status: " + pedido.getStatus()
                    + " Horario de triagem: "
                    + pedido.getTriagemHora()
                    + " Horario checkout: "
                    + pedido.getCheckoutHora()
                    + " Horario de finalizaçao: "
                    + pedido.getFinalizadoHora());

            if(pedido.getStatus() == 1 && pedido.getTriagemHora().equals("")){
                //table 1
                selectedTable = 1;
                System.out.println("Pedido na tabela 1");
            }else if(pedido.getStatus() == 1 && !(pedido.getTriagemHora().equals(""))){
                //table 2
                selectedTable = 2;
                System.out.println("Pedido na tabela 2");
            }else if(pedido.getStatus() == 4){
                //table 2
                selectedTable = 2;
                System.out.println("Pedido na tabela 2");
            }else if(pedido.getStatus() == 5){
                //table 3
                selectedTable = 3;
                System.out.println("Pedido na tabela 3");
            }

            intent.setTableIndex(selectedTable);
            intent.setDadosteste(String.valueOf(pedido.getId()));
            if(selectedTable != 0 && pedido.getId() != 0){
                try {
                    Parent parent = FXMLLoader.load(getClass().getResource("/detalhesPedido.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                } catch (IOException exception){
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, exception);
                }
            }



        }
            private void alertDialogExclusao() {
            JFXButton btnExcluir = defaultButton("CONFIRMAR");
            JFXButton btnCancelar = defaultButton("CANCELAR");
            JFXDialog dialogExclusao = ExcluirDialogModel.alertDialogErro("TEM CERTEZA QUE DESEJA EXCLUIR?", stackPane, btnExcluir, btnCancelar);
            dialogExclusao.show();
            btnCancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                dialogExclusao.close();
            });
            btnExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                    excluirCliente();
                    dialogExclusao.close();
            });
        }



    public void gerarDocumentoXLS(File file){
            System.out.println("Iniciando exportação");
            //Busca todos os itens da tabela
            ObservableList<Cliente> listaTabela = FXCollections.observableArrayList();
            int tableSize = tableView.getItems().size();
            for(int a = 0; a<tableSize; a++){
                System.out.println("Busca: " + a);
                System.out.println(tableSize);
                listaTabela.add(tableView.getItems().get(a));
            }
            //
            try{
                WritableWorkbook planilha = Workbook.createWorkbook(file);
                // Adcionando o nome da aba
                WritableSheet aba = planilha.createSheet("Lista de Produtos", 0);
                //Cabeçalhos
                String cabecalho[] = new String[9];
                cabecalho[0] = "ID:";
                cabecalho[1] = "Nome do Cliente:";
                cabecalho[2] = "Endereco do Cliente:";
                cabecalho[3] = "Telefone do Cliente:";
                cabecalho[4] = "Data de Cadastro:";


                // Cor de fundo das celular
                Colour bckColor = Colour.DARK_BLUE2;
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

                for (int linha = 1; linha <= listaTabela.size(); linha++) {
                    Label label = new Label(0, linha, String.valueOf(listaTabela.get(linha - 1).getId()));
                    aba.addCell(label);
                    label = new Label(1, linha, listaTabela.get(linha- 1).getNome());
                    //int count = DefaultComponents.countOfChar(listaTabela.get(linha).getCliente_nome());
                    aba.setColumnView(1, 55);
                    aba.addCell(label);
                    label = new Label(2, linha, listaTabela.get(linha- 1).getEndereco());
                    aba.setColumnView(2, 55);
                    aba.addCell(label);
                    label = new Label(3, linha, listaTabela.get(linha- 1).getTelefone());
                    aba.setColumnView(3, 10);
                    aba.addCell(label);
                    label = new Label(4, linha, listaTabela.get(linha- 1).getData_cadastro());
                    aba.setColumnView(4, 10);
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
            public void gerarDocumentoPedidosXLS(File file){
        System.out.println("Iniciando exportação");
        //Busca todos os itens da tabela
        ObservableList<OrdemPedido> listaTabela = FXCollections.observableArrayList();
        int tableSize = tableViewPedidos.getItems().size();
        for(int a = 0; a<tableSize; a++){
            System.out.println("Busca: " + a);
            System.out.println(tableSize);
            listaTabela.add(tableViewPedidos.getItems().get(a));
        }
        //
        try{
            WritableWorkbook planilha = Workbook.createWorkbook(file);
            // Adcionando o nome da aba
            WritableSheet aba = planilha.createSheet("Lista de Produtos", 0);
            //Cabeçalhos
            String cabecalho[] = new String[9];
            cabecalho[0] = "ID:";
            cabecalho[1] = "Nome do Cliente:";
            cabecalho[2] = "Endereco do Cliente:";
            cabecalho[3] = "Telefone do Cliente:";
            cabecalho[4] = "Data de Entrada:";
            cabecalho[5] = "H. Entrada";
            cabecalho[6] = "H. Triagem";
            cabecalho[7] = "H. Checkout";
            cabecalho[8] = "H. Finaliz.";


            // Cor de fundo das celular
            Colour bckColor = Colour.DARK_BLUE2;
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

            for (int linha = 1; linha <= listaTabela.size(); linha++) {
                Label label = new Label(0, linha, String.valueOf(listaTabela.get(linha - 1).getId()));
                aba.addCell(label);
                label = new Label(1, linha, listaTabela.get(linha- 1).getCliente().getNome());
                //int count = DefaultComponents.countOfChar(listaTabela.get(linha).getCliente_nome());
                aba.setColumnView(1, 35);
                aba.addCell(label);
                label = new Label(2, linha, listaTabela.get(linha- 1).getCliente().getEndereco());
                aba.setColumnView(2, 35);
                aba.addCell(label);
                label = new Label(3, linha, listaTabela.get(linha- 1).getCliente().getTelefone());
                aba.setColumnView(3, 14);
                aba.addCell(label);
                label = new Label(4, linha, listaTabela.get(linha- 1).getEntradaDate().toString());
                aba.setColumnView(4, 10);
                aba.addCell(label);
                label = new Label(5, linha, listaTabela.get(linha- 1).getEntradaHora().toString());
                aba.addCell(label);
                label = new Label(6, linha, listaTabela.get(linha- 1).getTriagemHora().toString());
                aba.addCell(label);
                label = new Label(7, linha, listaTabela.get(linha- 1).getCheckoutHora().toString());
                aba.addCell(label);
                label = new Label(8, linha, listaTabela.get(linha- 1).getFinalizadoHora().toString());
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
            private void pesquisarCliente(){
            filteredData = new FilteredList<>(listaClientes, b -> true);
            edtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
                filteredData.setPredicate(cliente -> {
                    if(newValue == null && newValue.isEmpty()){
                        return true;
                    }

                    String upperCaseFilter = newValue.toUpperCase().trim();
                    if(cliente.getNome().toUpperCase().indexOf(upperCaseFilter) != -1){
                        return true;
                    }else if(cliente.getEndereco().toUpperCase().indexOf(upperCaseFilter) != -1){
                        return true;
                    }else if(cliente.getTelefone().toUpperCase().indexOf(upperCaseFilter) != -1)
                        return true;
                    else
                        return false;
                });

                SortedList<Cliente> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(tableView.comparatorProperty());
                tableView.setItems(sortedData);
            });
        }
            private void switchQuery() throws SQLException {
                switch (cb_selectedIndex){
                    case 0:
                        System.out.println("Buscando nos Pedidos de Entrada");
                        //busca na entrada
                        PedidoFindJsonHelper dBH2 = new PedidoFindJsonHelper(dataInicial, dataFinal, cliente);
                        listaPedidos = FXCollections.observableArrayList(PedidoService.findAllByDateWithStatusFromClient(dBH2, 1));
                        tableViewPedidos.setItems(listaPedidos);
                        break;
                    case 1:
                        System.out.println("Buscando nos Pedidos em Triagem");
                        //busca na triagem
                        PedidoFindJsonHelper dBH3 = new PedidoFindJsonHelper(dataInicial, dataFinal, cliente);
                        listaPedidosTriagem = FXCollections.observableArrayList(PedidoService.findAllByDateWithMoreStatusFromClient(dBH3, Arrays.asList(2,3,4)));
                        tableViewPedidos.setItems(listaPedidosTriagem);
                        break;
                    case 2:
                        System.out.println("Buscando nos Pedidos Finalizados");
                        //busca nos finalizados
                        PedidoFindJsonHelper dBH1 = new PedidoFindJsonHelper(dataInicial, dataFinal, cliente);
                        listaPedidosFinalizados = FXCollections.observableArrayList(PedidoService.findAllByDateWithStatusFromClient(dBH1, 5));
                        tableViewPedidos.setItems(listaPedidosFinalizados);
                        break;
                    case 3:
                        listaPedidosTodos.clear();
                        //busca em todos os pedidos
                        PedidoFindJsonHelper dBH = new PedidoFindJsonHelper(dataInicial, dataFinal, cliente);
                        listaPedidosTodos = FXCollections.observableArrayList(PedidoService.findAllByDateFromClient(dBH));
                                    tableViewPedidos.setItems(listaPedidosTodos);
                                    break;
                }
            }
            private void recuperarClientes() throws SQLException {
                //busca todos os clientes
                listaClientes.clear();
                listaClientes = FXCollections.observableArrayList(ClienteService.findAll());
                tableView.setItems(listaClientes);
            }
            private void alertDialogClientes() {
                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
                dialogLayout.setBody(
                        formularioCliente()
                );
                dialog.show();
            }
            private void alertDialogDetalhes() throws IOException {
                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
                dialogLayout.setBody(
                        tablePedidosCliente()
                );
                dialog.show();
            }

            private void verificarComboBox() {
            cb_selectedIndex = comboBoxFiltroStatus.getSelectionModel().getSelectedIndex();
            System.out.println("Index de busca: " + cb_selectedIndex);
        }

    private void excluirCliente() {
            switch (ClienteService.delete(cliente.getId())){
                case 0 :
                    JFXDialog dialoginner = AlertDialogModel.alertDialogErro("Houve um problema ao atualizar o produto.", stackPane);
                    dialoginner.show();
                    break;
                case 1 :
                    JFXDialog dialoginner2 = AlertDialogModel.alertDialogErro("Nao e possivel excluir um cliente com pedidos.", stackPane);
                    dialoginner2.show();
                    break;
                case 2 :
                    refreshTable();
                    break;
            }
    }
    //metodos de negocios

        //objetos
            public AnchorPane formularioCliente(){
                AnchorPane pane = new AnchorPane();
                VBox vboxPrincipal = defaultVBox();
                JFXButton btnSalvar = defaultButton("SALVAR ALTERAÇÕES");
                JFXButton btnCancelar = defaultButton("CANCELAR");
                JFXTextField edtNome = textFieldPadrao(400);

                JFXTextField edtTelefone = textFieldPadrao(150);
                JFXTextField edtEndereco = textFieldPadrao(550);

                bairros = FXCollections.observableArrayList(BairroService.findAll());
                JFXComboBox<Bairro> cbBairros = new JFXComboBox<>(bairros);
                cbBairros.getSelectionModel().selectFirst();

                HBox row1 = defaultHBox();
                HBox row2 = defaultHBox();
                HBox row3 = defaultHBox();
                HBox row4 = defaultHBox();

                VBox R1C1 = defaultVBox();
                VBox R1C2 = defaultVBox();
                VBox R2C1 = defaultVBox();

                R1C1.getChildren().addAll(
                        defaultText("NOME"),
                        edtNome
                );
                R1C2.getChildren().addAll(
                        defaultText("TELEFONE"),
                        edtTelefone
                );
                R2C1.getChildren().addAll(
                        defaultText("ENDEREÇO"),
                        edtEndereco
                );

                row1.getChildren().addAll(
                        R1C1,
                        R1C2);

                row2.getChildren().addAll(
                        R2C1
                );

                row3.getChildren().addAll(

                );

                row4.getChildren().addAll(
                        btnSalvar,
                        btnCancelar
                );

                row3.setAlignment(Pos.CENTER_RIGHT);

                vboxPrincipal.getChildren().addAll(row1, row2, row3, row4);

                edtNome.setText(cliente.getNome());
                edtTelefone.setText(cliente.getTelefone());
                edtEndereco.setText(cliente.getEndereco());
                btnSalvar.setOnAction((e) -> {
                    Bairro bairro = cbBairros.getValue();
                    Cliente cli = new Cliente(cliente.getId(),
                            edtNome.getText().toUpperCase().trim() ,
                            edtEndereco.getText().toUpperCase().trim() ,
                            bairro,
                            edtTelefone.getText().toUpperCase().trim(),
                            cliente.getData_cadastro());
                    editarCliente(cli);
                });
                btnCancelar.setOnAction((event -> {
                    dialog.close();
                }));
                pane.getChildren().add(vboxPrincipal);
                return pane;
            }

    private void editarCliente(Cliente cli) {
        System.out.println("Iniciando verificação");

        switch (ClienteService.update(cli)){
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
    }

    public AnchorPane tablePedidosCliente(){
                AnchorPane pane = new AnchorPane();
                VBox vboxPrincipal = defaultVBox();
                JFXButton btnSair = defaultButton("SAIR");
                btnSair.setStyle("-fx-background-color: white");
                JFXButton btnPesquisar = defaultButton("BUSCAR");
                btnPesquisar.setPrefHeight(40);
                btnPesquisar.setStyle("-fx-background-color: white");

                HBox row2 = defaultHBox();
                row2.setAlignment(Pos.CENTER_LEFT);
                HBox row3 = defaultHBox();
                row3.getChildren().addAll(
                        btnSair
                );

                row3.setAlignment(Pos.CENTER_RIGHT);

                tableViewPedidos = tableViewPedidos();

                JFXButton btnPrint = DefaultComponents.buttonIcon(" ", "SAVE", 40);
                btnPrint.setPrefHeight(40);
                comboBoxFiltroStatus = new JFXComboBox();
                comboBoxFiltroStatus.setPrefHeight(40);
                datePickerInicial = new JFXDatePicker();
                datePickerInicial.setPrefHeight(40);
                datePickerInicial.setStyle("-fx-background-color: white");
                datePickerInicial.setValue(
                        nowOnDate()
                );
                datePickerInicial.setOnAction((e) -> {
                    dataInicial = onDate(datePickerInicial);
                });

                datePickerInicial.setConverter(new StringConverter<LocalDate>() {
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

                datePickerFinal = new JFXDatePicker();
                datePickerFinal.setPrefHeight(40);
                datePickerFinal.setStyle("-fx-background-color: white");
                datePickerFinal.setPromptText("Data Final");

                datePickerFinal.setValue(
                        nowOnDate()
                );
                datePickerFinal.setOnAction((e) ->{
                    dataFinal = onDate(datePickerFinal);
                });

                datePickerFinal.setConverter(new StringConverter<LocalDate>() {
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

                row2.getChildren().addAll(comboBoxFiltroStatus, datePickerInicial, datePickerFinal, btnPrint, btnPesquisar);
                row2.setStyle("-fx-background-color: navy; -fx-background-radius: 5");
                row2.setPadding(new Insets(10,10,10,10));
                comboBoxFiltroStatus.setPrefWidth(150);
                comboBoxFiltroStatus.getItems().addAll(listaComboBox);
                comboBoxFiltroStatus.setStyle("-fx-background-color: white");


                vboxPrincipal.getChildren().addAll(row2, tableViewPedidos, row3);

                btnSair.setOnAction(e -> dialog.close());

                btnPesquisar.setOnAction((e) -> {
                    try {
                        switchQuery();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });

                btnPrint.setOnAction((e) -> {
                    File file = DefaultComponents.fileChooserSave(stackPane, "DOCUMENTO EXCEL (*.xls)", "*.xls");
                    gerarDocumentoPedidosXLS(file);
                });

                pane.getChildren().add(vboxPrincipal);
                return pane;
            }
            private TableView tableViewPedidos(){
                TableView<OrdemPedido> tableView = new TableView();
                double prefWidth = 650;
                tableView.setPrefWidth(prefWidth);
                TableColumn<OrdemPedido, Long> idCol = new TableColumn<>();
                idCol.setText("ID");
                idCol.setPrefWidth(prefWidth/10);
                idCol.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getId()).asObject());

                TableColumn<OrdemPedido, String> Data1Col = new TableColumn<>();
                Data1Col.setText("DATA ENTRADA");
                Data1Col.setPrefWidth(prefWidth/3.5);
                Data1Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEntradaDateString()));

                TableColumn<OrdemPedido, String> Data2Col = new TableColumn<>();
                Data2Col.setText("H. TRIAGEM");
                Data2Col.setPrefWidth(prefWidth/3.5);
                Data2Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().dateToTimeString(c.getValue().getTriagemHora())));

                TableColumn<OrdemPedido, String> Data3Col = new TableColumn<>();
                Data3Col.setText("H. FINALIZADO");
                Data3Col.setPrefWidth(prefWidth/3.5);
                Data3Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().dateToTimeString(c.getValue().getFinalizadoHora())));
                tableView.getColumns().addAll(idCol, Data1Col, Data2Col, Data3Col);
                ContextMenu cm = new ContextMenu();
                MenuItem mi1 = new MenuItem("ABRIR DETALHES DO PEDIDO");
                cm.getItems().add(mi1);
                tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        if(t.getButton() == MouseButton.SECONDARY) {
                            cm.show(tableView, t.getScreenX(), t.getScreenY());
                            mi1.setOnAction((e) -> {
                                iniciarDetalhesScreen(tableView.getSelectionModel().getSelectedItem());
                                System.out.println("ID do pedido selecionado: " + tableView.getSelectionModel().getSelectedItem().getId());
                            });
                        }
                    }
                });
                return tableView;
            }
            public JFXButton defaultButton(String texto){
                JFXButton button = new JFXButton();
                button.setStyle("-fx-background-color: lightgrey");
                button.setText(texto);
                return button;
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
             private void intentImport() {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/importarClientesScreen.fxml"));
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

             private LocalDate nowOnDate(){
                LocalDate localDate = LocalDate.now();
                localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dataFinal = formatData(localDate.toString());
                dataInicial = formatData(localDate.toString());
                System.out.println("Data Atual: " + dataInicial);
                System.out.println("Data Atual: " + dataFinal);
                return localDate;

            }
            private String onDate (JFXDatePicker picker){
                LocalDate localDate = picker.getValue();
                String dataFormatada = formatData(localDate.toString());
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
            private void refreshTable() {
                listaClientes.clear();
                try {
                    recuperarClientes();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

            }
        //metodos de controle
}
