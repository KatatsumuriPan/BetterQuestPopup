package kpan.bq_popup.asm.hook;

import com.feed_the_beast.ftbquests.net.MessageDisplayCompletionToast;

public class HK_MessageDisplayCompletionToast {

	public static void onMessage(MessageDisplayCompletionToast self) {
		//ここでToast表示をするのではなく、setProgress内で行うようにした
	}
}
