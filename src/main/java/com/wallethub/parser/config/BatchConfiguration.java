package com.wallethub.parser.config;

import com.wallethub.parser.model.Log;
import com.wallethub.parser.processor.LogItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        JobBuilder importUserJob = jobBuilderFactory.get("importUserJob");

        Job job = importUserJob
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();

        Map<String, JobParameter> parameters = listener.getParameters();

        if (parameters != null) {
            for (String key : parameters.keySet()) {
                System.out.println(">>>" + " Key: " + key + " Value: " + parameters.get(key) + "<<<");
            }
        }
        return job;
    }

    @Bean
    public Step step1() {
        StepBuilder step = stepBuilderFactory.get("step1");

        TaskletStep taskletStep = step
                .<Log, Log>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

        return taskletStep;
    }



    @Bean
    public FlatFileItemReader<Log> reader() {
        String[] names = {"now", "ip", "request", "status", "user"};

        LineMapper<Log> lineMapper = new DefaultLineMapper<Log>() {
            {
                DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("|") {
                    {
                        setNames(names);
                    }
                };
                BeanWrapperFieldSetMapper<Log> fieldSetMapper = new BeanWrapperFieldSetMapper<Log>() {
                    {
                        setTargetType(Log.class);
                    }
                };
                setLineTokenizer(tokenizer);
                setFieldSetMapper(fieldSetMapper);
            }
        };

        String path = "sample-data.csv";
        path = "access.log";
        path = "C:\\projetos-git\\repo-wallet\\smart-parser\\docs\\access.log";

        Resource resource = new FileSystemResource(path);
        FlatFileItemReader<Log> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public LogItemProcessor processor() {
        return new LogItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Log> writer() {
        String sql = "INSERT INTO tb_log (dt_now, ds_ip, ds_request, cd_status, ds_user) VALUES (:now, :ip, :request, :status, :user)";
        JdbcBatchItemWriter<Log> writer = new JdbcBatchItemWriter<Log>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Log>());
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }
}
