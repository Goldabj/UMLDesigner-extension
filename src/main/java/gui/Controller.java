package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
public class Controller {
	private ProjectModel pm;
	private Configuration config;
	private ProgramRunner runner;
	private boolean settingsFileCustom;
	private static final String defaultSettingsFile = "Settings/GUISettings.json";
	
	public static final String PRIVATE_PROTECTIOIN = "-private";
	public static final String PROTECTED_PROTECTION = "-protected";
	public static final String PUBLIC_PROTECTION = "-public";

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
	


}
