package com.udacity.jdnd.course3.critter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix="com.udacity.datasource")
    public DataSource getDataSource(){
        DataSourceBuilder dsb = DataSourceBuilder.create();
        //After the start of the summertime, I wasn't able to access my database on mySql.
        //"serverTimezone=Etc/UTC&useLegacyDatetimeCode=false" has been added
        dsb.url("jdbc:mysql://localhost:3306/pet?serverTimezone=Etc/UTC&useLegacyDatetimeCode=false");
        return dsb.build();
    }
}
