package net.runelite.asm.attributes.code.instructions;

import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.InstructionType;
import net.runelite.asm.attributes.code.Instructions;
import net.runelite.asm.attributes.code.instruction.types.LVTInstruction;
import net.runelite.asm.execution.Frame;
import net.runelite.asm.execution.InstructionContext;
import net.runelite.asm.execution.Stack;
import net.runelite.asm.execution.StackContext;
import net.runelite.asm.execution.VariableContext;
import net.runelite.asm.execution.Variables;


public class ALoad_0 extends Instruction implements LVTInstruction
{
	public ALoad_0(Instructions instructions, InstructionType type, int pc)
	{
		super(instructions, type, pc);
	}

	public ALoad_0(Instructions instructions)
	{
		super(instructions, InstructionType.ALOAD_0, -1);
	}

	@Override
	public InstructionContext execute(Frame frame)
	{
		InstructionContext ins = new InstructionContext(this, frame);
		Stack stack = frame.getStack();
		Variables var = frame.getVariables();
		
		VariableContext vctx = var.get(0);
		ins.read(vctx);
		
		StackContext ctx = new StackContext(ins, vctx);
		stack.push(ctx);
		
		ins.push(ctx);
		
		return ins;
	}

	@Override
	public int getVariableIndex()
	{
		return 0;
	}

	@Override
	public boolean store()
	{
		return false;
	}

	@Override
	public Instruction setVariableIndex(int idx)
	{
		return new ALoad(this.getInstructions(), idx);
	}
	
	@Override
	public Instruction makeGeneric()
	{
		return new ALoad(this.getInstructions(), 0);
	}
}