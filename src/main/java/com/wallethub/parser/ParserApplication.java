package com.wallethub.parser;

import com.wallethub.parser.helper.ParserLog;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class ParserApplication {

    public static void main(String[] args) {

        args = new String[]{
                "--accesslog=/path/to/file",
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                "--threshold=100"
        };

        try {
            ParserLog parserLog = new ParserLog(args);
            Map<String, String> maps = parserLog.getMaps();

            Set<String> keys = maps.keySet();
            for (String key : keys) {
                String value = maps.get(key);
                System.out.println(MessageFormat.format("Key: {0} Value: {1}", new Object[]{key, value}));
            }
        } catch (Exception e) {
        }

        SpringApplication.run(ParserApplication.class, args);
    }
}
