package ru.study.t4_spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.study.t4_spring.config.LoaderConfig;
import ru.study.t4_spring.dto.Login;
import ru.study.t4_spring.repo.LoginRepository;
import ru.study.t4_spring.repo.UserRepository;
import ru.study.t4_spring.dto.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {Main.class})
public class IntegrationTest {
    @Autowired
    private Starter starter;
    @Autowired
    private LoaderConfig config;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private LoginRepository loginRepo;

    private static final String LOGIN = "KARogov";
    private static final String FIO = "Rogov kirill Alekseevich";
    private static final String ACCESS_DATE1 = "2023-12-26T10:56:07";
    private static final String ACCESS_DATE2 = "2023-12-27T12:36:34";
    private static final String APPL_TYPE1 = "mobile";
    private static final String APPL_TYPE2 = "web";

    @Test
    @DisplayName("Чтение данных из файла, обработка, запись в БД")
    public void file2DB_success() {
        deleteUserToDB(LOGIN);
        createFile(getPath());

        starter.processFiles();

        User user = userRepo.findByUsername(LOGIN);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(2, loginRepo.findAllByUserId(user.getId()).size());
    }

    private void createFile(Path file) {
        try {
            clearPath(config.getIn());

            if (!Files.exists(Path.of(config.getIn())))
                Files.createDirectories(Path.of(config.getIn()));

            String data = String.format("%s,%s,%s,%s\r\n", LOGIN, FIO, ACCESS_DATE1, APPL_TYPE1);
            data = data.concat(String.format("%s,%s,%s,%s\r\n", LOGIN, FIO, ACCESS_DATE2, APPL_TYPE2));
            Files.write(file, data.getBytes(), StandardOpenOption.CREATE);
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

    private void deleteUserToDB(String login) {
        User user = userRepo.findByUsername(login);
        if (user != null) {
            try {
                List<Login> logins = loginRepo.findAllByUserId(user.getId());
                loginRepo.deleteAll(logins);
                userRepo.deleteById(user.getId());
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
