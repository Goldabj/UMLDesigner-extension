package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import projectRunner.Configuration;
import projectRunner.ProgramRunner;
import projectRunner.ProjectModel;

/**
 * This class is used by the GUI to control the project - load it, change configurations, and
 * run the program 
 * 
 * @author goldacbj
 *
 */
public class Controller implements Initializable{
	private ProjectModel pm;
	private Configuration config;
	private ProgramRunner runner;
	private boolean settingsFileCustom;
	private static final String defaultSettingsFile = "Settings/GUISettings.json";
	
	public static final String PRIVATE_PROTECTIOIN = "-private";
	public static final String PROTECTED_PROTECTION = "-protected";
	public static final String PUBLIC_PROTECTION = "-public";
	public static final String DEFAULT_FILECREATOR = "graphvizcreator.GraphVizCreator";
	
	
	@FXML
	private ChoiceBox<String> protectionLevelOption;
	
	@FXML
	private CheckBox recursionBox;
	
	@FXML
	private TextField fileCreatorText;
	
	@FXML
	private TextArea whiteListText;
	
	@FXML
	private TextArea blackListText;
	
	@FXML
	private TextArea patternDetectorsText;
	
	@FXML
	private TextField settingsFileText;
	
	@FXML
	private Button settingsCreateBttn;
	
	@FXML
	private Button regCreateBttn;
	
	@FXML
	private TextArea patternDrawersArea;
	
	public Controller() {
		this.pm = ProjectModel.getInstance();
		this.config = Configuration.getInstance();
		this.runner = new ProgramRunner();
		settingsFileCustom = false;
	}
	
	public void changeProtectionLevel(String protectionLevel) {
		this.config.setProtectLevel(protectionLevel);
	}
	
	public void setWhiteList(List<String> whiteList) {
		this.config.setWhiteList(whiteList);
		this.pm.setClassNames(whiteList);
	}
	
	public void setBlackList(List<String> blackList) {
		this.config.setBlackList(blackList);
	}
	
	public void setRecursionOn(boolean isOn) {
		this.config.setRecursive(isOn);
	}
	
	public void setPatternDetectors(List<String> detectors) {
		this.config.setDetectors(detectors);
	}
	
	public void setPatternsToDrawers(Map<String, String> patternsToDrawers) {
		this.config.setPatternsToDrawers(patternsToDrawers);
	}
	
	public void setFileCreator(String fileCreator) {
		this.config.setFileCreator(fileCreator);
	}
	
	public void setSettingsFile(String file) {
		this.runner.setSettingsFile(file);
		settingsFileCustom = true;
	}
	
	public void run() {
		// set the pms config to this one
		this.pm.setConfiguration(this.config);
		
		this.runner.loadProgram();
		if (!settingsFileCustom) {
			this.runner.setSettingsFile(defaultSettingsFile);
		}
		this.pm = this.runner.executeProgram();
		
		
		// open drawable file
		File drawableFile = pm.getDrawableFile();
		if (drawableFile != null) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().open(drawableFile);
				} catch (IOException e) {
					System.err.println("could not open the drawableFile");
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@FXML
	public void handleRegRun(ActionEvent e) {
		// handles the running of the program
		// gets all info from the fields
		// then sets the config
		// then calls run
		this.runner.setSettingsFile("Settings/GUISettings.json");
		
		String protectLevel = this.protectionLevelOption.getSelectionModel().getSelectedItem();
		this.changeProtectionLevel(protectLevel);
		
		String filecreator = this.fileCreatorText.getText();
		if (filecreator == null || filecreator.equals("")) {
			this.setFileCreator(DEFAULT_FILECREATOR);
		}else {
			this.setFileCreator(filecreator);
		}
		
		boolean recursive = this.recursionBox.isSelected();
		this.setRecursionOn(recursive);
		
		String whiteListText = this.whiteListText.getText();
		if (!whiteListText.isEmpty()) {
			String[] classes = whiteListText.split(",");
			List<String> whiteList = new ArrayList<>();
			for (int i = 0; i < classes.length; i++) {
				String clazz = classes[i].trim();
				whiteList.add(clazz);
			}
			this.setWhiteList(whiteList);
		}else {
			this.setWhiteList(new ArrayList<>());
		}
		
		
		String blackListText = this.blackListText.getText();
		if (!blackListText.isEmpty()) {
			String[] bClasses = blackListText.split(",");
			List<String> blackList = new ArrayList<>();
			for (int i = 0; i < bClasses.length; i++) {
				String clazz = bClasses[i].trim();
				blackList.add(clazz);
			}
			this.setBlackList(blackList);
		}else {
			this.setBlackList(new ArrayList<>());
		}
		
		
		String patternDectText = this.patternDetectorsText.getText();
		if (!patternDectText.isEmpty()) {
			String[] detectors = patternDectText.split(",");
			List<String> detectorsL = new ArrayList<>();
			for (int i = 0; i < detectors.length; i++) {
				String dect = detectors[i].trim();
				detectorsL.add(dect);
			}
			this.setPatternDetectors(detectorsL);
		}else{
			this.setPatternDetectors(new ArrayList<>());
		}
		
	
		Map<String, String> patsToDrawers = new HashMap<>();
		String patternDrawersTest = this.patternDrawersArea.getText();
		if (!patternDrawersTest.isEmpty()) {
			String[] pairs = patternDrawersTest.split(",");
			for (int i = 0; i < pairs.length; i++) {
				String pair = pairs[i];
				String[] splitup = pair.split(":");
				String pattern = splitup[0].trim();
				String drawer = splitup[1].trim();
				patsToDrawers.put(pattern, drawer);
			}
		}	
		this.setPatternsToDrawers(patsToDrawers);
		
		
		this.run();
	}
	
	@FXML
	public void handleSettingsFileRun(ActionEvent e) {
		String settingsFile = this.settingsFileText.getText();
		this.setSettingsFile(settingsFile);
		this.run();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.protectionLevelOption.setItems(FXCollections.observableArrayList(
				PRIVATE_PROTECTIOIN, PROTECTED_PROTECTION,PUBLIC_PROTECTION));
	}
	


}
