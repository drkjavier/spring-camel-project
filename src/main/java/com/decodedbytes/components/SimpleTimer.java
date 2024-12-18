package com.decodedbytes.components;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.INFO;

@Component
public class SimpleTimer extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:simpletimer?period=2000")
                .routeId("simpleTimerId")
                .setBody(constant("Hello World"))
                .log(INFO, "${body}")
            ;
    }
}
