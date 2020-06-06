package application;

import java.io.IOException;
import java.sql.Connection;
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

public class GestionParking {
	int rNumPar; 
	@FXML
	private TextField nParking;
	@FXML
	private TextField nomParking;
	@FXML
	private TextField capaciteParking;
	@FXML
	private TextField rue;
	@FXML
	private TextField arrondissement;
	@FXML
	private TextField rechercher;
	
	
		
		
	public void exitButton() {
		Main.stage.close();
	}
	
	public void interfaceAjouterParking(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/AjouterParking.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void ajouterParking(ActionEvent e) throws IOException, SQLException {
		if(!nParking.getText().equals("") && !nomParking.getText().equals("") && !capaciteParking.getText().equals("") 
				&& !rue.getText().equals("")  && !arrondissement.getText().equals("") ) 
		{
			String pSql = "SELECT * FROM `parking` WHERE nParking='"+nParking.getText()+"';";
			Connection pC = Login.connectDB();
			PreparedStatement pPS = pC.prepareStatement(pSql);
			ResultSet pResult = pPS.executeQuery();
			if(pResult.next()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un parking avec le même N° Parking");
				alert.show();
			} 
			else {
				
				int nPar = Integer.parseInt(nParking.getText());
				int capacitePar = Integer.parseInt(capaciteParking.getText());
				
				Parking parking = new Parking(nPar, nomParking.getText(), capacitePar, rue.getText(), arrondissement.getText());
				String sql = "INSERT INTO `parking`(`nParking`, `nom`, `capacite`, `rue`, `arrondissement`) VALUES (?,?,?,?,?)";
				Connection C = Login.connectDB();
				PreparedStatement ps = C.prepareStatement(sql);
				ps.setInt(1, parking.getnParking());
				ps.setString(2, parking.getNom());
				ps.setInt(3, parking.getCapacite());
				ps.setString(4, parking.getRue());
				ps.setString(5, parking.getArrondissement());				
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
	
	public void interfaceModifierParking(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ModifierParking.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}

	public void rechercherParking() throws SQLException {
		rNumPar = Integer.parseInt(rechercher.getText());
		String sql = "SELECT * FROM parking WHERE nParking='"+ rNumPar +"';";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ResultSet result = ps.executeQuery();
		if(result.next()) {
			nParking.setText(result.getString(1));
			nomParking.setText(result.getString(2));
			capaciteParking.setText(result.getString(3));
			rue.setText(result.getString(4));
			arrondissement.setText(result.getString(5));
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Il n'y a aucun Parking avec ce N° Parking");
			alert.show();
		}
		C.close();
	}
	
	public void modifierParking(ActionEvent e) throws IOException, SQLException {
		if(!nParking.getText().equals("") && !nomParking.getText().equals("") && !capaciteParking.getText().equals("") 
				&& !rue.getText().equals("")  && !arrondissement.getText().equals("") )
		{
			int numPar= Integer.parseInt(nParking.getText());
			int capacitePar = Integer.parseInt(capaciteParking.getText());
			
			Parking parking = new Parking(numPar, nomParking.getText(), capacitePar , rue.getText(), arrondissement.getText());
			String sql = "SELECT `nParking` FROM `parking` WHERE nParking='"+numPar+"';";
			Connection C = Login.connectDB();
			PreparedStatement ps = C.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			if(result.next() && parking.getnParking()!=rNumPar) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un vehicule avec le même N° Immatriculation");
				alert.show();
			}
			else {
				String sql2 = "UPDATE `parking` SET `nParking`=? ,`nom`=? ,`capacite`=? ,`rue`=? ,`arrondissement`=? WHERE nParking=?;";
				Connection C2 = Login.connectDB();
				PreparedStatement ps2 = C2.prepareStatement(sql2);
				ps2.setInt(1, parking.getnParking());
				ps2.setString(2, parking.getNom());
				ps2.setInt(3, parking.getCapacite());
				ps2.setString(4, parking.getRue());
				ps2.setString(5, parking.getArrondissement());	
				ps2.setInt(7, rNumPar);
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
	
	public void supprimerParking(ActionEvent e) throws SQLException, IOException {
		String sql = "DELETE FROM `parking` WHERE nParking=?;";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ps.setInt(1, rNumPar);
		ps.executeUpdate();
		C.close();
		retourGestion();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Succès");
		alert.setHeaderText("Le vehicule a été supprimé");
		alert.show();
	}
	
	public void interfaceInfosParking(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/InfosParking.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	public void retourGestion() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/GestionParking.fxml")));
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
