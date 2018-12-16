package com.sacom.order;

import com.sacom.order.common.LifeCycleException;
import com.sacom.order.dispatcher.DispatcherException;
import com.sacom.order.dispatcher.filesystem.Dispatcher;
import com.sacom.order.dispatcher.filesystem.DispatcherSettings;
import com.sacom.order.processing.ProcessingSettings;
import com.sacom.order.processing.XMLOrderProcessing;
import com.sacom.order.receiver.ReceiverException;
import com.sacom.order.receiver.filesystem.Receiver;
import com.sacom.order.common.LifeCycle;
import com.sacom.order.receiver.filesystem.ReceiverSettings;

import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {
  private static Options constructOptions() {
    Options options = new Options();

    // input
    final Option input = Option.builder("i").longOpt("input").hasArg().argName("dir").desc("input directory").required().build();
    options.addOption(input);

    // procesing
    final Option processingClean = Option.builder("pc").hasArg(false).desc("clean up processed order files").longOpt("processing-clean").build();
    options.addOption(processingClean);

    // output
    final Option output = Option.builder("o").longOpt("output").hasArg().argName("dir").desc("output directory").required().build();
    options.addOption(output);

    // help
    final Option help = Option.builder("h").longOpt("help").desc("print this message").build();
    options.addOption(help);

    return options;
  }

  private static void displayHelp(final Options _options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("order", _options);
  }

  private static CommandLine parseCommandLine(final Options _options, String[] _args) {
    final CommandLineParser parser = new DefaultParser();
    try {
      final CommandLine commandLine = parser.parse(_options, _args);

      if (commandLine.hasOption("h")) {
        displayHelp(_options);
        System.exit(0);
      }

      return commandLine;
    } catch (ParseException e) {
      System.err.println("Unable to parse options: " + e.getMessage());
      displayHelp(_options);
      System.exit(-1);
    }
    return null;
  }

  private static ReceiverSettings constructReceiverSettings(final CommandLine _commandLine) {
    try {
      final String inputDirectory = _commandLine.getOptionValue("i");
      final ReceiverSettings receiverSettings = new ReceiverSettings(inputDirectory);
      return receiverSettings;
    } catch (ReceiverException _ex) {
      System.err.println("ERROR: invalid receiver settings: " + _ex.toString());
    }
    return null;
  }

  private static ProcessingSettings constructProcessingSettings(final CommandLine _commandLine) {
    final boolean processingClean = _commandLine.hasOption("processing-clean");
    final ProcessingSettings processingSettings = new ProcessingSettings("orders.xsd", processingClean);
    return processingSettings;
  }

  private static DispatcherSettings constructDispatherSettings(final CommandLine _commandLine) {
    try {
      final String outputDirectory = _commandLine.getOptionValue("output");
      final DispatcherSettings dispatcherSettings = new DispatcherSettings(outputDirectory);
      return dispatcherSettings;
    } catch (DispatcherException e) {
      System.err.println("ERROR: invalid dispatcher settings: " + e.toString());
    }
    return null;
  }

  public static void main(String[] args) {
    final Options options = constructOptions();
    final CommandLine commandLine = parseCommandLine(options, args);

    //System.out.println("working dir = " + System.getProperty("user.dir"));

    // construct settings
    //
    final ReceiverSettings receiverSettings = constructReceiverSettings(commandLine);
    final ProcessingSettings processingSettings = constructProcessingSettings(commandLine);
    final DispatcherSettings dispatcherSettings = constructDispatherSettings(commandLine);

    //
    // construct the pipeline backwards
    //
    Dispatcher orderDispatcher = null;
    if (dispatcherSettings != null) {
      orderDispatcher = new Dispatcher(dispatcherSettings);
      orderDispatcher.start();
    }

    XMLOrderProcessing orderProcessing = null;
    if (orderDispatcher != null) {
      try {
        orderProcessing = new XMLOrderProcessing(processingSettings, orderDispatcher);
        orderProcessing.start();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: cannot instantiate processing: " + _ex.toString());
        orderProcessing = null;
      }
    }

    LifeCycle orderReceiver = null;
    if (receiverSettings != null && orderProcessing != null) {
      try {
        orderReceiver = new Receiver(receiverSettings, orderProcessing);
        orderReceiver.start();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: cannot start order receiver: " + _ex.toString());
        orderReceiver = null;
      }
    }

    //
    // TODO: rudimentary wait for execution
    //
    try {
      System.out.println("Monitoring/Processing/Dispatching loop running. Hit 'Return' to stop..");
      System.in.read();
    } catch (IOException _ex) {
      // nop
    }

    //
    // tear down the pipeline forwards, starting from the input
    //
    if (orderReceiver != null) {
      try {
        orderReceiver.stop();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: failed to stop the receiver: " + _ex.toString());
      } finally {
        orderReceiver = null;
      }
    }

    if (orderProcessing != null) {
      try {
        orderProcessing.stop();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: failed to stop the processing: " + _ex.toString());
      } finally {
        orderProcessing = null;
      }
    }

    if (orderDispatcher != null) {
      try {
        orderDispatcher.stop();
      } catch (LifeCycleException _ex) {
        System.err.println("ERROR: failed to stop the dispatcher: " + _ex.toString());
      } finally {
        orderDispatcher = null;
      }
    }

    System.exit(0);
  }
}
