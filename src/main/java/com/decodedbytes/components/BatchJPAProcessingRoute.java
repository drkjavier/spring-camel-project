package com.decodedbytes.components;

import com.decodedbytes.processor.InboundMessageProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BatchJPAProcessingRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:readDB?period=10000")
                .routeId("readBDId")
                .to("jpa:com.decodedbytes.beans.NameAddress?namedQuery=fetchAllRows")
                .split(body())
                .log("Fetched records from DB: ${body}")
                    .process(new InboundMessageProcessor())
                .log(LoggingLevel.INFO, "Trnasformed headers: ${headers}")
                    .log(LoggingLevel.INFO, "Transformed body: ${body}")
                    .convertBodyTo(String.class)
                    .to("file:src/data/output?fileName=outputFile.csv&fileExist=append&appendChars=\\n")
                    .toD("jpa:com.decodedbytes.beans.NameAddress?nativeQuery=DELETE FROM NAME_ADDRESS WHERE id = ${header.consumedId}&useExecuteUpdate=true")
                //.log("Removed records from DB: ${body}")
                .end()
        ;
    }
}
