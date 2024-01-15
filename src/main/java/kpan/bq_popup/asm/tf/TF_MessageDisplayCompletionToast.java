package kpan.bq_popup.asm.tf;

import kpan.bq_popup.asm.core.AsmTypes;
import kpan.bq_popup.asm.core.AsmUtil;
import kpan.bq_popup.asm.core.adapters.ReplaceRefMethodAdapter;
import org.objectweb.asm.ClassVisitor;

public class TF_MessageDisplayCompletionToast {

	private static final String TARGET = "com.feed_the_beast.ftbquests.net.MessageDisplayCompletionToast";
	private static final String HOOK = AsmTypes.HOOK + "HK_" + "MessageDisplayCompletionToast";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (!TARGET.equals(className))
			return cv;
		ClassVisitor newcv = new ReplaceRefMethodAdapter(cv, HOOK, TARGET, "onMessage", AsmUtil.toMethodDesc(AsmTypes.VOID))
				.setSuccessExpectedMin(0).setSuccessExpectedMax(1);
		return newcv;
	}
}
