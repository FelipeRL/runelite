package net.runelite.deob.deobfuscators.arithmetic;

import java.util.List;
import net.runelite.asm.ClassGroup;
import net.runelite.deob.Deobfuscator;
import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.Instructions;
import net.runelite.asm.attributes.code.instruction.types.PushConstantInstruction;
import net.runelite.asm.attributes.code.instructions.IMul;
import net.runelite.asm.attributes.code.instructions.LMul;
import net.runelite.asm.attributes.code.instructions.NOP;
import net.runelite.asm.execution.Execution;
import net.runelite.asm.execution.InstructionContext;
import net.runelite.asm.execution.MethodContext;
import net.runelite.asm.execution.StackContext;

public class MultiplyOneDeobfuscator implements Deobfuscator
{
	private int count;

	private void visit(MethodContext mctx)
	{
		for (InstructionContext ictx : mctx.getInstructionContexts())
		{
			Instruction instruction = ictx.getInstruction();

			if (!(instruction instanceof IMul) && !(instruction instanceof LMul))
			{
				continue;
			}

			Instructions ins = ictx.getInstruction().getInstructions();
			if (ins == null)
			{
				continue;
			}

			List<Instruction> ilist = ins.getInstructions();

			if (!ilist.contains(ictx.getInstruction()))
			{
				continue; // already done
			}
			StackContext one = ictx.getPops().get(0);
			StackContext two = ictx.getPops().get(1);

			int removeIdx = -1;
			if (one.getPushed().getInstruction() instanceof PushConstantInstruction
				&& DMath.equals((Number) ((PushConstantInstruction) one.getPushed().getInstruction()).getConstant().getObject(), 1))
			{
				removeIdx = 0;
			}
			else if (two.getPushed().getInstruction() instanceof PushConstantInstruction
				&& DMath.equals((Number) ((PushConstantInstruction) two.getPushed().getInstruction()).getConstant().getObject(), 1))
			{
				removeIdx = 1;
			}

			if (removeIdx == -1)
			{
				continue;
			}

			if (!MultiplicationDeobfuscator.isOnlyPath(ictx, removeIdx == 0 ? one : two))
			{
				continue;
			}

			ictx.removeStack(removeIdx);
			ins.replace(ictx.getInstruction(), new NOP(ins));

			++count;
		}
	}

	@Override
	public void run(ClassGroup group)
	{
		Execution e = new Execution(group);
		e.addMethodContextVisitor(i -> visit(i));
		e.populateInitialMethods();
		e.run();

		System.out.println("Removed " + count + " 1 multiplications");
	}

}