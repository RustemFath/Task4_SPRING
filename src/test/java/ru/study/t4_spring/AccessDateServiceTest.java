package ru.study.t4_spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t4_spring.config.LoggerConfig;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.service.AccessDateService;
import ru.study.t4_spring.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AccessDateServiceTest {
    private final AccessDateService service;
    private final LoggerConfig config = new LoggerConfig();

    private static final String LOGIN = "PRVlas";
    private static final String FIO = "Vlasov Pavel Romanovich";
    private static final String ACCESS_DATE = "2023-12-25T08:12:43";
    private static final String APPL_TYPE = "mobile";

    public AccessDateServiceTest() {
        config.setDir("c:\\share\\logs");
        service = new AccessDateService(config, null);
    }

    @Test
    @DisplayName("Дата входа присутствует")
    public void accessDate_exist() {
        List<LoginRec> actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));
        actualList = service.make(actualList);

        List<LoginRec> expectedList = new ArrayList<>();
        expectedList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));

        Assertions.assertEquals(expectedList, actualList);
        Assertions.assertEquals(0, getLogFileCount(), "Запись не должна попасть в лог файл");
    }

    @Test
    @DisplayName("Дата входа отсутствует")
    public void accessDate_empty() {
        List<LoginRec> actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO, null, APPL_TYPE, null));
        actualList = service.make(actualList);

        Assertions.assertEquals(0, actualList.size(), "Запись не должна попадать в выходной список");
        Assertions.assertEquals(1, getLogFileCount(), "Запись должна попасть в лог файл");
    }

    private int getLogFileCount() {
        final File folder = new File(config.getDir());
        final File[] files = folder.listFiles();

        if (files == null) return 0;
        return files.length;
    }

    @BeforeEach
    public void clearDir() {
        final File folder = new File(config.getDir());
        final File[] files = folder.listFiles();

        if (files == null) return;

        for (File file : files) {
            file.delete();
        }
    }
}
