package kpan.bq_popup.asm.tf;

import kpan.bq_popup.asm.core.AsmTypes;
import kpan.bq_popup.asm.core.AsmUtil;
import kpan.bq_popup.asm.core.adapters.InjectInstructionsAdapter;
import kpan.bq_popup.asm.core.adapters.Instructions;
import kpan.bq_popup.asm.core.adapters.MyClassVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TF_TaskData {

	private static final String TARGET = "com.feed_the_beast.ftbquests.quest.task.TaskData";
	private static final String HOOK = AsmTypes.HOOK + "HK_" + "TaskData";


	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (!TARGET.equals(className))
			return cv;
		ClassVisitor newcv = new MyClassVisitor(cv, className) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				if (name.equals("setProgress")) {
					mv = InjectInstructionsAdapter.after(mv, name
							, Instructions.create()
									.putField(TARGET, "progress", AsmTypes.LONG)
							, Instructions.create()
									.aload(0)
									.invokeStatic(HOOK, "onSetProgress", AsmUtil.composeRuntimeMethodDesc(AsmTypes.VOID, TARGET))
					);
					success();
				}
				return mv;
			}
		};
		return newcv;
	}
}
