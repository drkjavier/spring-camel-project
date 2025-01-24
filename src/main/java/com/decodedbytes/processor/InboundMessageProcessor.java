package com.decodedbytes.processor;

import com.decodedbytes.beans.NameAddress;
import com.decodedbytes.beans.OutboundNameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboundMessageProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(InboundMessageProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        NameAddress nameAddress = exchange.getIn().getBody(NameAddress.class);
        exchange.getIn().setBody(new OutboundNameAddress(nameAddress.getName(), returnOuboundAddress(nameAddress)));
        exchange.getIn().setHeader("consumedId", nameAddress.getId());
        logger.info("Consumed id:d {}", exchange.getIn().getHeader("consumedId"));
    }

    private String returnOuboundAddress(NameAddress nameAddress) {
        StringBuilder concatenatedAddress = new StringBuilder();
        concatenatedAddress.append(nameAddress.getHouseNumber()).append(" ");
        concatenatedAddress.append(nameAddress.getCity()).append(",").append(" ");
        concatenatedAddress.append(nameAddress.getProvince()).append(" ");
        concatenatedAddress.append(nameAddress.getPostalCode());
        return concatenatedAddress.toString();
    }
}
