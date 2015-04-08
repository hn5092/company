package ге╫сдёй╫;

public  abstract class Bridge {
			Sourcebale source;
			public void method(){
				source.method();
				System.out.println("this is bridge method");
			}
			public Sourcebale getSource() {
				return source;
			}
			public void setSource(Sourcebale source) {
				this.source = source;
			}
			public abstract void go();
		
}
