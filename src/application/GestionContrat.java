package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;

public class GestionContrat {

	@FXML
	private TextField codeContrat;
	
	@FXML
	private TextField rechercher;
	
	@FXML
	private DatePicker dateContrat;
	
	@FXML
	private DatePicker dateEcheance;
	
	
	
	public void exitButton() {
		Main.stage.close();
	}
	
	
	public void retour() throws IOException {
		Parent root;
		if(Main.ad) {
			root = FXMLLoader.load(getClass().getResource(("GUI/AdminPanel.fxml")));
		}
		else {
			root = FXMLLoader.load(getClass().getResource(("GUI/UserPanel.fxml")));
		}
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();

	}
	
 
	
	public void interfaceAjouterContrat(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/AjouterContrat.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void ajouterContrat(ActionEvent e) throws IOException, SQLException {
		if( !codeContrat.getText().equals("") &&  !dateContrat.equals(null) && !dateEcheance.equals(null)  ) 
		{
			String cSql = "SELECT * FROM `contrat` WHERE codeContrat='"+codeContrat.getText()+"';";
			Connection cC = Login.connectDB();
			PreparedStatement cPS = cC.prepareStatement(cSql);
			ResultSet contratResult = cPS.executeQuery();
			if(contratResult.next()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un utilisateur avec le même CIN");
				alert.show();
			} 
			else {
				 
				Contrat contrat = new Contrat(codeContrat.getText(), dateContrat.getValue(), dateEcheance.getValue());
				String sql = "INSERT INTO `contrat`(`codeContrat`, `dateContrat`, `dateEcheance`) VALUES (?,?,?)";
				Connection C = Login.connectDB();
				PreparedStatement ps = C.prepareStatement(sql);
				ps.setString(1, contrat.getCodeContrat());
				ps.setObject(2, contrat.getDateContrat());
				ps.setObject(3, contrat.getDateEcheance());
				ps.executeUpdate();
				C.close();
				retourGestion();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Succès");
				alert.setHeaderText("Les données ont été enregistrées");
				alert.show();
			}
		}
		
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Veuillez remplir tous les champs");
			alert.show();
		}
	}
	
	public void interfaceModifierUtilisateur(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ModifierContrat.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void rechercherContrat(ActionEvent e) throws SQLException {
		String rCONTRAT = rechercher.getText();
		String sql = "SELECT * FROM `contrat` WHERE codeContrat='"+rCONTRAT+"';";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ResultSet result = ps.executeQuery();
		if(result.next()) {
			codeContrat.setText(result.getString(1));
			Date sqlDateContrat=result.getDate(2);
			dateContrat.setValue(sqlDateContrat.toLocalDate());
			Date sqlDateEcheance = result.getDate(3);
			dateEcheance.setValue(sqlDateEcheance.toLocalDate());
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Il n'y a aucun utilisateur avec ce CIN");
			alert.show();			
		}
		C.close();
	}
	
	public void modifierContrat(ActionEvent e) throws IOException, SQLException {
		if(!codeContrat.getText().equals("") && !dateContrat.equals(null) && !dateEcheance.equals(null)) {
			
			Contrat contrat = new Contrat(codeContrat.getText(),dateContrat.getValue(),dateEcheance.getValue());
			String sql = "SELECT `codeContrat` FROM `contrat` WHERE codeContrat='"+contrat.getCodeContrat()+"';";
			Connection C = Login.connectDB();
			PreparedStatement ps = C.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			if(result.next() && !contrat.getCodeContrat().equalsIgnoreCase(rechercher.getText())) {	
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un utilisateur avec le même CIN");
				alert.show();
			}
			else {
				String sql2 = "UPDATE `contrat` SET `codeContrat`=?, `dateContrat`=?, `dateEcheance`=?  WHERE codeContrat=?;";
				Connection C2 = Login.connectDB();
				PreparedStatement ps2 = C2.prepareStatement(sql2);
				ps2.setString(1, contrat.getCodeContrat());
				ps2.setObject(2, contrat.getDateContrat());
				ps2.setObject(3, contrat.getDateEcheance());
				ps2.setString(4, rechercher.getText());
				ps2.executeUpdate();
				retourGestion();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Succès");
				alert.setHeaderText("Les données ont été enregistrées");
				alert.show();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Veuillez remplir tous les champs");
			alert.show();
		}
	}
	
	public void supprimerContrat(ActionEvent e) throws SQLException, IOException {
		String sql = "DELETE FROM `contrat` WHERE codeContrat=?;";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ps.setString(1, rechercher.getText());
		ps.executeUpdate();
		C.close();
		retourGestion();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Succès");
		alert.setHeaderText("L'utilisateur a été supprimé");
		alert.show();
	}
	public void retourGestion() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/GestionContrat.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void retourMenu() throws IOException {
		Parent root;
		if(Main.ad) {
			root = FXMLLoader.load(getClass().getResource(("GUI/AdminPanel.fxml")));
		}
		else {
			root = FXMLLoader.load(getClass().getResource(("GUI/UserPanel.fxml")));
		}
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();

	}
}
