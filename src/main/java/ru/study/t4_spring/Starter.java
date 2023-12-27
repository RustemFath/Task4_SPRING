package ru.study.t4_spring;

import org.springframework.stereotype.Component;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.service.Serviceable;

import java.util.ArrayList;
import java.util.List;

@Component
public class Starter {
    private final Serviceable loader;

    public Starter(Serviceable loader) {
        this.loader = loader;
    }

    /**
     * Загрузка файлов из входящего каталога и дальнейшая обработка
     */
    public void processFiles() {
        try {
            Serviceable service = loader;
            List<LoginRec> list = new ArrayList<>();
            while (service != null) {
                list = service.make(list);
                service = service.getNextService();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
