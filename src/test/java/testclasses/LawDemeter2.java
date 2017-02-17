package testclasses;

public class LawDemeter2 {
	private String s;

	public LawDemeter2() {

	}

	public void badMethod(LottaFeildsClass clazz) {
		// lookup testclasses.LottaFeildClass, java.lang.string, 
		// this shouldn't violate the principle
		this.s = clazz.string;
		int hash = this.s.hashCode();
	}

}
