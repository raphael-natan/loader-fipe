package br.com.dfsys.loaderfipe.infrastructure.job;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractJob {

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Value("${application.batch.settings.chunkSize}")
    protected Integer chunkSize;
}
