package com.decodedbytes.components;

import com.decodedbytes.beans.InboundRestProcessingBean;
import com.decodedbytes.beans.NameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.management.JMException;
import java.net.ConnectException;

@Component
public class NewRestRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        Predicate isCityAjax = header("userCity").isEqualTo("Ajax");

        onException(JMException.class, ConnectException.class)
                .routeId("jmsExceptionRouteId")
                        .handled(true)
                                .log(LoggingLevel.INFO, "JMS Exception has ocurred; handling gracefully");

        restConfiguration()
                .component("jetty")
                .host("0.0.0.0")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("masterclass")
                .produces("application/json")
                .post("nameAddress").type(NameAddress.class).route()
                .routeId("newRestRouteId")
                .log(LoggingLevel.INFO, "${body}")
                //.convertBodyTo(String.class)
                //.to("file:src/main/resources/output?fileName=outputFile.csv&fileExist=append&appendChars=\\n");
                //.multicast()
                //.to("jpa:com.decodedbytes.beans.NameAddress")
                //.to("activemq:queue:nameaddressqueue?exchangePattern=InOnly")
                .bean(new InboundRestProcessingBean())

                // Setup Rule
                // If City = AJAX -> send to MQ
                // Else Send to both DB and MQ
                .choice()
                //.when(simple("${header.userCity} == 'Ajax'"))
                .when(isCityAjax)
                .to("direct:toActiveMQ")
                .otherwise()
                .to("direct:toDB")
                .to("direct:toActiveMQ")
                .end()

                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .transform().simple("Message Processed and Result Generated with Bodu: ${body}")
                .endRest();



        from("direct:toDB")
                .routeId("toDBId")
                .to("jpa:com.decodedbytes.beans.NameAddress");

        from("direct:toActiveMQ")
                .routeId("toActiveMQId")
                .to("activemq:queue:nameaddressqueue?exchangePattern=InOnly")
        ;
    }
}