package com.wallethub.parser;

import com.wallethub.parser.helper.ParserLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);

        ParserLog parserLog = new ParserLog(args);



        for (String s: args) {
            System.out.println(s);
        }
    }
}
