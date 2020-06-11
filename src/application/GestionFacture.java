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


public class GestionFacture {
	
	String rCodeFacture;

	@FXML
	private TextField codeFacture;
	
	@FXML
	private DatePicker dateFacture;
	
	@FXML
	private TextField montantPayer;
	
	@FXML
	private TextField rechercher;
	
	// Table d'affichage
			@FXML
			private TableView<Facture> tableview;
			@FXML
			private TableColumn<Facture,String> code;
			@FXML
			private TableColumn<Facture,?> dateF;
			@FXML
			private TableColumn<Facture,Double> mPayer;
	public void exitButton() {
		Main.stage.close();
	}
	
	
	public void interfaceAjouterFacture(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/AjouterFacture.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	
	public void ajouterFacture(ActionEvent e) throws IOException, SQLException {
		if( !codeFacture.getText().equals("") &&  dateFacture.getValue()!=null && !montantPayer.getText().equals("") ) 
		{
			String cSql = "SELECT * FROM `facture` WHERE codeFacture='"+codeFacture.getText()+"';";
			Connection cC = Login.connectDB();
			PreparedStatement cPS = cC.prepareStatement(cSql);
			ResultSet contratResult = cPS.executeQuery();
			if(contratResult.next()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà une facture avec le même Code");
				alert.show();
			} 
			else {
				String montantP = montantPayer.getText();
				double montant= Double.parseDouble(montantP);
				Facture facture = new Facture(codeFacture.getText(), dateFacture.getValue(), montant);
				String sql = "INSERT INTO `facture`(`codeFacture`, `dateFacture`, `montantPayer`) VALUES (?,?,?)";
				Connection C = Login.connectDB();
				PreparedStatement ps = C.prepareStatement(sql);
				ps.setString(1, facture.getCodeFacture());
				ps.setObject(2, facture.getDateFacture());
				ps.setDouble(3, facture.getMontantPayer());
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
	
	public void interfaceModifierFacture(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/ModifierFacture.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}

	public void rechercherFacture() throws SQLException {
		String rCodeFacture = rechercher.getText();
		String sql = "SELECT * FROM facture WHERE codeFacture='"+rCodeFacture+"';";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ResultSet result = ps.executeQuery();
		if(result.next()) {
			codeFacture.setText(result.getString(1));
			Date sqlDateFacture=result.getDate(2);
			dateFacture.setValue(sqlDateFacture.toLocalDate());
			montantPayer.setText(result.getString(3));
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Il n'y a aucune Facture avec ce Code");
			alert.show();
		}
		C.close();
	}
	
	public void modifierFacture(ActionEvent e) throws IOException, SQLException {
		if( !codeFacture.getText().equals("") &&  dateFacture.getValue()!=null && !montantPayer.getText().equals("") ) 
		{	
			String montantP = montantPayer.getText();
			double montant= Double.parseDouble(montantP);
			Facture facture = new Facture(codeFacture.getText(), dateFacture.getValue(), montant);
			String sql = "SELECT `codeFacture` FROM `facture` WHERE codeFacture='"+facture.getCodeFacture()+"';";
			Connection C = Login.connectDB();
			PreparedStatement ps = C.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			if(result.next() && !facture.getCodeFacture().equalsIgnoreCase(rCodeFacture)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Il y'a déjà une Facture avec le meme code ");
				alert.show();
			}
			else {
				String sql2 = "UPDATE `facture` SET `codeFacture`=?, `dateFacture`=?, `montantPayer`=?  WHERE codeFacture=?;";
				Connection C2 = Login.connectDB();
				PreparedStatement ps2 = C2.prepareStatement(sql2);
				ps2.setString(1, facture.getCodeFacture());
				ps2.setObject(2, facture.getDateFacture());
				ps2.setDouble(3, facture.getMontantPayer());
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
	
	public void supprimerFacture(ActionEvent e) throws SQLException, IOException {
		String sql = "DELETE FROM `facture` WHERE codeFacture=?;";
		Connection C = Login.connectDB();
		PreparedStatement ps = C.prepareStatement(sql);
		ps.setString(1, rechercher.getText());
		ps.executeUpdate();
		C.close();
		retourGestion();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Succès");
		alert.setHeaderText("La facture a été supprimé");
		alert.show();
	}
	
	public void interfaceInfosFacture(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/InfosFacture.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	 
	public void interfaceFactureDecroissance(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/FactureDecroissance.fxml")));
		root.setOnMousePressed(Main.handlerPressed);
		root.setOnMouseDragged(Main.handlerDragged);
		Scene scene = new Scene(root);
		Main.stage.setScene(scene);
		Main.stage.centerOnScreen();
	}
	public void actualiser(ActionEvent e) throws SQLException {
		ObservableList<Facture> data = FXCollections.observableArrayList();	
		String sql = "SELECT * FROM facture ORDER BY dateFacture DESC;";
		Connection C = Login.connectDB();
		PreparedStatement ps = (PreparedStatement)C.prepareStatement(sql);
		ResultSet result = ps.executeQuery(sql);
		while(result.next())
			{
			Facture facture = new Facture();
				facture.setCodeFacture(result.getString(1));
				Date sqlDateFacture=result.getDate(2);
				facture.setDateFacture(sqlDateFacture.toLocalDate());
				facture.setMontantPayer(result.getDouble(3));
				data.add(facture);
			}
				code.setCellValueFactory(new PropertyValueFactory<Facture,String>("codeFacture"));
				dateF.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));
				mPayer.setCellValueFactory(new PropertyValueFactory<Facture,Double>("montantPayer"));
				
				tableview.setItems(data);
		C.close();
	}
	
	public void retourGestion() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(("GUI/GestionFacture.fxml")));
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
