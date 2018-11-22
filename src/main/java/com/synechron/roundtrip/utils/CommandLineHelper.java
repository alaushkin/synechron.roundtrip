package com.synechron.roundtrip.utils;

import com.synechron.roundtrip.exception.SynechronPropertyException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class CommandLineHelper {

    public static String PITCHER_MODE = "p";
    public static String CATCHER_MODE = "c";
    public static String PORT = "port";
    public static String BIND = "bind";
    public static String MPS = "mps";
    public static String SIZE = "size";
    public static String HOST_NAME = "hostname";

    public static String PITCHER_MODE_DESC = "Pitcher mode";
    public static String CATCHER_MODE_DESC = "Catcher mode";
    public static String PORT_DESC = "[Pitcher] TCP socket port used for connecting " +
            "[Catcher] TCP socket port used for listening";
    public static String BIND_DESC = "[Catcher] TCP socket bind address that will be used to run listen";
    public static String MPS_DESC = "[Pitcher] the speed of message sending expressed " +
            "as „messages per second. Default: 1";
    public static String SIZE_DESC = "[Pitcher] message length Minimum: 50\n Maximum: 3000 Default: 300";
    public static String HOST_NAME_DESC = "[Pitcher] the name of the computer which runs Catcher";

    private static String IP_REGEX = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static String IS_MISSING_ERROR = "Option %opt% is missing";
    private static String NOT_NUMERIC_ERROR = "Option %opt% is not numeric";
    private static String WRONG_VALUE_ERROR = "Option %opt% has wrong value";
    private static String INVALID_MODE = "Invalid mode";
    private static String OPTION_PATTERN = "%opt%";

    private static int MIN_SIZE;
    private static int MAX_SIZE;

    private CommandLine cmd;

    public CommandLineHelper(String[] args) throws ParseException, SynechronPropertyException {
        MIN_SIZE = PropertyHelper.getIntProperty("min.message.size");
        MAX_SIZE = PropertyHelper.getIntProperty("max.message.size");
        CommandLineParser parser = new DefaultParser();
        cmd = parser.parse(initOptions(), args);
        validateOptions(cmd);
    }

    private Options initOptions() {
        Options options = new Options();
        options.addOption(PITCHER_MODE, false, PITCHER_MODE_DESC);
        options.addOption(CATCHER_MODE, false, CATCHER_MODE_DESC);
        options.addOption(PORT, true, PORT_DESC);
        options.addOption(BIND, true, BIND_DESC);
        options.addOption(MPS, true, MPS_DESC);
        options.addOption(SIZE, true, SIZE_DESC);
        options.addOption("", true, HOST_NAME_DESC);
        return options;
    }

    private void validateOptions(CommandLine cmd) throws SynechronPropertyException {
        boolean pMode = cmd.hasOption(PITCHER_MODE);
        boolean cMode = cmd.hasOption(CATCHER_MODE);

        if (pMode == cMode)
            throw new SynechronPropertyException(INVALID_MODE);

        if (!cmd.hasOption(PORT))
            throw new SynechronPropertyException(makeErrorMsg(IS_MISSING_ERROR, PORT));

        if (pMode) {
            if (!cmd.hasOption(MPS))
                throw new SynechronPropertyException(makeErrorMsg(IS_MISSING_ERROR, MPS));

            String sizeStr = cmd.getOptionValue(SIZE);
            if (sizeStr != null && !"".equals(sizeStr)) {
                if (!StringUtils.isNumeric(cmd.getOptionValue(SIZE)))
                    throw new SynechronPropertyException(makeErrorMsg(NOT_NUMERIC_ERROR, SIZE));
                int size = Integer.valueOf(sizeStr);
                if (size != 0 && (size < MIN_SIZE || size > MAX_SIZE))
                    throw new SynechronPropertyException(makeErrorMsg(WRONG_VALUE_ERROR, SIZE));
            }

            String[] args = cmd.getArgs();
            if (args.length == 0)
                throw new SynechronPropertyException(makeErrorMsg(IS_MISSING_ERROR, HOST_NAME));

            if(!args[0].matches(IP_REGEX))
                throw new SynechronPropertyException(makeErrorMsg(WRONG_VALUE_ERROR, HOST_NAME));

        } else {
            if (!cmd.hasOption(BIND))
                throw new SynechronPropertyException(makeErrorMsg(IS_MISSING_ERROR, BIND));

            if (!cmd.getOptionValue(BIND).matches(IP_REGEX))
                throw new SynechronPropertyException(makeErrorMsg(WRONG_VALUE_ERROR, BIND));
        }
    }

    public String getOption(String name) {
        if (HOST_NAME.equals(name)) {
            return cmd.getArgs()[0];
        }
        return cmd.getOptionValue(name);
    }

    public Integer getIntOption(String name) throws SynechronPropertyException {
        String option = getOption(name);
        if (option == null || "".equals(option))
            return 0;
        if (!StringUtils.isNumeric(option)) {
            throw new SynechronPropertyException(makeErrorMsg(NOT_NUMERIC_ERROR, name));
        }
        return Integer.valueOf(option);
    }

    private String makeErrorMsg(String text, String name) {
        return text.replace(OPTION_PATTERN, name);
    }

    public boolean hasOption(String name) {
        return cmd.hasOption(name);
    }

}
