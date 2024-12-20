package com.decodedbytes.components;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LegacyFileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:src/data/input?fileName=inputFile.txt")
                .routeId("lecacyFileMoveRoute")
                .to("file:src/data/output?fileName=outputFile.txt");
    }
}
