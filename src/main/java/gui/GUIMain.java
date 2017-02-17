package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUIMain extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button allAsbtn = new Button("Click this badass button to give"
				+ " BBV squad all As on this not as cool project");	
		
		
		StackPane root = new StackPane();
		root.getChildren().add(allAsbtn);
		Scene scene1 = new Scene(root, 500, 500);
		primaryStage.setScene(scene1);
		primaryStage.show();
	}

}
