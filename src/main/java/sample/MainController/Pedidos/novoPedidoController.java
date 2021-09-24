package sample.MainController.Pedidos;

import Services.BairroService;
import Services.ClienteService;
import Services.PedidoService;
import com.jfoenix.controls.*;
import helpers.AlertDialogModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Bairro;
import models.Cliente;
import models.OrdemPedido;
import models.Usuario;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class novoPedidoController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField edtNome;

    @FXML
    private JFXTextField edtEndereco;

    @FXML
    private JFXComboBox<Bairro> cb_Bairro;

    @FXML
    private JFXTextField edtTel;

    @FXML
    private JFXTextField edtFormaPagamento;

    @FXML
    private JFXTextField edtTroco;

    @FXML
    private JFXTextField edtFonte;

    @FXML
    private JFXButton btnSalvar;

    @FXML
    private CheckBox checkBoxManual;

    @FXML
    private JFXDatePicker datePickerEntrada;

    @FXML
    private JFXTimePicker timePickerEntrada;

    @FXML
    private JFXTimePicker timePickerTriagem;

    @FXML
    private JFXTimePicker timePickerCheckout;

    @FXML
    private JFXTimePicker timePickerSaida;

    @FXML
    private JFXTimePicker timePickerFinalizado;

    @FXML
    private JFXTextField edtCaixa;

    @FXML
    private JFXTextField edtEntregador;


    @FXML
    private HBox hboxManual;


    JFXTextField edtSearch;
    JFXDialog dialog;
    BorderPane border;
    TableView tableView;

    @FXML
    private JFXButton btnCancelar;

    int selectedIndex;
    int cliente_id = 0;

    ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    ObservableList<Bairro> bairros = FXCollections.observableArrayList();
    FilteredList<Cliente> filteredData;

    Cliente selected_Cliente;

    OrdemPedido pedidoAtual = new OrdemPedido();
    double troco;
    String dataAtual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //recuperar usuario logado

        recuperarClientes();
        recuperarBairros();

        setupComponentes();
    }



    //Metodos Iniciais
    private void recuperarClientes() {
        clientes.clear();
        clientes = FXCollections.observableArrayList(ClienteService.findAll());
        //recuperar todos os clientes
    }

    private void recuperarBairros() {
        bairros.clear();
        bairros = FXCollections.observableArrayList(BairroService.findAll());
        cb_Bairro.getItems().addAll(bairros);
    }

    private void setupComponentes() {
        edtNome.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        setupEdtCliente();
        setupEdtFormaPag();
        setupEdtOrigem();

        checkBoxManual.setOnMouseClicked((e) -> {
            if(checkBoxManual.isSelected()){
                hboxManual.setVisible(true);
            }else{
                hboxManual.setVisible(false);
            }
        });
        edtEndereco.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtTel.requestFocus();
                    break;
            }
        });
        edtEndereco.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        edtTel.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtFormaPagamento.requestFocus();
                    break;

            }
        });
        edtTel.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        edtFormaPagamento.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        edtFormaPagamento.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtTroco.requestFocus();
                    break;
            }
        });

        edtTroco.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtFonte.requestFocus();
                    break;
            }
        });
        edtTroco.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        edtFonte.setTextFormatter(new TextFormatter<Object>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        edtFonte.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    if(checkBoxManual.isSelected()){
                        timePickerEntrada.requestFocus();
                    }else{
                        btnSalvar.requestFocus();
                    }
                    break;
            }
        });
        datePickerEntrada.setValue(nowOnDate());
        timePickerEntrada.setValue(nowOnTime());
        timePickerEntrada.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    timePickerCheckout.requestFocus();
                    break;
            }
        });

        timePickerCheckout.setValue(nowOnTime());
        timePickerCheckout.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    timePickerTriagem.requestFocus();
                    break;
            }
        });

        timePickerTriagem.setValue(nowOnTime());
        timePickerTriagem.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    timePickerSaida.requestFocus();
                    break;
            }
        });

        timePickerSaida.setValue(nowOnTime());
        timePickerSaida.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    timePickerFinalizado.requestFocus();
                    break;
            }
        });

        timePickerFinalizado.setValue(nowOnTime());
        timePickerFinalizado.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtCaixa.requestFocus();
                    break;
            }
        });

        edtCaixa.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    edtEntregador.requestFocus();
                    break;
            }
        });
        edtEntregador.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    btnSalvar.requestFocus();
                    break;
            }
        });
        btnCancelar.setOnAction((e) -> fecharJanela());

        btnSalvar.setOnKeyPressed((e) -> {
            switch (e.getCode()){
                case ENTER:
                    verificarClienteExistente(edtNome.getText());
                    pedidoAtual.setCliente(selected_Cliente);
                    try {
                        salvarPedido();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        });
        btnSalvar.setOnAction((e) -> {
            verificarClienteExistente(edtNome.getText());
            pedidoAtual.setCliente(selected_Cliente);
            try {
                salvarPedido();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });

    }


    private void setupEdtCliente() {
        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i<clientes.size(); i++){
            nomes.add(clientes.get(i).getNome());
        }

        JFXAutoCompletePopup<Cliente> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setPrefWidth(500);
        autoCompletePopup.getSuggestions().addAll(clientes);

        autoCompletePopup.setSelectionHandler(event -> {
            edtNome.setText(event.getObject().getNome());
            edtEndereco.requestFocus();
            selected_Cliente = event.getObject();
            setClienteDetalhes(event.getObject());
            // you can do other actions here when text completed
        });

        // filtering options
        edtNome.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.getNome().toLowerCase().contains(edtNome.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || edtNome.getText().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(edtNome);
            }
        });


    }
    private void setupEdtFormaPag() {
        ArrayList<String> nomes = new ArrayList<>();
        nomes.add("DINHEIRO");
        nomes.add("CREDITO");
        nomes.add("DEBITO");
        nomes.add("A PRAZO");

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setPrefWidth(90);
        autoCompletePopup.getSuggestions().addAll(nomes);

        autoCompletePopup.setSelectionHandler(event -> {
            edtFormaPagamento.setText(event.getObject());
            edtTroco.requestFocus();
            //recuperarClienteSelecionado(event.getObject());
            // you can do other actions here when text completed
        });

        // filtering options
        edtFormaPagamento.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(edtFormaPagamento.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || edtFormaPagamento.getText().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(edtFormaPagamento);
            }
        });
    }
    private void setupEdtOrigem() {
        ArrayList<String> nomes = new ArrayList<>();
        nomes.add("LIGACAO");
        nomes.add("WHATSAPP");
        nomes.add("OUTROS");


        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setPrefWidth(90);
        autoCompletePopup.getSuggestions().addAll(nomes);

        autoCompletePopup.setSelectionHandler(event -> {
            edtFonte.setText(event.getObject());
            //recuperarClienteSelecionado(event.getObject());
            // you can do other actions here when text completed
        });

        // filtering options
        edtFonte.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(edtFonte.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || edtFonte.getText().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(edtFonte);
            }
        });
    }





    private void setClienteDetalhes(Cliente cli) {
        //recuperar cliente selecionado
        edtTel.setText(cli.getTelefone());
        edtEndereco.setText(cli.getEndereco());
        edtEndereco.requestFocus();
        //System.out.println("Bairro recuperado: ID:" + bairro.getId() + "Nome: " + bairro.getNome());
        cb_Bairro.getSelectionModel().select(cli.getBairro().getId().intValue() - 1);
    }
    //Metodos Iniciais

    //Metodos de Negocios
    private void pesquisarCliente(){
        filteredData = new FilteredList<>(clientes, b -> true);
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
    private void salvarPedido() throws ParseException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date horario = new Date(System.currentTimeMillis());

        pedidoAtual.setForma_pagamento(edtFormaPagamento.getText());
        pedidoAtual.setFonte_pedido(edtFonte.getText());
        pedidoAtual.setEntradaDate(sdf.parse(sdf.format(date)));
        pedidoAtual.setEntradaHora(horario);
        pedidoAtual.setTriagemHora(horario);
        pedidoAtual.setCheckoutHora(horario);
        pedidoAtual.setFinalizadoHora(horario);
        pedidoAtual.setOperador(new Usuario(5L,"daniel",1));
        pedidoAtual.setStatus(1);

        if (checkBoxManual.isSelected() == false) {
            if(pedidoAtual.getForma_pagamento().isEmpty() ||  pedidoAtual.getFonte_pedido().isEmpty()){
                alertDialogErro("Preencha todos os Campos!");
            }else{
                verificarCheckboxManual();
            }
        }else{
            if(pedidoAtual.getForma_pagamento().isEmpty() ||  pedidoAtual.getFonte_pedido().isEmpty()
            || timePickerEntrada.getPromptText().isEmpty()|| timePickerTriagem.getPromptText().isEmpty() || timePickerCheckout.getPromptText().isEmpty()
            || timePickerSaida.getPromptText().isEmpty() || timePickerFinalizado.getPromptText().isEmpty() || edtCaixa.getText().isEmpty()
                    || edtEntregador.getText().isEmpty()){
                alertDialogErro("Preencha todos os Campos!");
            }else if((selected_Cliente.getNome().length() > 155 || selected_Cliente.getEndereco().length() > 500 || selected_Cliente.getTelefone().length() > 55 )){
                alertDialogErro("Os campos excedem o tamanho de maximo de caracteres.");
            }else{
                verificarCheckboxManual();
            }
        }


    }
    private void verificarCheckboxManual (){
        if (checkBoxManual.isSelected()) {
            //insertPedidoManual();
        }else{
            insertPedido();
        }
    }
    private void insertPedidoManual() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date horario = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String horario_entrada = format.format(horario);
        //data_entrada = dateFormat.format(date);
        LocalDate data = datePickerEntrada.getValue();
        String dataInformada = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String hEntrada = timePickerEntrada.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String hTriagem = timePickerTriagem.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String hCheckout = timePickerCheckout.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String hSaida = timePickerSaida.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String hFinalizado = timePickerFinalizado.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        //Inserir pedido

        if(true){
            //inserir estatistica
            //PedidoEstatistica estatistica = createEstatistica(pedido.getId(), pedido.getHorario_triagem(), pedido.getHorario_checkout(), pedido.getHorario_finalizado(), pedido.getHorario_entrada());
            //boolean statistics = db_crud.metodoInsertEstatistica(estatistica,pedido.getData_entrada(), query);
            if(true){
                restartAdd();
                System.out.println("Sucesso ao gerar estatistica do pedido");
            }else{
                System.out.println("Houve um problema ao gerar estatistica do pedido");
            }
        }else{
            JFXDialog dialog = AlertDialogModel.alertDialogErro("Houve um problema ao tentar incluir pedido", stackPane);
            dialog.show();
        }
    }
    private void insertPedido() {
        //data_entrada = dateFormat.format(date);
        System.out.println("PId: " + pedidoAtual.getId());
        System.out.println("CId: " + pedidoAtual.getCliente().getId());
        System.out.println("CNome: " + pedidoAtual.getCliente().getNome());
        System.out.println("Pag: " + pedidoAtual.getForma_pagamento());
        System.out.println("HR: " + pedidoAtual.getEntradaHora());
        System.out.println("DT: " + pedidoAtual.getEntradaDate());
        System.out.println("OP: " + pedidoAtual.getOperador());
        //inserir pedido

        OrdemPedido newPed = PedidoService.insert(pedidoAtual);
        System.out.println(newPed);
        if(newPed != null){
            //Adicionar Contagem ao cliente
            //boolean state2 = db_crud.metodoClienteAddPedido(clienteAtual.getId(), clienteAtual.getQtdPedidos());
            fecharJanela();
        }else{
            JFXDialog dialog = AlertDialogModel.alertDialogErro("Houve um problema ao tentar incluir pedido", stackPane);
            dialog.show();
        }
    }
    private Cliente verificarClienteExistente(String nome) {
        //verificar cliente existente
        List<Cliente> clientes = ClienteService.getByNome(nome);
        if(clientes.size() <= 0){
            JFXButton buttonConfirmar = new JFXButton("OK");
            JFXDialog dialog = AlertDialogModel.alertDialogAction("Cliente nao existente, deseja criar um novo?",stackPane, buttonConfirmar);
            buttonConfirmar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Criando cliente");
                    Cliente newCliente = new Cliente(null, edtNome.getText(),edtEndereco.getText(),cb_Bairro.getValue(),edtTel.getText(),null);
                    criarCliente(newCliente);
                    dialog.close();
                }
            });
            dialog.show();
        }else{
            System.out.println("Cliente ja existe");
            selected_Cliente = clientes.get(0);
            return clientes.get(0);
        }
        return null;
    }
    private void criarCliente(Cliente cli){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
        Date data = new Date();
        String data_cadastro = format.format(data);
        if(cli.getNome().length() < 150 && cli.getEndereco().length() < 150 && cli.getTelefone().length() < 150){
            if(!(cli.getNome().equals("") && !(cli.getEndereco().equals("")) && !(cli.getTelefone().equals("")))){
                //inserir pedido
                cli.setData_cadastro(data_cadastro);
                Cliente newCli = ClienteService.insert(cli);
                if(!(newCli.getNome().equals("") && newCli.getEndereco().equals("")
                        && newCli.getTelefone().equals("") && newCli.getData_cadastro().equals(""))){
                    System.out.println("Cliente Criado com Sucesso");
                    recuperarClientes();
                    selected_Cliente = newCli;
                    alertDialogErro("Cliente Criado com sucesso!");
                }else{
                    System.out.println("Houve um erro");
                    alertDialogErro("Houve um problema ao criar novo Cliente");
                }
            }else{
                System.out.println("Preencha todos os Campos");
            }
        }else{
            System.out.println("Os campos devem ter menos de 55 caracteres");
        }

    }
    //Metodos de Negocios

    //Objetos

    private void alertDialogClientes() {
        selectedIndex = 0;
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton buttonCancelar = new JFXButton("Cancelar");
        JFXButton buttonConfirmar = new JFXButton("Confirmar");
        dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        buttonCancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) ->{
            dialog.close();
        });
        buttonConfirmar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if(selectedIndex > 0){
                setClienteEDT(selected_Cliente);
                dialog.close();
            }else{
                System.out.println("Selecione um cliente");
            }
        });
        dialogLayout.setBody(
                borderPane()
        );
        dialogLayout.setActions(buttonConfirmar,buttonCancelar);
        dialog.show();
        tableView.setItems(clientes);
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cliente>() {
            @Override
            public void changed(ObservableValue<? extends Cliente> observable, Cliente oldValue, Cliente newValue) {
                if(tableView.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
                    Cliente cliente = (Cliente) tableView.getSelectionModel().getSelectedItem();
                    selectedIndex = Math.toIntExact(cliente.getId());
                    selected_Cliente = cliente;
                    System.out.println(selectedIndex);
                }
            }
        });
    }
    public BorderPane borderPane(){
        border = new BorderPane();
        edtSearch = new JFXTextField();
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

        edtSearch.setStyle("-fx-background-color: #d3d3d3");
        border.setTop(edtSearch);
        border.setCenter(tableView());
        return border;
    }
    public TableView tableView(){
        tableView = new TableView<Cliente>();
        TableColumn nomeCol = new TableColumn<Cliente, String>();
        nomeCol.setSortType(TableColumn.SortType.ASCENDING);
        nomeCol.setText("NOME");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn telCol = new TableColumn<Cliente, String>();
        telCol.setText("TELEFONE");
        telCol.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        TableColumn endCol = new TableColumn<Cliente, String>();
        endCol.setText("ENDEREÇO");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tableView.getColumns().addAll(nomeCol, endCol, telCol);
        tableView.getSortOrder().add(nomeCol);
        return tableView;
    }
    private void alertDialogErro(String erro) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton buttonCancelar = new JFXButton("OK");
        JFXDialog dialogErro = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        Text texto = new Text();
        texto.setText(erro);
        texto.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));
        buttonCancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) ->{
            dialogErro.close();
        });
        dialogLayout.setBody(texto);
        dialogLayout.setActions(buttonCancelar);
        dialogErro.show();
    }
    //Objetos

    //Metodos de Controle
    private void restartAdd() {
        edtEntregador.clear();
        edtCaixa.clear();
        edtEndereco.clear();
        edtNome.clear();
        edtTel.clear();
        edtFonte.clear();
        edtTroco.clear();
        edtFormaPagamento.clear();
        edtNome.requestFocus();
    }
    private void setClienteEDT(Cliente cliente){
        edtNome.setText(cliente.getNome());
        edtEndereco.setText(cliente.getEndereco());
        edtTel.setText(cliente.getTelefone());
        cliente_id = Math.toIntExact(cliente.getId());
    }
    private void fecharJanela() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    private LocalDate nowOnDate(){
        LocalDate localDate = LocalDate.now();
        localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dataAtual = formatData(localDate.toString());
        System.out.println("Data Atual: " + dataAtual);
        return localDate;

    }
    private LocalTime nowOnTime(){
        LocalTime localTime = LocalTime.now();
        localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return localTime;

    }
    public String formatData (String data){
        SimpleDateFormat sdf = null;
        Date d = null;
        try{
            sdf = new SimpleDateFormat("yy-MM-dd");
            d = sdf.parse(data);
            sdf.applyPattern("yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(d);
    }
    //Metodos de Controle
}

