package в╟йндёй╫;

public class Decorator implements Sourable{
    private Sourable source;
    public Decorator (Sourable source){
    	super();
    	this.source =source;
    }
    public void method(){
    	System.out.println("before Decorator ");
    	source.method();
    	System.out.println("after Decorator");
    }
}
