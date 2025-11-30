package com.spring.batch.learn;

import com.spring.batch.learn.listener.BatchJobListener;
import com.spring.batch.learn.processor.TextItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class SpringBatchDemoApplication {

    @Bean
    protected FlatFileItemReader<String> fileItemReader(){
        return new FlatFileItemReaderBuilder<String>().
                resource(new ClassPathResource("data-file.csv")).
                name("csv-reader").
                lineMapper((line, lineNumber) -> line).build();
    }

    @Bean
    protected FlatFileItemWriter<String> fileItemWriter(){
        return new FlatFileItemWriterBuilder<String>().
                resource(new FileSystemResource("src/main/resources/masked-file.csv")).
                name("csv-writer").
                lineAggregator(item -> item).build();
    }

    @Bean
    protected Step maskingStep(JobRepository jobRepository, PlatformTransactionManager manager,
                               TextItemProcessor processor, FlatFileItemWriter<String> writer,
                               FlatFileItemReader<String> reader){
        return new StepBuilder("masking-repo", jobRepository)
                .<String, String> chunk(2, manager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

    }

    @Bean
    protected Job maskingJob(JobRepository jobRepository, Step maskingStep, BatchJobListener jobListener){
        return new JobBuilder("masking-job", jobRepository)
                .start(maskingStep).listener(jobListener)
                .build();
    }
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemoApplication.class, args);
	}

}
