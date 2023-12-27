package ru.study.t4_spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t4_spring.config.LoaderConfig;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.service.Loader;
import ru.study.t4_spring.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoaderTest {
    private final Loader loader;
    private final LoaderConfig config = new LoaderConfig();
    private final Path inFile;

    private static final String LOGIN1 = "PRVlas";
    private static final String FIO1 = "Vlasov Pavel Romanovich";
    private static final String ACCESS_DATE1 = "2023-12-25T08:12:43";
    private static final String APPL_TYPE1 = "mobile";

    private static final String LOGIN2 = "AANovov";
    private static final String FIO2 = "Novov Alex Arturovich";
    private static final String ACCESS_DATE2 = "2023-12-25T08:15:58";
    private static final String APPL_TYPE2 = "web";

    public LoaderTest() {
        config.setIn("c:\\share\\source");
        config.setArchive("c:\\share\\archive");
        loader = new Loader(config, null);
        inFile = getPath();
    }

    @Test
    @DisplayName("Успешное чтение данных из файла")
    public void load_success() {
        createFile(inFile);

        List<LoginRec> actualList = loader.make(new ArrayList<>());
        actualList = actualList.stream().sorted(Comparator.comparing(LoginRec::getLogin)).toList();

        List<LoginRec> expectedList = new ArrayList<>();
        expectedList.add(new LoginRec(LOGIN1, FIO1, Utils.mapStringToDate(ACCESS_DATE1), APPL_TYPE1, inFile.toFile().getName()));
        expectedList.add(new LoginRec(LOGIN2, FIO2, Utils.mapStringToDate(ACCESS_DATE2), APPL_TYPE2, inFile.toFile().getName()));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoginRec::getLogin)).toList();

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Чтение некорректных данных из файла")
    public void load_invalidData() {
        createInvalidFile(inFile);
        final List<LoginRec> actualList = loader.make(new ArrayList<>());

        Assertions.assertEquals(0, actualList.size());
    }

    @Test
    @DisplayName("Чтение данных из пустого каталога")
    public void load_emptyDir() {
        clearPath(config.getIn());
        final List<LoginRec> actualList = loader.make(new ArrayList<>());

        Assertions.assertEquals(0, actualList.size());
    }

    private void createFile(Path file) {
        try {
            clearPath(config.getIn());

            if (!Files.exists(Path.of(config.getIn())))
                Files.createDirectories(Path.of(config.getIn()));

            String data = String.format("%s,%s,%s,%s\r\n", LOGIN1, FIO1, ACCESS_DATE1, APPL_TYPE1);
            Files.write(file, data.getBytes(), StandardOpenOption.CREATE);

            data = String.format("%s,%s,%s,%s\r\n", LOGIN2, FIO2, ACCESS_DATE2, APPL_TYPE2);
            Files.write(file, data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createInvalidFile(Path file) {
        try {
            clearPath(config.getIn());

            if (!Files.exists(Path.of(config.getIn())))
                Files.createDirectories(Path.of(config.getIn()));

            String data = String.format("%s\r\n", LOGIN1);
            Files.write(file, data.getBytes(), StandardOpenOption.CREATE);

            data = String.format("%s,%s\r\n", LOGIN2, FIO2);
            Files.write(file, data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPath() {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        final String fileName = formatter.format(new Date()).concat(".txt");
        return Path.of(config.getIn().concat("\\").concat(fileName));
    }

    private void clearPath(String pathName) {
        final File folder = new File(pathName);
        final File[] files = folder.listFiles();

        if (files == null) return;

        for (File file : files) {
            file.delete();
        }
    }
}
