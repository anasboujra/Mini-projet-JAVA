package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionReservation {
	
	@FXML
	private TextField codeReservation;
	
	@FXML
	private TextField rechercher;
		
	@FXML
	private DatePicker dateReservation;
	
	@FXML
	private DatePicker dateDepart;
	
	@FXML
	private DatePicker dateRetour;
	
	// Table d'affichage
			@FXML
			private TableView<Reservation> tableview;
			@FXML
			private TableColumn<Reservation,String> code;
			@FXML
			private TableColumn<Reservation,?> dateRe;
			@FXML
			private TableColumn<Reservation,?> dateD;
			@FXML
			private TableColumn<Reservation,?> dateRo;
			
			

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
	
	public void interfaceModifierReservation(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ModifierReservation.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void rechercherReservation(ActionEvent e) throws SQLException {
		String RESERVATION = rechercher.getText();
		String sql = "SELECT * FROM `reservation` WHERE codeReservation='"+ RESERVATION +"';";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ResultSet result = ps.executeQuery();
		if(result.next()) {
			codeReservation.setText(result.getString(1));
			Date sqlDateReservation=result.getDate(2);
			dateReservation.setValue(sqlDateReservation.toLocalDate());
			Date sqlDateDepart = result.getDate(3);
			dateDepart.setValue(sqlDateDepart.toLocalDate());
			Date sqlDateRetour = result.getDate(4);
			dateRetour.setValue(sqlDateRetour.toLocalDate());
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Il n'y a aucun utilisateur avec ce CODE");
			alert.show();			
		}
		C.close();
	}
	
	public void modifierReservation(ActionEvent e) throws IOException, SQLException {
		if(!codeReservation.getText().equals("") && !dateReservation.equals(null) && !dateDepart.equals(null) && !dateRetour.equals(null)) {
			
			Reservation reservation = new Reservation(codeReservation.getText(),dateReservation.getValue(),dateDepart.getValue(),dateRetour.getValue());
			String sql = "SELECT `codeReservation` FROM `reservation` WHERE codeReservation='"+reservation.getCodeReservation()+"';";
			Connection C = Login.connectDB();
			PreparedStatement ps = C.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			if(result.next() && !reservation.getCodeReservation().equalsIgnoreCase(rechercher.getText())) {	
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un utilisateur avec le même CODE");
				alert.show();
			}
			else {
				String sql2 = "UPDATE `reservation` SET `codeReservation`=? ,`dateReservation`=? ,`dateDepart`=? ,`dateRetour`=?  WHERE codeReservation=?;";
				Connection C2 = Login.connectDB();
				PreparedStatement ps2 = C2.prepareStatement(sql2);
				ps2.setString(1, reservation.getCodeReservation());
				ps2.setObject(2, reservation.getDateReservation());
				ps2.setObject(3, reservation.getDateDepart());
				ps2.setObject(4, reservation.getDateRetour());
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
	
	public void supprimerReservation(ActionEvent e) throws SQLException, IOException {
		String sql = "DELETE FROM `reservation` WHERE codeReservation=?;";
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
	
	public void interfaceInfosReservation(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/InfosReservation.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void interfaceReservationDecroissance(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ReservationDecroissance.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void actualiser(ActionEvent e) throws SQLException {
		ObservableList<Reservation> data = FXCollections.observableArrayList();	
		String sql = "SELECT * FROM reservation ORDER BY dateReservation ;";
		Connection C = Login.connectDB();
		PreparedStatement ps = (PreparedStatement)C.prepareStatement(sql);
		ResultSet result = ps.executeQuery(sql);
		while(result.next())
			{
			Reservation reservation = new Reservation();
				reservation.setCodeReservation(result.getString(1));
				Date sqlDateReservation=result.getDate(2);
				reservation.setDateReservation(sqlDateReservation.toLocalDate());
				Date sqlDateDepart=result.getDate(3);
				reservation.setDateDepart(sqlDateDepart.toLocalDate());
				Date sqlDateRetour=result.getDate(4);
				reservation.setDateRetour(sqlDateRetour.toLocalDate());
				data.add(reservation);
			}
				code.setCellValueFactory(new PropertyValueFactory<Reservation,String>("codeReservation"));
				dateRe.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
				dateD.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
				dateRo.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));
				
				tableview.setItems(data);
		C.close();
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
