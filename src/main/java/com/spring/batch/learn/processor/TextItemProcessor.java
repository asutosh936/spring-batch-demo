package com.spring.batch.learn.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TextItemProcessor implements ItemProcessor<String, String> {
    @Override
    public String process(String item) throws Exception {
        String maskedMessage = item.replaceAll("\\d", "*");
        return maskedMessage;
    }
}
