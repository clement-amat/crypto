package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	/** Espace de pr�sentation des sc�nes */
    private Stage theatre;

    /** Sc�ne principale de la fenetre */
    private Scene principale;

    /** fen�tre principale */
    private BorderPane racine;

    /** chemin vers le fichier fxml */
    private final String CHEMIN_INTERFACE = "vue/FenetrePrincipale.fxml";

	@Override
	public void start(Stage primaryStage) {
        theatre = primaryStage;
        racine = new BorderPane();
        // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        FXMLLoader chargeur = new FXMLLoader(getClass().getResource(CHEMIN_INTERFACE));
        try {
            racine = chargeur.load();
            principale = new Scene(racine,400, 550);
            theatre.setScene(principale);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier");
            System.out.println(e.getMessage());
        }
        theatre.setTitle("Crypto");
        theatre.setResizable(false);
        theatre.show();
    }

	public static void main(String[] args) {
		launch(args);
	}
}
