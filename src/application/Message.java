package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Message {
	
	static Stage stage;
	
	@FXML
	private Label alerte;
	
	public void exitButton() {
		stage.close();
	}
	
	public void alert(String message) throws IOException {
		stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("GUI/Message.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		stage.centerOnScreen();
		alerte.setText("ok");
	}
}
