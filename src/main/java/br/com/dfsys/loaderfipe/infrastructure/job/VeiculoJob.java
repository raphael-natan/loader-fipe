package br.com.dfsys.loaderfipe.infrastructure.job;

import br.com.dfsys.loaderfipe.domain.entity.fipe.Veiculo;
import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Ano;
import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Marca;
import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Modelo;
import br.com.dfsys.loaderfipe.infrastructure.processor.AnoProcessor;
import br.com.dfsys.loaderfipe.infrastructure.processor.MarcaProcessor;
import br.com.dfsys.loaderfipe.infrastructure.processor.ModeloProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class VeiculoJob extends AbstractJob {

    @Autowired
    private MarcaProcessor marcaProcessor;
    @Autowired
    private ModeloProcessor modeloProcessor;
    @Autowired
    private AnoProcessor anoProcessor;
    @Autowired
    private EntityManagerFactory emf;

    @Value("${application.batch.settings.volumes.basepath}")
    private String basepath;
    @Value("${application.batch.settings.volumes.filename}")
    private String filename;

    @Bean
    public Step marcaStep() throws FlatFileParseException {
        return stepBuilderFactory.get("marcaStep")
                                .<Veiculo, Marca>chunk(chunkSize)
                                .reader(veiculoReader())
                                .processor(marcaProcessor)
                                .writer(marcaWriter())
                                .build();
    }

    @Bean
    public ItemStreamReader<Veiculo> veiculoReader() {
        return new FlatFileItemReaderBuilder<Veiculo>()
                .name("veiculoReader")
                .resource(new FileSystemResource(basepath + filename))
                .delimited().delimiter(";")
                .names(new String[]{"MARCA", "MODELO", "ANO", "TIPO"})
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Veiculo>() {{
                    setTargetType(Veiculo.class);
                }})
                .strict(false)
                .build();
    }

    @Bean
    public ItemWriter<Marca> marcaWriter() {
        JpaItemWriter<Marca> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(emf);
        return itemWriter;
    }

    @Bean
    public Step modeloStep() throws FlatFileParseException {
        return stepBuilderFactory.get("modeloStep")
                .<Veiculo, Modelo>chunk(chunkSize)
                .reader(veiculoReader())
                .processor(modeloProcessor)
                .writer(modeloWriter())
                .build();
    }

    @Bean
    public ItemWriter<Modelo> modeloWriter() {
        JpaItemWriter<Modelo> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(emf);
        return itemWriter;
    }

    @Bean
    public Step anoStep() throws FlatFileParseException {
        return stepBuilderFactory.get("anoStep")
                .<Veiculo, Ano>chunk(chunkSize)
                .reader(veiculoReader())
                .processor(anoProcessor)
                .writer(anoWriter())
                .build();
    }

    @Bean
    public ItemWriter<Ano> anoWriter() {
        JpaItemWriter<Ano> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(emf);
        return itemWriter;
    }
}