package 对象适配器模式;

public class Wrapper implements Targetable {
	private Source source;

	public Wrapper(Source source) {
		super();
		this.source = source;

	}

	@Override
	public void method1() {
		source.method1();
	}

	@Override
	public void method2() {

	}
}
