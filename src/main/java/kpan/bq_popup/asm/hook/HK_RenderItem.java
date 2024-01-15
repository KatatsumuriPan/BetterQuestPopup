package kpan.bq_popup.asm.hook;

public class HK_RenderItem {

	public static float alpha = 1f;

	public static int getColor() {
		int a = (int) (alpha * 255);
		return (a << 24) | 0x00ffffff;
	}
}
