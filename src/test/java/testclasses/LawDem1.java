package testclasses;

public class LawDem1 {	
	
	public LawDem1() {
	}
	
	public void specailMethod(LottaFeildsClass c) {
		// lookup testclasses.LottaFeildClass, java.lang.string
		// this violates the principle
		c.string.hashCode();
	}

}
