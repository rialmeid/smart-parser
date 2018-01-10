package com.wallethub.parser.helper;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ParserLogTest {

    private String[] args;
    private ParserLog parserLog;

    @Before
    public void setUp() throws Exception {
        args = new String[]{
                "--accessLog=C:/apps/Git/path/to/file",
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                "--threshold=100"
        };
        parserLog = new ParserLog(args);
    }

    @Test
    public void newParserLog_withoutParameters_returnError() {
        try {
            new ParserLog(null);
            fail();
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(IllegalArgumentException.class));
        }

    }

    @Test
    public void newParserLog_withParametersEmpty_returnError() {
        try {
            new ParserLog(new String[]{});
            fail();
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void newParserLog_withParameters_setArgs() {
        assertThat(parserLog.getArgs().length, Matchers.equalTo(4));
    }

    @Test
    public void newParserLog_withParameters_xxxx() {
        parserLog.initMap();
        String accessLogValue = parserLog.getMaps().get("accessLog");
        assertThat(accessLogValue, Matchers.equalTo("C:/apps/Git/path/to/file"));
    }

    @Test
    public void putArgsInMaps_withStringFull_putInMaps() {
        parserLog.putArgsInMaps("--accessLog=C:/apps/Git/path/to/file");
        String value = parserLog.getMaps().get("accessLog");
        assertThat(value, Matchers.equalTo("C:/apps/Git/path/to/file"));
    }

    @Test
    public void putArgsInMaps_withParametersInvalid_notInputMap() {
        Map<String, String> maps = parserLog.getMaps();
        int sizeBefore = maps.size();
        parserLog.putArgsInMaps("=");
        int sizeAfter = maps.size();
        assertThat(sizeBefore, Matchers.equalTo(sizeAfter));
    }
}