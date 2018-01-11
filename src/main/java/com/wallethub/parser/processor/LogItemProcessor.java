package com.wallethub.parser.processor;


import com.wallethub.parser.model.Log;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class LogItemProcessor implements ItemProcessor<Log, Log> {

    @Override
    public Log process(Log log) throws Exception {
        String now = log.getNow();
        String ip = log.getIp();
        String request = log.getRequest();
        int status = log.getStatus();
        String user = log.getUser();
        Log transformedLog = new Log(now, ip, request, status, user);
        return transformedLog;
    }
}
