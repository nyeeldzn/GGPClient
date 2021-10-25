package sample.MainController.Produtos;

import Services.ProdutoService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import helpers.DefaultComponents;
import helpers.LoadingPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import models.Produto;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class importarProdutoController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton btnQuit;
    @FXML
    private JFXButton btnImport;
    @FXML
    private JFXButton btnSelect;
    @FXML
    private Text textNome;

    ObservableList<String> listaProdutos = FXCollections.observableArrayList();
    Connection connection = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupComponents();

    }
    //metodos iniciais
    private void setupComponents() {
        btnSelect.setOnAction((e) -> {
            metodoSelecao();
        });
        btnImport.setOnAction((e) -> {
            metodoInsertListaProdutos();
        });
    }

    private boolean metodoInsertListaProdutos() {
        boolean state = false;

        new Service<Produto>(){
            String item = "";
            JFXDialog loading = LoadingPane.ItemsLoading(item, stackPane);
            @Override
            public void start() {
                loading.show();
                super.start();
            }

            @Override
            protected Task<Produto> createTask() {
                return new Task<Produto>() {
                    @Override
                    protected Produto call() throws Exception {
                        for (int i = 0; i < listaProdutos.size(); i++) {
                            item = "Adicionando produto: " + listaProdutos.get(i) + "  " + "(" + i + "/" + listaProdutos.size() + ")";
                            if(insert(listaProdutos.get(i)) != null){
                                System.out.println("Produto: " + listaProdutos.get(i) + " Adicionado com sucesso!");
                            }else {
                                System.out.println("Houve um problema ao adicionar o produto: " + listaProdutos.get(i));
                            }
                        }
                        return null;
                    }
                };
            }

            private Produto insert (String nome){
                return ProdutoService.insert(new Produto(null, nome));
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loading.close();
            }
        }.start();

        return state;
    }

    //metodos iniciais
    //metodos de negocio
    private void metodoSelecao() {
        File file = DefaultComponents.fileChooserSelect(stackPane, "DOCUMENTO DO EXCEL (*.xls)", "*.xls");
        textNome.setText(file.getName());

        try {
            listaProdutos = metodoImport(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        if(listaProdutos.size() > 0){
            textNome.setText("Sucesso, clique em importar para enviar para o Banco de Dados");
        }

    }

    private ObservableList<String> metodoImport(File file) throws IOException, BiffException {
        ObservableList<String> listaProdutosImportados = FXCollections.observableArrayList();
        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet(0);
        int linhas = sheet.getRows();

        System.out.println("Iniando leitura da planilha: " + file.getName());
        System.out.println("Planilha com: " + linhas + " linhas");

        for(int i = 0; i<linhas; i++){
            Cell cell = sheet.getCell(3, i);
            listaProdutosImportados.add(cell.getContents());
            System.out.println("Produto: " + listaProdutosImportados.get(i));
        }
        workbook.close();
        return listaProdutosImportados;
    }
    //metodos de negocio
}
