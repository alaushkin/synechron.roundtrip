package com.synechron.roundtrip;

import com.synechron.roundtrip.catcher.Catcher;
import com.synechron.roundtrip.exception.SynechronPropertyException;
import com.synechron.roundtrip.pitcher.Pitcher;
import com.synechron.roundtrip.pitcher.PitcherProcessor;
import com.synechron.roundtrip.pitcher.generator.*;
import com.synechron.roundtrip.utils.CommandLineHelper;
import com.synechron.roundtrip.utils.PropertyHelper;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.synechron.roundtrip.utils.CommandLineHelper.*;

public class TCPPing {

    private final static Logger logger = Logger.getLogger(TCPPing.class);

    public static void main(String[] args) throws ParseException {

        try {
            PropertyHelper.loadProperties("application.properties");

            CommandLineHelper cmdHelper = new CommandLineHelper(args);

            if (cmdHelper.hasOption(PITCHER_MODE)) {

                int size = cmdHelper.getIntOption(SIZE);
                if (size == 0) {
                    size = PropertyHelper.getIntProperty("default.message.size");
                }

                MessageGenerator messageGenerator;
                switch (GeneratorType.valueOf(System.getProperty("generator.type"))) {
                    case INFO:
                        messageGenerator = new PackageInfoGenerator();
                        break;
                    case PACKAGE:
                        messageGenerator = new SystemInfoGenerator();
                        break;
                    case RANDOM:
                        messageGenerator = new RandomGenerator();
                        break;
                    default:
                        messageGenerator = new RandomGenerator();
                }

                Pitcher pitcher = new Pitcher(
                        messageGenerator,
                        cmdHelper.getOption(HOST_NAME),
                        cmdHelper.getIntOption(PORT),
                        cmdHelper.getIntOption(MPS),
                        size
                );
                pitcher.startSending();
            } else {
                new Catcher(
                        cmdHelper.getIntOption(PORT),
                        cmdHelper.getOption(BIND)
                ).startListening();
            }

        } catch (SynechronPropertyException e) {
            logger.error(e.getMessage());
        }
    }

}
