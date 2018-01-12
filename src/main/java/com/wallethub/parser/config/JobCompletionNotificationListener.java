package com.wallethub.parser.config;

import com.wallethub.parser.model.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    private Map<String, JobParameter> parameters;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        parameters = jobParameters.getParameters();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            List<Log> results = jdbcTemplate.query("SELECT " +
                    " dt_now, " +
                    " ds_ip, " +
                    " ds_request, " +
                    " cd_status, " +
                    " ds_user " +
                    "FROM TB_LOG", new RowMapper<Log>() {
                @Override
                public Log mapRow(ResultSet rs, int row) throws SQLException {
                    String now = rs.getString("dt_now");
                    String ip = rs.getString("ds_ip");
                    String request = rs.getString("ds_request");
                    int status = rs.getInt("cd_status");
                    String user = rs.getString("ds_user");
                    return new Log(now, ip, request, status, user);
                }
            });

            for (Log log : results) {
                JobCompletionNotificationListener.log.info("Found <" + log + "> in the database.");
            }
        }
    }

    public Map<String, JobParameter> getParameters() {
        return parameters;
    }
}
