package ����������ģʽ;

public class AdaptTest {
	public static void main(String[] args) {
		// ��һ��Source�࣬ӵ��һ�������������䣬Ŀ��ӿ�ʱTargetable��ͨ��Adapter�࣬��Source�Ĺ�����չ��Targetable��
		Targetable t = new Adapter();
		t.method1();
		t.method2();

		// �����������
		Source source = new Source();
		Targetable t2 = new Wrapper(source);
		t2.method1();
		t2.method2();
	}
}
	