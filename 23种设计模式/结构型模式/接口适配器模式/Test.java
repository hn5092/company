package 接口适配器模式;

public class Test {
			public static void main(String[] args) {
				Wrapper w=new SourceSub1();
				Wrapper w2=new SourceSub2();
				w.method1();
				w2.method2();
				
			}
}
