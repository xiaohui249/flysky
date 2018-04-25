package com.sean.flysky.hystrix;

public class CommandHelloFailureTest {
    @Test
    public void getFallback() throws Exception {
        assertEquals("Hello Failure World!", new CommandHelloFailure("World").execute());
        assertEquals("Hello Failure Bob!", new CommandHelloFailure("Bob").execute());
    }
}