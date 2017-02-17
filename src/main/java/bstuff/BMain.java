package bstuff;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import projectRunner.ProgramRunner;
import projectRunner.ProjectModel;

public class BMain {

	/**
	 * 
	 * 
	 * @param args - first arg is the settings file to run
	 */
	public static void main(String[] args) {
		// problem, we dont have UML things that check for method calls
		// so we must get the project models classNodes
		// and add this checking ourself
		
		// set the LawDemetersDetectors classNodes before we detect with it
		// therfore we need to set our patternPhase to our own
		
		ProgramRunner runner = new ProgramRunner();
		runner.setSettingsFile("Settings/LawDemeterSettings1.json");
		
		if (args.length > 0) {
			runner.setSettingsFile(args[0]);
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
