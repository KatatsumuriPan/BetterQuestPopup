package kpan.bq_popup.asm.tf;

import kpan.bq_popup.asm.core.AsmTypes;
import kpan.bq_popup.asm.core.AsmUtil;
import kpan.bq_popup.asm.core.MyAsmNameRemapper.MethodRemap;
import kpan.bq_popup.asm.core.adapters.InjectInstructionsAdapter;
import kpan.bq_popup.asm.core.adapters.Instructions;
import kpan.bq_popup.asm.core.adapters.MixinAccessorAdapter;
import kpan.bq_popup.asm.core.adapters.MyClassVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TF_FontRenderer {

	private static final String TARGET = "net.minecraft.client.gui.FontRenderer";
	private static final String HOOK = AsmTypes.HOOK + "HK_" + "FontRenderer";
	private static final String ACC = AsmTypes.ACC + "ACC_" + "FontRenderer";
	private static final MethodRemap renderChar = new MethodRemap(TARGET, "renderChar", AsmUtil.toMethodDesc(AsmTypes.FLOAT, AsmTypes.CHAR, AsmTypes.BOOL), "func_181559_a");

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (!TARGET.equals(className))
			return cv;
		ClassVisitor newcv = new MyClassVisitor(cv, className) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				if (renderChar.isTarget(name, desc)) {
					mv = InjectInstructionsAdapter.injectFirst(mv, name,
							Instructions.create()
									.aload(0)
									.iload(1)
									.invokeStatic(HOOK, "onRenderChar", AsmUtil.composeRuntimeMethodDesc(AsmTypes.CHAR, TARGET, AsmTypes.CHAR))
									.istore(1)
					);
					success();
				}
				return mv;
			}
		};
		newcv = new MixinAccessorAdapter(newcv, className, ACC);
		return newcv;
	}
}
