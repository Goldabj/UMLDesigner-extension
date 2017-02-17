package bstuff;

import patternparser.PatternParserPhase;
import projectRunner.Phase;
import projectRunner.ProjectModel;

public class PatternPhase implements Phase {
	private Phase underlying;
	private ProjectModel pm;

	public PatternPhase() {
		this.underlying = new PatternParserPhase();
		pm = ProjectModel.getInstance();
	}

	@Override
	public ProjectModel getResults() {
		return this.underlying.getResults();
	}

	@Override
	public void runPhase(ProjectModel arg0) {
		// set the lawDemeters classNodes then run
		LawDemeterDetector.setClassNodes(pm.getClassNodes());
		underlying.runPhase(arg0);

	}

}
