package cstuff;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import projectRunner.ProgramRunner;
import projectRunner.ProjectModel;

public class MainC {

	/**
	 * 
	 * 
	 * @param args - first arg is class to detect
	 * 				- second arg is settings file name
	 */
	public static void main(String[] args) {
		ProgramRunner runner = new ProgramRunner();
		runner.setSettingsFile("Settings/ClassDetectSettings1.json");
		
		if (args.length > 0) {
			ClassCreatorDetector.setClassToDetect(args[0]);
		}
		if (args.length > 1) {
			runner.setSettingsFile(args[1]);
		}

		runner.loadProgram();
		runner.executeProgram();
		
		File drawableFile = ProjectModel.getInstance().getDrawableFile();
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
