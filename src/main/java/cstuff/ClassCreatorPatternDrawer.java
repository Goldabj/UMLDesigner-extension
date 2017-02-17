package cstuff;

import graphvizcreator.AbstractPatternDrawer;
import umlentities.IDrawer;
import umlentities.IUMLEntity;

public class ClassCreatorPatternDrawer extends AbstractPatternDrawer {

	public ClassCreatorPatternDrawer() {
	}

	public ClassCreatorPatternDrawer(IDrawer drawer, IUMLEntity pattern) {
		super(drawer, pattern);
	}

	

	@Override
	public String draw() {
		ClassCreatorPattern pat = (ClassCreatorPattern) this.getEntity();
		
		// filling class color
		String sup = super.draw();
		String[] nameAndatts = sup.split("\\[");
		String atts = nameAndatts[1];
		String newAtts = "[ style=\"filled\"" + ", " + "fillcolor=" + "\"" + pat.getColor() + "\"" + ", "
				+ "color=\"black\"" + ", " + atts;
		for (int i = 2; i < nameAndatts.length; i++) {
			newAtts += " [ " + nameAndatts[i];
		}
		String finalString = nameAndatts[0] + " " + newAtts;

		// adding the class tag
		String[] labelandOthers = finalString.split("label=");
		String label = labelandOthers[1];
		String[] classNameandOthers = label.split("\\|");
		String classname = classNameandOthers[0];
		classname = classname + " " + "\\n" + pat.getTag().replace("<", "\\<").replace(">", "\\>");

		classNameandOthers[0] = classname;
		String allClassName = "";
		for (int i = 0; i < classNameandOthers.length; i++) {
			allClassName += classNameandOthers[i] + "|";
		}
		allClassName = allClassName.substring(0, allClassName.length() - 1);
		labelandOthers[1] = allClassName;
		String finalClassString = "";
		for (int i = 0; i < labelandOthers.length; i++) {
			finalClassString += labelandOthers[i] + "label= ";
		}
		finalClassString = finalClassString.substring(0, finalClassString.length() - 7);

		return finalClassString;

	}

}
