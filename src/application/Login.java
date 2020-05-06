package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class Login {
	@FXML
	private Label connectionLabel;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;

	
	public void exitButton() {
		Main.stage.close();
	}
	
	public static Connection connectDB(){
		Connection C = null;
		try {
			C = DriverManager.getConnection("jdbc:mysql://localhost/java","root","");
			System.out.println("Connected to DB");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return C;
	}
	
	public void connectionStat(ActionEvent e) throws SQLException{
		if(!Login.connectDB().isClosed())
			connectionLabel.setText("connecté");
		else
			connectionLabel.setText("pas connecté");
	}
	
	public void loginButton(ActionEvent e) throws IOException, SQLException {
		String adminSql = "SELECT * FROM `administrateur` WHERE username='"+username.getText()+"';";
		Connection adminC = Login.connectDB();
		PreparedStatement adminPS = adminC.prepareStatement(adminSql);
		ResultSet adminResult = adminPS.executeQuery();
		if(adminResult.next()) {
			if(password.getText().equals(adminResult.getString(2))) {
				Main.ad=true;
				Parent panelRoot = FXMLLoader.load(getClass().getResource(("GUI/AdminPanel.fxml")));
				panelRoot.setOnMousePressed(Main.handlerPressed);
				panelRoot.setOnMouseDragged(Main.handlerDragged);
				Scene panelScene = new Scene(panelRoot);
				Main.stage.setScene(panelScene);
				Main.stage.centerOnScreen();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("L'identifiant ou le mot de passe est incorrect");
				alert.show();
			}
		}
		else {
			String userSql = "SELECT * FROM `utilisateur` WHERE cin='"+username.getText()+"';";
			Connection userC = Login.connectDB();
			PreparedStatement userPS = userC.prepareStatement(userSql);
			ResultSet userResult = userPS.executeQuery();
			if(userResult.next() && password.getText().equals(userResult.getString(2))) {
				if(userResult.getString(7).equals("continuer")) {
					Main.ad=false;
					Parent panelRoot = FXMLLoader.load(getClass().getResource(("GUI/UserPanel.fxml")));
					panelRoot.setOnMousePressed(Main.handlerPressed);
					panelRoot.setOnMouseDragged(Main.handlerDragged);
					Scene panelScene = new Scene(panelRoot);
					Main.stage.setScene(panelScene);
					Main.stage.centerOnScreen();
				}
				else if(userResult.getString(7).equals("suspendre")) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("INFORMATION");
					alert.setHeaderText("Cet utilisateur a été suspendu");
					alert.show();
				}
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("L'identifiant ou le mot de passe est incorrect");
				alert.show();
			}
		}
	}
}
