package bstuff;

import patternparser.UMLDependencyDecorator;
import umlentities.IUMLDependency;

public class LawDemeterDependency extends UMLDependencyDecorator {
	public static final String TYPE = "LawDemeterDependency";
	private String color = "cyan";
	
	public LawDemeterDependency() {
	}

	public LawDemeterDependency(IUMLDependency dep) {
		super(dep);
	}
	
	public String getColor() {
		return this.color;
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
