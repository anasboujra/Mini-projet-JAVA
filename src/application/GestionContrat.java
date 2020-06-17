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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GestionContrat {
	
	String rCode;

	@FXML
	private TextField codeContrat;
	
	@FXML
	private TextField rechercher;
	
	@FXML
	private DatePicker dateContrat;
	
	@FXML
	private DatePicker dateEcheance;
	
	// Table d'affichage
		@FXML
		private TableView<Contrat> tableview;
		@FXML
		private TableColumn<Contrat,String> code;
		@FXML
		private TableColumn<Contrat,?> dateC;
		@FXML
		private TableColumn<Contrat,?> dateE;
	
		
	public void exitButton() {
		Main.stage.close();
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
		if( !codeContrat.getText().equals("") &&  dateContrat.getValue()!=null && dateEcheance.getValue()!=null ) 
		{
			String cSql = "SELECT * FROM `contrat` WHERE codeContrat='"+codeContrat.getText()+"';";
			Connection cC = Login.connectDB();
			PreparedStatement cPS = cC.prepareStatement(cSql);
			ResultSet contratResult = cPS.executeQuery();
			if(contratResult.next()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà un contat avec le même Code");
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
	
	public void interfaceModifierContrat(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ModifierContrat.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void rechercherContrat(ActionEvent e) throws SQLException {
		rCode = rechercher.getText();
		String sql = "SELECT * FROM `contrat` WHERE codeContrat='"+rCode+"';";
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
			alert.setHeaderText("Il n'y a aucun contrat avec ce CODE");
			alert.show();			
		}
		C.close();
	}
	
	public void modifierContrat(ActionEvent e) throws IOException, SQLException {
		if(!codeContrat.getText().equals("") &&  dateContrat.getValue()!=null && dateEcheance.getValue()!=null) {
			
			Contrat contrat = new Contrat(codeContrat.getText(),"" ,dateContrat.getValue(),dateEcheance.getValue());
			String sql = "SELECT `codeContrat` FROM `contrat` WHERE codeContrat='"+contrat.getCodeContrat()+"';";
			Connection C = Login.connectDB();
			PreparedStatement ps = C.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			if(result.next() && !contrat.getCodeContrat().equalsIgnoreCase(rCode)) {	
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
		alert.setHeaderText("Le contrat a été supprimé");
		alert.show();
	}
	
	public void interfaceInfosContrat(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/InfosContrat.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void interfaceContratDecroissance(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ContratDecroissance.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	public void actualiser(ActionEvent e) throws SQLException {
		ObservableList<Contrat> data = FXCollections.observableArrayList();	
		String sql = "SELECT * FROM contrat ORDER BY dateContrat ;";
		Connection C = Login.connectDB();
		PreparedStatement ps = (PreparedStatement)C.prepareStatement(sql);
		ResultSet result = ps.executeQuery(sql);
		while(result.next())
			{
			Contrat contrat = new Contrat();
				contrat.setCodeContrat(result.getString(1));
				Date sqlDateContrat=result.getDate(2);
				contrat.setDateContrat(sqlDateContrat.toLocalDate());
				Date sqlDateEcheance=result.getDate(3);
				contrat.setDateEcheance(sqlDateEcheance.toLocalDate());
				data.add(contrat);
			}
				code.setCellValueFactory(new PropertyValueFactory<Contrat,String>("codeContrat"));
				dateC.setCellValueFactory(new PropertyValueFactory<>("dateContrat"));
				dateE.setCellValueFactory(new PropertyValueFactory<>("dateEcheance"));
				
				tableview.setItems(data);
		C.close();
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
