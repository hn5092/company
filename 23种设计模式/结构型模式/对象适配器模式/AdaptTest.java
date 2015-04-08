package 对象适配器模式;

public class AdaptTest {
	public static void main(String[] args) {
		// 有一个Source类，拥有一个方法，待适配，目标接口时Targetable，通过Adapter类，将Source的功能扩展到Targetable里
		Targetable t = new Adapter();
		t.method1();
		t.method2();

		// 测试适配对象
		Source source = new Source();
		Targetable t2 = new Wrapper(source);
		t2.method1();
		t2.method2();
	}
}
	