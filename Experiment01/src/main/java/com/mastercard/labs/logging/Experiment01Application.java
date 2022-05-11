package com.mastercard.labs.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Experiment01Application {

    public static void main(String[] args) {

        Logger LOG = LogManager.getLogger(Experiment01Application.class.getName());

        LOG.debug("This Will Be Printed On Debug");
        LOG.info("This Will Be Printed On Info");
        LOG.warn("This Will Be Printed On Warn");
        LOG.error("This Will Be Printed On Error");
        LOG.fatal("This Will Be Printed On Fatal");

        LOG.info("Appending string: {}.", "Hello, World");
        LOG.info("Java runtime: ${java:runtime}.");
    }

}
