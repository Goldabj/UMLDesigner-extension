package featuretests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import patternparser.AdapterPatternDetector;
import patternparser.IPatternDetector;
import patternparser.PatternParser;
import projectparser.ProjectParser;
import umlentities.IUMLNode;
import umlentityparser.IEntityParser;
import umlentityparser.PrivateParser;

public class ClassCreatorTest {
	
	@Test
	public void test() {
		List<String> classes = new ArrayList<String>();
		classes.add("testClasses.AdaptedInterface");
		classes.add("testClasses.AdapterClass");
		classes.add("testClasses.FromAdaptedInterface");

		ProjectParser parser = new ProjectParser(classes);
		parser.parseClasses();
		List<ClassNode> classNodes = new ArrayList<>();
		classNodes = parser.getClassNodes();
		IEntityParser par = new PrivateParser();
		par.setClasses(classNodes);
		par.parseClasses();
		List<IUMLNode> nodes = par.getNodes();

		List<IPatternDetector> detectors = new ArrayList<>();
		IPatternDetector dect = new AdapterPatternDetector();
		detectors.add(dect);
		//PatternParser patParser = new PatternParser(nodes, detectors);
		//patParser.detectPatterns();
		//List<IUMLNode> patterns = patParser.getUpdatedlNodes();

	}

}
