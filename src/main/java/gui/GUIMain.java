package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import projectRunner.Phase;
import projectRunner.ProjectModel;

public class GUIMain extends Application implements Phase{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("BATest.fxml"));
		Scene scene1 = new Scene(root);
		primaryStage.setTitle("BBVSquad :)");
		primaryStage.setScene(scene1);
		primaryStage.show();
		
		
		//====================================Table================================================
		
		
	}

	@Override
	public ProjectModel getResults() {
		return ProjectModel.getInstance();
	}

	@Override
	public void runPhase(ProjectModel arg0) {
		main(new String[0]); // idk if this will really work or not
	}

}
