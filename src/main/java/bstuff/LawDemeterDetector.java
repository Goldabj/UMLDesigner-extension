package bstuff;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;

import patternparser.AbstractPatternDetector;
import umlentities.ClassInheritanceDependency;
import umlentities.CreatesDependency;
import umlentities.IUMLDependency;
import umlentities.IUMLNode;
import umlentities.InterfaceImplementDependency;

public class LawDemeterDetector extends AbstractPatternDetector {
	private static List<ClassNode> nodes;

	public LawDemeterDetector() {
	}

	public static void setClassNodes(List<ClassNode> nds) {
		nodes = nds;
	}

	private class Pair {
		public String from;
		public String to;

		public Pair(String f, String t) {
			this.from = f;
			this.to = t;
		}
	}

	@Override
	public void detectPattern() {
		// Simplified Law of Demeter
		// Only call methods on classes that are one of your friends. In a given
		// method m, your friends are:
		// • m’s class and all of its subclasses.
		// • The classes of m’s parameters and their subclasses.
		// • The classes of any objects created/instantiated within m.
		// • The classes and subclasses of fields that belong to m’s class.
		// • The classes associated with static method calls.

		List<Pair> depsToMakePatterns = new ArrayList<>();

		for (ClassNode node : nodes) {
			String nodeName = Type.getObjectType(node.name).getClassName().toString();
			List<MethodNode> methods = node.methods;
			for (MethodNode met : methods) {
				List<String> validClasses = new ArrayList<>();
				getvalidClasses(validClasses, node, met);
				InsnList instructions = met.instructions;
				for (int i = 0; i < instructions.size(); i++) {
					String baddMethodOwner = "";
					AbstractInsnNode insNode = instructions.get(i);
					if (insNode instanceof MethodInsnNode) {
						// do the checking above
						MethodInsnNode metNode = (MethodInsnNode) insNode;
						String ownerType = Type.getObjectType(metNode.owner).getClassName().toString();
						boolean validCall = false;
						for (String valid : validClasses) {
							if (valid.equals(ownerType)) {
								validCall = true;
								break;
							}
						}
						if (!validCall) {
							baddMethodOwner = ownerType;
							Pair p = new Pair(nodeName, baddMethodOwner);
							depsToMakePatterns.add(p);
						}
					}
				}
			}
		}
		List<IUMLNode> patterns = new ArrayList<>();
		// remove duplicates form depsToMakePattens
		for (int i = 0; i < depsToMakePatterns.size(); i++) {
			Pair p1 = depsToMakePatterns.get(i);
			for (int j = i + 1; j < depsToMakePatterns.size(); j++) {
				Pair p2 = depsToMakePatterns.get(j);
				if (p1.to.equals(p2.to) && p1.from.equals(p2.from)) {
					depsToMakePatterns.remove(p2);
					j--;
				}
			}
		}

		for (Pair pair : depsToMakePatterns) {
			IUMLNode fromNode = null;
			IUMLNode toNode = null;
			IUMLDependency badDep = null;
			for (IUMLNode node1 : this.getNodes()) {
				if (pair.from.equals(node1.getClassName())) {
					fromNode = node1;
				} else if (pair.to.equals(node1.getClassName())) {
					toNode = node1;
				}
			}
			if (fromNode != null && toNode != null) {
				for (IUMLDependency dep : fromNode.getDependencies()) {
					if (dep.getTo().equals(toNode.getClassName())) {
						badDep = dep;
						break;
					}
				}
				// if badDep is null then we must create one
				if (badDep == null) {
					CreatesDependency underDep = new CreatesDependency(toNode.getClassName(), fromNode.getClassName(),
							false, false);
					LawDemeterDependency patternDep = new LawDemeterDependency(underDep);
					fromNode.addDependency(patternDep);
				} else {
					LawDemeterDependency patternDep = new LawDemeterDependency(badDep);
					fromNode.removeDependency(badDep);
					fromNode.addDependency(patternDep);
				}
				boolean toPattern = true;
				for (IUMLNode pat : patterns) {
					if (pat.getClassName().equals(fromNode.getClassName())) {
						toPattern = false;
					}
				}
				if (toPattern) {
					IUMLNode patternNode = new LawDemeterPattern(fromNode);
					patterns.add(patternNode);
				}

			}

		}

		this.addPatterns(patterns);

	}

	private void getvalidClasses(List<String> classNames, ClassNode node, MethodNode method) {
		// Simplified Law of Demeter
		// Only call methods on classes that are one of your friends. In a given
		// method m, your friends are:
		// • m’s class and all of its subclasses.
		// • The classes of m’s parameters and their subclasses.
		// • The classes of any objects created/instantiated within m.
		// • The classes and subclasses of fields that belong to m’s class.
		// • The classes associated with static method calls.

		classNames.add("java.lang.Object");

		// m’s class and all of its subclasses
		String cName = Type.getObjectType(node.name).getClassName().toString();
		classNames.add(cName);
		getSubclasses(classNames, cName);
		getAllSuperClasses(classNames, cName);

		// get parameters and their subclasses
		List<ParameterNode> params = method.parameters;
		if (params != null) {
			for (ParameterNode param : params) {
				String type = Type.getObjectType(param.name).getClassName().toString();
				classNames.add(type);
				getSubclasses(classNames, type);
				getAllSuperClasses(classNames, type);
			}
		}

		// get classes insanciated in m
		List<LocalVariableNode> localvars = method.localVariables;
		if (localvars != null) {
			for (LocalVariableNode localvar : localvars) {
				String type = Type.getType(localvar.desc).getClassName().toString();
				classNames.add(type);
				getSubclasses(classNames, type);
				getAllSuperClasses(classNames, type);
			}
		}

		// The classes and subclasses of fields that belong to m’s class.
		List<FieldNode> fields = node.fields;
		if (fields != null) {

			for (FieldNode field : fields) {
				String fieldType = Type.getType(field.desc).getClassName().toString();
				classNames.add(fieldType);
				getSubclasses(classNames, fieldType);
				getAllSuperClasses(classNames, fieldType);
			}
		}
		
	}

	private void getSubclasses(List<String> allNames, String clazz) {
		// gets the subclasses of the clazz
		List<IUMLNode> UMLnodes = this.getNodes();
		for (IUMLNode node : UMLnodes) {
			List<IUMLDependency> deps = node.getDependencies();
			for (IUMLDependency dep : deps) {
				if (dep.getDependencyType().equals(ClassInheritanceDependency.CLASS_TYPE)
						|| dep.getDependencyType().equals(InterfaceImplementDependency.CLASS_TYPE)) {
					if (dep.getTo().equals(clazz)) {
						allNames.add(node.getClassName());
					}
				}
			}
		}
	}

	private void getAllSuperClasses(List<String> allNames, String clazz) {
		List<IUMLNode> UMLnodes = this.getNodes();
		for (IUMLNode node : UMLnodes) {
			if (node.getClassName().equals(clazz)) {
				List<IUMLDependency> deps = node.getDependencies();
				for (IUMLDependency dep : deps) {
					if (dep.getDependencyType().equals(ClassInheritanceDependency.CLASS_TYPE)
							|| dep.getDependencyType().equals(InterfaceImplementDependency.CLASS_TYPE)) {
						allNames.add(dep.getTo());
						getAllSuperClasses(allNames, dep.getTo());
					}
				}
			}
		}
	}

}
