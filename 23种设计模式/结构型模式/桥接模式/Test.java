package ге╫сдёй╫;

public class Test {
			public static void main(String[] args) {
				Bridge b=new MindBridge();
				Sourcebale source1=new SourceSub1();
				b.setSource(source1);
				b.method();
				
				Sourcebale source2=new SourceSub2();
				b.setSource(source2);
				b.method();
				
			}
}
