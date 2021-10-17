package helpers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;

public class LoadingPane {

    private static ImageView imageview;

    public static JFXDialog SimpleLoading(StackPane stackPane) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialogErro = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);

        Image i = new Image(new File("src/main/resources/loading-buffering.gif").toURI().toString());
        imageview = new ImageView();
        imageview.setImage(i);
        imageview.setFitHeight(45);
        imageview.setFitWidth(45);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(imageview);
        Text text = new Text();
        text.setText("PROCESSANDO");
        text.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 10));

        dialogLayout.setHeading(text);
        dialogLayout.setBody(vbox);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setMaxSize(95,95);
        dialogLayout.setMinSize(95,95);

        return dialogErro;
    }

    public static JFXDialog ItemsLoading(String item ,StackPane stackPane) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialogErro = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);

        Image i = new Image(new File("src/main/resources/loading-buffering.gif").toURI().toString());
        imageview = new ImageView();
        imageview.setImage(i);
        imageview.setFitHeight(45);
        imageview.setFitWidth(45);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(imageview);
        Text text = new Text();
        text.setText("PROCESSANDO" + "\n" + item);
        text.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 10));

        dialogLayout.setHeading(text);
        dialogLayout.setBody(vbox);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setMaxSize(95,95);
        dialogLayout.setMinSize(95,95);

        return dialogErro;
    }


}
