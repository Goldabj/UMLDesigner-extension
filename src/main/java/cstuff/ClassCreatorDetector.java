package cstuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import patternparser.AbstractPatternDetector;
import umlentities.ClassInheritanceDependency;
import umlentities.CreatesDependency;
import umlentities.HasADependency;
import umlentities.IUMLDependency;
import umlentities.IUMLNode;
import umlentities.InterfaceImplementDependency;
import umlentities.UMLField;
import umlentities.UMLMethod;

public class ClassCreatorDetector extends AbstractPatternDetector {
	private static String className; // this static thing might not work since
										// it is
										// dynamically loaded in the other class

	public ClassCreatorDetector() {
	}

	public static void setClassToDetect(String clazz) {
		className = clazz;
	}

	@Override
	public void detectPattern() {
		// problem, since our dep optimizer removes createsDeps if their is a
		// field of the same type
		// then that class is not detected
		// so instead well check if it has the field, and doesn't take the class
		// as a param to <init> then
		// we will count it as detected

		if (className == null) {
			System.out.println("retreving class to detect from detectorFile");
			// if the static thing doesn't work then this is plan B
			Scanner detectFile = null;
			try {
				detectFile = new Scanner(new File("Configurations/detectorClassFile"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			className = detectFile.nextLine();
		}
		List<String> allClassNames = new ArrayList<>();
		allClassNames.add(className);
		// now get all subclasses of this class
		// if className is not included in the uml then we will have no subtypes
		getAllClassNames(allClassNames);

		List<IUMLNode> toMakePattern = new ArrayList<>();
		// now check if any of these nodes has a creates dep to one of these
		// classes
		List<IUMLNode> nodes = this.getNodes();
		for (IUMLNode node : nodes) {
			List<IUMLDependency> deps = node.getDependencies();
			for (IUMLDependency dep : deps) {
				if (dep.getDependencyType().equals(CreatesDependency.CLASS_TYPE)) {
					for (String name : allClassNames) {
						if (dep.getTo().equals(name)) {
							toMakePattern.add(node);
						}
					}
				} else {
					if (dep.getDependencyType().equals(HasADependency.CLASS_TYPE)) {
						for (String name : allClassNames) {
							if (dep.getTo().equals(name)) {
								// check the <init> to see if it has a
								// paramerter of this type
								// check counts equal
								int fieldTypeCount = 0;
								int paramTypeCount = 0;
								int maxParamTypeCount = 0;
								List<UMLField> fields = node.getFields();
								for (UMLField field : fields) {
									if (field.getFieldType().equals(name)) {
										fieldTypeCount++;
									}
								}
								List<UMLMethod> mets = node.getMethods();
								for (UMLMethod met : mets) {
									if (met.getName().equals("<init>")) {
										paramTypeCount = 0;
										List<String> args = met.getArguments();
										for (String arg : args) {
											if (arg.equals(name)) {
												paramTypeCount++;
												if (paramTypeCount > maxParamTypeCount) {
													maxParamTypeCount = paramTypeCount;
												}
											}
										}
									}
								}
								if (fieldTypeCount > maxParamTypeCount) {
									toMakePattern.add(node);
								}
							}
						}
					}
				}
			}
		}
		List<IUMLNode> patterns = new ArrayList<>();
		for (IUMLNode node : toMakePattern) {
			ClassCreatorPattern pat = new ClassCreatorPattern(node);
			pat.setDetectClassName(className);
			patterns.add(pat);
		}

		this.addPatterns(patterns);

	}

	private void getAllClassNames(List<String> allNames) {
		// get all subtypes of the className and add them to the list
		List<IUMLNode> nodes = this.getNodes();
		for (IUMLNode node : nodes) {
			List<IUMLDependency> deps = node.getDependencies();
			for (IUMLDependency dep : deps) {
				if (dep.getDependencyType().equals(ClassInheritanceDependency.CLASS_TYPE)
						|| dep.getDependencyType().equals(InterfaceImplementDependency.CLASS_TYPE)) {
					if (dep.getTo().equals(className)) {
						allNames.add(node.getClassName());
					}
				}
			}
		}
	}

}
