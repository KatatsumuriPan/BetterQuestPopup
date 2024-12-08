package kpan.bq_popup.asm.tf;

import kpan.bq_popup.asm.core.AsmTypes;
import kpan.bq_popup.asm.core.AsmUtil;
import kpan.bq_popup.asm.core.MyAsmNameRemapper.MethodRemap;
import kpan.bq_popup.asm.core.adapters.Instructions;
import kpan.bq_popup.asm.core.adapters.Instructions.OpcodeInt;
import kpan.bq_popup.asm.core.adapters.MyClassVisitor;
import kpan.bq_popup.asm.core.adapters.ReplaceInstructionsAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_RenderItem {

	private static final String TARGET = "net.minecraft.client.renderer.RenderItem";
	private static final String HOOK = AsmTypes.HOOK + "HK_" + "RenderItem";
	private static final String IBAKEDMODEL = "net.minecraft.client.renderer.block.model.IBakedModel";
	private static final String BUFFERBUILDER = "net.minecraft.client.renderer.BufferBuilder";

	private static final MethodRemap renderItem = new MethodRemap(TARGET, "renderItem", AsmUtil.toMethodDesc(AsmTypes.VOID, AsmTypes.ITEMSTACK, IBAKEDMODEL), "func_180454_a");
	private static final MethodRemap renderModel = new MethodRemap(TARGET, "renderModel", AsmUtil.toMethodDesc(AsmTypes.VOID, IBAKEDMODEL, AsmTypes.ITEMSTACK), "func_191961_a");
	private static final MethodRemap renderModel2 = new MethodRemap(TARGET, "renderModel", AsmUtil.toMethodDesc(AsmTypes.VOID, IBAKEDMODEL, AsmTypes.INT, AsmTypes.ITEMSTACK), "func_191967_a");
	private static final MethodRemap renderQuads = new MethodRemap(TARGET, "renderQuads", AsmUtil.toMethodDesc(AsmTypes.VOID,BUFFERBUILDER, AsmTypes.LIST, AsmTypes.INT, AsmTypes.ITEMSTACK), "func_191970_a");

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (!TARGET.equals(className))
			return cv;
		ClassVisitor newcv = new MyClassVisitor(cv, className) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				if (renderItem.isTarget(name, desc)) {
					mv = new ReplaceInstructionsAdapter(mv, name + "(virtual)"
							, Instructions.create()
							.invokeVirtual(renderModel)
							, Instructions.create()
							.invokeStatic(HOOK, "getColor", AsmUtil.composeRuntimeMethodDesc(AsmTypes.INT))
							.insn(Opcodes.SWAP)
							.invokespecial(renderModel2)
					).setSuccessExpectedMin(0).setSuccessExpectedMax(1);
					mv = new ReplaceInstructionsAdapter(mv, name + "(special)"
							, Instructions.create()
							.invokespecial(renderModel)
							, Instructions.create()
							.invokeStatic(HOOK, "getColor", AsmUtil.composeRuntimeMethodDesc(AsmTypes.INT))
							.insn(Opcodes.SWAP)
							.invokespecial(renderModel2)
					).setSuccessExpectedMin(0).setSuccessExpectedMax(2);//optifineがあると2つ
					success();
				}
				if(renderQuads.isTarget(name,desc)){
					mv = new ReplaceInstructionsAdapter(mv, name
							, Instructions.create()
							.iload(3)
							.insn(Opcodes.ICONST_M1)
							, Instructions.create()
							.iload(3)
							.ldcInsn(0x00FF_FFFF)
							.insn(Opcodes.IAND)
							.ldcInsn(0x00FF_FFFF)
					);
					mv = new ReplaceInstructionsAdapter(mv, name
							, Instructions.create()
							.ldcInsn(0xFF00_0000)
							.insn(Opcodes.IOR)
							, Instructions.create()
							//アルファ部分を削除
							.ldcInsn(0x00FF_FFFF)
							.insn(Opcodes.IAND)
							//colorのアルファ
							.iload(3)
							.intInsn(OpcodeInt.BIPUSH,24)
							.insn(Opcodes.IUSHR)
							//kのアルファ
							.iload(9)
							.intInsn(OpcodeInt.BIPUSH,24)
							.insn(Opcodes.IUSHR)
							//colorとkの掛け算
							.insn(Opcodes.IMUL)
							//正規化兼0xFF00_0000部分へ移動
							.ldcInsn(65794)
							.insn(Opcodes.IMUL)
							.ldcInsn(0xFF00_0000)
							.insn(Opcodes.IAND)
							.insn(Opcodes.IOR)
					);
					success();
				}
				return mv;
			}
		}.setSuccessExpected(2);
		return newcv;
	}
}
