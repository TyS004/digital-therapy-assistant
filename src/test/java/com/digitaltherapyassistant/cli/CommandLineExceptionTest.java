package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CommandLineExceptionTest {

    @Test
    void reasonGetterSetter() {
        CommandLineException ex = new CommandLineException("bad");
        assertEquals("bad", ex.getReason());
        ex.setReason("fixed");
        assertEquals("fixed", ex.getReason());
    }
}
