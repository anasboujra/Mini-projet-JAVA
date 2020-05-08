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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class GestionReservation {
	
	@FXML
	private TextField codeReservation;
		
	@FXML
	private DatePicker dateReservation;
	
	@FXML
	private DatePicker dateDepart;
	
	@FXML
	private DatePicker dateRetour;

	public void exitButton() {
		Main.stage.close();
	}
	
	
	public void interfaceAjouterReservation(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/AjouterReservation.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void ajouterReservation(ActionEvent e) throws IOException, SQLException {
		if( !codeReservation.getText().equals("") &&  !dateReservation.equals(null) && !dateDepart.equals(null) && !dateRetour.equals(null) ) 
		{
			String cSql = "SELECT * FROM `reservation` WHERE codeReservation='"+codeReservation.getText()+"';";
			Connection cC = Login.connectDB();
			PreparedStatement cPS = cC.prepareStatement(cSql);
			ResultSet reservationResult = cPS.executeQuery();
			if(reservationResult.next()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un utilisateur avec le même CIN");
				alert.show();
			} 
			else {
				 
				Reservation reservation = new Reservation(codeReservation.getText(), dateReservation.getValue(), dateDepart.getValue(),dateRetour.getValue());
				String sql = "INSERT INTO `reservation`(`codeReservation`, `dateReservation`, `dateDepart`, `dateRetour`) VALUES (?,?,?,?)";
				Connection C = Login.connectDB();
				PreparedStatement ps = C.prepareStatement(sql);
				ps.setString(1, reservation.getCodeReservation());
				ps.setObject(2, reservation.getDateReservation());
				ps.setObject(3, reservation.getDateDepart());
				ps.setObject(4, reservation.getDateRetour());
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
	
	
	
	
	public void retourGestion() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/GestionReservation.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
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
}
