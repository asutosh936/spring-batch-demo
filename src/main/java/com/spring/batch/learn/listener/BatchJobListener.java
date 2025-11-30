package com.spring.batch.learn.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class BatchJobListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            String filePath = "src/main/resources/masked-file.csv";
            try {
                Files.lines(Paths.get(filePath)).forEach(System.out::println);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
