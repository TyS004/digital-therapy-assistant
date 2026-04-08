package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class CLISessionTest {

    @Test
    void loginLogoutAndAccessors() {
        CLISession s = new CLISession();
        assertFalse(s.isLoggedIn());
        assertNull(s.getToken());
        assertNull(s.getUserID());
        assertNull(s.getEmail());

        UUID uid = UUID.randomUUID();
        s.login("e@e.com", uid, "tok");
        assertTrue(s.isLoggedIn());
        assertEquals("tok", s.getToken());
        assertEquals(uid, s.getUserID());
        assertEquals("e@e.com", s.getEmail());

        s.setToken("t2");
        assertEquals("t2", s.getToken());
        s.setUserID(uid);
        s.setEmail("x@x.com");
        assertEquals("x@x.com", s.getEmail());

        s.logout();
        assertFalse(s.isLoggedIn());
        assertNull(s.getToken());
    }
}
