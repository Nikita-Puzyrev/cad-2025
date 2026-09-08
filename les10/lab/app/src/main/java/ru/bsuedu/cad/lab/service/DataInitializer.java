package ru.bsuedu.cad.lab.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CsvDataLoader csvDataLoader;

    public DataInitializer(CsvDataLoader csvDataLoader) {
        this.csvDataLoader = csvDataLoader;
    }

    @PostConstruct
    public void init() {
        csvDataLoader.loadData();
        log.info("Начальные данные загружены из CSV-файлов");
    }
}