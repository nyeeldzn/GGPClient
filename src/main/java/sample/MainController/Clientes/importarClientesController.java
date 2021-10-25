package sample.MainController.Clientes;

import Services.ClienteService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import helpers.AlertDialogModel;
import helpers.DefaultComponents;
import helpers.LoadingPane;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import models.Bairro;
import models.Cliente;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class importarClientesController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btnImport;

    @FXML
    private JFXButton btnQuit;

    @FXML
    private JFXButton btnSelect;

    @FXML
    private Text textNome;
    List<Cliente> listaClientes = new ArrayList<>();

    Thread t1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnImport.setVisible(false);
        Thread t = Thread.currentThread();
        System.out.println( "Thread Inicial: " + t.getName());
        setupComponents();
    }

    private void setupComponents() {
        btnQuit.setOnAction((e) -> {
            quitScreen();
        });
        btnImport.setOnAction((e) -> {
                if(listaClientes.size() > 0){
                    metodoInsertListaClientes(listaClientes);
                }else{
                    JFXDialog dialog = AlertDialogModel.alertDialogErro("Selecione uma planilha compativel primeiro", stackPane);
                    dialog.show();
                }
        });
        btnSelect.setOnAction((e) -> {
            metodoSelecao();
        });
    }
    //metodos de negocios
    private void metodoSelecao() {
        File file = DefaultComponents.fileChooserSelect(stackPane, "DOCUMENTO DO EXCEL (*.xls)", "*.xls");
        textNome.setText(file.getName());

        try {
            listaClientes = metodoImport(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        if(listaClientes.size() > 0){
            btnImport.setVisible(true);
            textNome.setText("Sucesso, clique em importar para enviar para o Banco de Dados");
        }
    }

    private List<Cliente> metodoImport(File file) throws IOException, BiffException {
        List<Cliente> listaTemporaria = new ArrayList<>();
        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet(0);
        int linhas = sheet.getRows();

        System.out.println("Iniando leitura da planilha: " + file.getName());
        System.out.println("Planilha com: " + linhas + " linhas");

        for(int i = 0; i<linhas; i++){
            Cell cellNome = sheet.getCell(0, i);
            Cell cellEndereco = sheet.getCell(4, i);
            Cell cellNumero = sheet.getCell(5, i);
            Cell cellBairro = sheet.getCell(6, i);
            String enderecoCompleto = cellEndereco.getContents() + "," + cellNumero.getContents() ;
            listaTemporaria.add(new Cliente(0L, cellNome.getContents(), enderecoCompleto, new Bairro(0L, cellBairro.getContents()), "Usuario Importado", "sem data"));
            System.out.println("Cliente: " + listaTemporaria.get(i).getNome() + " " + listaTemporaria.get(i).getTelefone() + " " + listaTemporaria.get(i).getEndereco() + " " + listaTemporaria.get(i).getData_cadastro());
        }

        workbook.close();
        return listaTemporaria;
    }

    private boolean metodoInsertListaClientes(List<Cliente> cliente) {
        boolean state = false;

        new Service<List<Cliente>>(){
            JFXDialog loading = LoadingPane.SimpleLoading(stackPane);
            @Override
            public void start() {
                loading.show();
                super.start();
            }

            @Override
            protected Task<List<Cliente>> createTask() {
                return new Task<List<Cliente>>() {
                    @Override
                    protected List<Cliente> call() throws Exception {
                        List<Cliente> list = ClienteService.insertList(listaClientes);
                        return list;
                    }
                };
            }

            @Override
            protected void succeeded() {
                if(getValue().size() == listaClientes.size() ){
                    loading.close();
                    quitScreen();
                }
                super.succeeded();
            }
        }.start();

        return state;
    }

    //metodos de negocios

    //metodos de controle
    private void quitScreen() {
        Stage stage;
        stage = (Stage) stackPane.getScene().getWindow();
        stage.close();
    }
    //metodos de controle
}
