package sml;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubInstructionTest {

    @Test
    void execute() {
        Machine machine = new Machine();

        ArrayList<Instruction> prog = new ArrayList<>();
        prog.add(new LinInstruction("L1", 0, 88));
        prog.add(new LinInstruction("L2", 1, 9));
        prog.add(new SubInstruction("L3", 2, 0, 1));

        machine.setProg(prog);
        machine.execute();

        assertEquals(79, machine.getRegisters().getRegister(2));
    }
}