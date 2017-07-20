package application.controller;

import application.model.ChiffrementVigenere;
import application.model.CodeCesar;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

	/*
	 * 
	 * PARTIE CHIFFRE DE CESAR
	 * 
	 */

	/* partie chiffrement */
	@FXML
	private TextArea tb_textechiffre;

	@FXML
	private TextArea tb_achiffrer;

	@FXML
	private TextField cle;

	/* partie d�chiffrement */
	@FXML
	private TextArea tb_adechiffrer;

	@FXML
	private RadioButton rb_codeconnu;

	@FXML
	private RadioButton rb_toutesSolutions;

	@FXML
	private RadioButton rb_devinerSolution;

	@FXML
	private TextField tb_cleconnu;

	@FXML
	private TextArea tb_dechiffre;

	@FXML
	public boolean chiffrer() {
		int cleInt;
		CodeCesar chiffrement;
		// recuperer le texte
		String aChiffrer = tb_achiffrer.getText();
		// recuperer la cl�
		try {
			cleInt = Integer.parseInt(cle.getText());
			chiffrement = new CodeCesar(cleInt, aChiffrer, false);
			tb_textechiffre.setText(chiffrement.getEncode());
		} catch (NumberFormatException error) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("La clé de chiffrement doit être un nombre entier");
			alert.setTitle("Erreur de clé");
			alert.show();
			return false;
		}

		return true;
	}

	@FXML
	public boolean dechiffrer() {
		int cleChiffrement;
		String aDechiffrer;

		if (rb_toutesSolutions.isSelected()) {
			// methode de brute force
			String[] solutions;
			aDechiffrer = tb_adechiffrer.getText();
			solutions = CodeCesar.bruteForce(aDechiffrer);
			tb_dechiffre.setText("Solutions possibles :\n");
			for (int i = 0; i < solutions.length; i++) {
				tb_dechiffre.appendText("Cle " + i + " : " + solutions[i] + '\n');
			}
		} else if (rb_codeconnu.isSelected()) {
			// recuperer la cl�
			try {
				cleChiffrement = Integer.parseInt(tb_cleconnu.getText());
			} catch (NumberFormatException error) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("La clé de chiffrement doit être un nombre entier");
				alert.setTitle("Erreur de clé");
				alert.show();
				return false;
			}

			// traduire le mot saisi
			aDechiffrer = tb_adechiffrer.getText();
			CodeCesar dechiffrement = new CodeCesar(cleChiffrement, aDechiffrer, true);
			tb_dechiffre.setText(dechiffrement.getOriginal());
		} else { // par défaut : le système doit deviner la clé
			int cle = CodeCesar.rechercheCle(tb_adechiffrer.getText());
			CodeCesar dechiffrement = new CodeCesar(cle, tb_adechiffrer.getText(), true);
			tb_cleconnu.setText(cle + "");
			tb_dechiffre.setText(dechiffrement.getOriginal());
		}

		return false;
	}

	/*
	 *
	 * PARTIE VIGENERE
	 * 
	 */
	@FXML
	private TextArea tb_vig_original;

	@FXML
	private TextArea tb_vig_cleChiffrement;

	@FXML
	private TextArea tb_vig_chiffre;

	@FXML
	private TextArea tb_vig_adechiffrer;

	@FXML
	private TextArea ta_vig_resultatDechiffre;

	@FXML
	private TextField tf_vig_lgCle;

	@FXML
	private TextField tf_vig_cle;

	@FXML
	private RadioButton rb_vig_inconnue;

	@FXML
	private RadioButton rb_vig_lgConnue;

	@FXML
	private RadioButton rb_vig_cleConnue;

	public void chiffrerVigenere() {

		ChiffrementVigenere vigenere = new ChiffrementVigenere(tb_vig_cleChiffrement.getText(),
				tb_vig_original.getText(), false);
		tb_vig_chiffre.setText(vigenere.getEncode());
	}

	public void dechiffrerVigenere() {
		ChiffrementVigenere chiffrement;

		if (rb_vig_inconnue.isSelected()) {
			String cle = ChiffrementVigenere.chercherCleVigenere(tb_vig_adechiffrer.getText());
			tf_vig_cle.setText(cle);
			tf_vig_lgCle.setText(cle.length() + "");

			chiffrement = new ChiffrementVigenere(cle, tb_vig_adechiffrer.getText(), true);
			ta_vig_resultatDechiffre.setText(chiffrement.getOriginal());

		} else if (rb_vig_lgConnue.isSelected()) {
			try {
				int lgCle = Integer.parseInt(tf_vig_lgCle.getText());
				String cle = ChiffrementVigenere.chercherCleVigenere(tb_vig_adechiffrer.getText(), lgCle);
				tf_vig_cle.setText(cle);

				chiffrement = new ChiffrementVigenere(cle, tb_vig_adechiffrer.getText(), true);
				ta_vig_resultatDechiffre.setText(chiffrement.getOriginal());
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("La clé de chiffrement doit être un nombre entier");
				alert.setTitle("Erreur de clé");
				alert.show();
			}
		} else {
			chiffrement = new ChiffrementVigenere(tf_vig_cle.getText(), tb_vig_adechiffrer.getText(), true);
			ta_vig_resultatDechiffre.setText(chiffrement.getOriginal());
		}
	}
}
