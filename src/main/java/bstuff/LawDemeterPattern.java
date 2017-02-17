package bstuff;

import patternparser.AbstractPatternNode;
import umlentities.IUMLNode;

public class LawDemeterPattern extends AbstractPatternNode {
	public static final String CLASS_TYPE = "LawDemeterPattern";
	private String color = "cyan";
	private String tag = "<LawOfDemeterViolation>";

	public LawDemeterPattern() {
	}
	
	public LawDemeterPattern(IUMLNode decorated) {
		super(decorated);
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getTag() {
		return this.tag;
	}

	@Override
	public String getType() {
		return CLASS_TYPE;
	}

}
