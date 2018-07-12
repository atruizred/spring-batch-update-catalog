package com.atruiz.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.atruiz.batch.bo.AnimeCsv;
import com.atruiz.batch.bo.MalAnime;
import com.atruiz.batch.listener.JobCompletionNotificationListener;
import com.atruiz.batch.processor.AnimeItemProcessor;

@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    public StepBuilderFactory stepBuilderFactory;
	
	// tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<AnimeCsv> reader() {
        return new FlatFileItemReaderBuilder<AnimeCsv>()
            .name("personItemReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"fansub", "name"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<AnimeCsv>() {{
                setTargetType(AnimeCsv.class);
            }})
            .build();
    }
    
    @Bean
    public AnimeItemProcessor processor() {
        return new AnimeItemProcessor();
    }
    
    @Bean
    public JdbcBatchItemWriter<MalAnime> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MalAnime>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO anime (mal_id, url, image_url, title, fansub, description, type, score, episodes, members) "
            		+ "VALUES (:mal_id, :url, :image_url, :title, :fansub, :description, :type, :score, :episodes, :members)")
            .dataSource(dataSource)
            .build();
    }
    // end::readerwriterprocessor[]
    
    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("updateAnimeJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<MalAnime> writer) {
        return stepBuilderFactory.get("step1")
            .<AnimeCsv, MalAnime> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }
    // end::jobstep[]

}
