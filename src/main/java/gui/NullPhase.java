package gui;

import projectRunner.Phase;
import projectRunner.ProjectModel;

public class NullPhase implements Phase {
	ProjectModel pm;

	public NullPhase() {
	}

	@Override
	public ProjectModel getResults() {
		return this.pm;
	}

	@Override
	public void runPhase(ProjectModel arg0) {
		this.pm = arg0;

	}

}
