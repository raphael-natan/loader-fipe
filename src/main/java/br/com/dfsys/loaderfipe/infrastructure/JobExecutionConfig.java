package br.com.dfsys.loaderfipe.infrastructure;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobExecutionConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private Step marcaStep;
    @Autowired
    private Step modeloStep;
    @Autowired
    private Step anoStep;

    @Bean
    public Job loadJob() {
        return jobBuilderFactory.get("loadJob")
                .incrementer(new RunIdIncrementer())
                .start(marcaStep)
                .next(modeloStep)
                .next(anoStep)
                .build();
    }
}