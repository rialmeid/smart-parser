package com.wallethub.parser.config;

import com.wallethub.parser.model.Log;
import com.wallethub.parser.processor.LogItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class LogBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<Log> reader() {
        String path = "sample-data.csv";
        path = "access.log";
        Resource resource = new ClassPathResource(path);

        LineMapper<Log> lineMapper = new DefaultLineMapper<Log>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer("|") {{
                    setNames(new String[]{"now", "ip", "request", "status", "user"});
                }});
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Log>() {
                    {
                        setTargetType(Log.class);
                    }
                });
            }
        };
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
        JdbcBatchItemWriter<Log> writer = new JdbcBatchItemWriter<Log>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Log>());
        writer.setSql("INSERT INTO tb_log (dt_now, ds_ip, ds_request, cd_status, ds_user) VALUES (:now, :ip, :request, :status, :user)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Log, Log> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
