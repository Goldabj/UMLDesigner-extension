package bstuff;

import graphvizcreator.AbstractPatternDrawer;
import umlentities.IDrawer;
import umlentities.IUMLEntity;

public class LawDemeterDependencyDrawer extends AbstractPatternDrawer {

	public LawDemeterDependencyDrawer() {
	}

	public LawDemeterDependencyDrawer(IDrawer drawer, IUMLEntity pattern) {
		super(drawer, pattern);
	}
	
	@Override
	public String draw() {
		String superString = super.draw();
		LawDemeterDependency depPat = (LawDemeterDependency) this.getEntity();

		String[] attributes = superString.split("\\[");
		String atts = attributes[1];

		atts = "[ color=" + depPat.getColor() + ", " + atts;

		for (int i = 2; i < attributes.length; i++) {
			atts += "[ " + attributes[1] + " ";
		}

		String finalString = attributes[0] + " " + atts;

		return finalString;
	}

}
