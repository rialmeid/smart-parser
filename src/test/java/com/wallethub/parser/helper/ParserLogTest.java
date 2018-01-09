package com.wallethub.parser.helper;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserLogTest {

    @Test
    public void newParserLog_withoutParameters_returnError() {
        try {
            new ParserLog(null);
            fail();
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(IllegalArgumentException.class));
        }

    }
}