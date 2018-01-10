package com.wallethub.parser.helper;

import java.util.HashMap;
import java.util.Map;

public class ParserLog {

    public static final String DELIMITER_ARGS = "=";
    public static final String INDICATOR_ARGS = "--";
    public static final String EMPTY_STRING = "";
    public static final int INDEX_KEY_ARGS = 0;
    public static final int INDEX_VALUE_ARGS = 1;
    public static final int MAXSIZE_ARGS = 2;

    private final String[] args;

    private Map<String, String> maps;

    public ParserLog(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException();
        }
        if (args.length == 0) {
            throw new IllegalArgumentException();
        }
        this.args = args;
        initMap();
    }

    protected void initMap() {
        maps = new HashMap<String, String>();
        for (String arg : args) {
            putArgsInMaps(arg);
        }
    }

    protected void putArgsInMaps(String arg) {
        if (arg != null) {
            String[] split = arg.split(DELIMITER_ARGS);
            if (split.length == MAXSIZE_ARGS) {
                String key = split[INDEX_KEY_ARGS].replace(INDICATOR_ARGS, EMPTY_STRING);
                String value = split[INDEX_VALUE_ARGS];
                maps.put(key, value);
            }
        }
    }

    public String[] getArgs() {
        return args;
    }

    public Map<String, String> getMaps() {
        return maps;
    }
}
