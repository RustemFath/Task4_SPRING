package ru.study.t4_spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.study.t4_spring.annotation.LogTransformation;
import ru.study.t4_spring.config.LoggerConfig;
import ru.study.t4_spring.record.LoginRec;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Qualifier("accessDateService")
@LogTransformation(filename = "accDateService.log")
public class AccessDateService implements Serviceable {
    private final Serviceable nextService;
    private final LoggerConfig config;

    public AccessDateService(LoggerConfig config, @Qualifier("writer") Serviceable nextService) {
        this.nextService = nextService;
        this.config = config;
    }

    @Override
    public List<LoginRec> make(List<LoginRec> list) {
        List<LoginRec> resultList = new ArrayList<>();
        list.forEach(rec -> {
            if (rec.getAccessDate() == null) saveLog(rec);
            else resultList.add(rec);
        });
        return resultList;
    }

    @Override
    public Serviceable getNextService() {
        return nextService;
    }

    private void saveLog(LoginRec rec) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        String fileName = config.getDir().concat("\\").concat(formatter.format(new Date())).concat(".log");
        String data = String.format("%s: %s\r\n", rec.getFilename(), rec);
        try {
            if (!Files.exists(Path.of(config.getDir())))
                Files.createDirectories(Path.of(config.getDir()));
            Files.write(Path.of(fileName), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Ошибка записи в лог: " + e);
        }
    }
}
