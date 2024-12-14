package kpan.bq_popup.asm.hook;

public class HK_RenderItem {

    public static float alpha = 1f;

    public static int getColor() {
        int a = (int) (alpha * 255);
        return (a << 24) | 0x00ffffff;
    }

    public static int applyAlpha(int modifiedColor, int originalColor) {
        return modifiedColor & 0x00FF_FFFF | (originalColor & 0xFF00_0000);
    }
}
