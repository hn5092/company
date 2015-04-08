package ����ģʽ;

public class Proxy  implements Sourceable{
	Sourceable source;
	public Proxy(){
		super();
		this.source = new Source(); 
	}
	@Override
	public void method() {
			start();
			source.method();
			finish();
	}
	public void start(){
		System.out.println("start");
	}
	
	public void finish(){
		System.out.println("finish");
	}
}
