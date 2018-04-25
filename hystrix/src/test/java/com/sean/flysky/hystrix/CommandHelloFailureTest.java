package com.sean.flysky.hystrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandHelloFailureTest {
    @Test
    public void getFallback() throws Exception {
        assertEquals("Hello Failure World!", new CommandHelloFailure("World").execute());
        assertEquals("Hello Failure Bob!", new CommandHelloFailure("Bob").execute());
    }
}