package cstuff;

import patternparser.AbstractPatternNode;
import umlentities.IUMLNode;

public class ClassCreatorPattern extends AbstractPatternNode {
	public static final String CLASS_TYPE = "ClassCreatorPattern";
	private String classColor = "purple";
	private String tag = "CratesClass";

	public ClassCreatorPattern() {
	}

	public ClassCreatorPattern(IUMLNode decorated) {
		super(decorated);
	}

	@Override
	public String getType() {
		return CLASS_TYPE;
	}
	
	public void setDetectClassName(String clazz) {
		this.tag = this.tag + "<" + clazz + ">";
	}
	
	public String getColor() {
		return this.classColor;
	}
	
	public String getTag() {
		return this.tag;
	}

}
