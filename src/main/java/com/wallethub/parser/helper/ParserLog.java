package com.wallethub.parser.helper;

import java.util.HashMap;
import java.util.Map;

public class ParserLog {

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
        String key;
        String value;

        for (String arg : args) {
            putArgsInMaps(arg);
        }

    }

    protected void putArgsInMaps(String arg) {
        String[] split = arg.split("=");
        if (split.length == 2) {
            String key = split[0].replace("--", "");
            String value = split[1];
            maps.put(key, value);
        }
    }

    public String[] getArgs() {
        return args;
    }

    public Map<String, String> getMaps() {
        return maps;
    }
}
