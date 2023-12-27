package ru.study.t4_spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.study.t4_spring.dto.Login;
import ru.study.t4_spring.dto.User;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.repo.LoginRepository;
import ru.study.t4_spring.repo.UserRepository;
import ru.study.t4_spring.service.Writer;
import ru.study.t4_spring.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

public class WriterTest {
    private final Writer service;
    private final UserRepository userRepo = Mockito.mock(UserRepository.class);
    private final LoginRepository loginRepo = Mockito.mock(LoginRepository.class);

    private static final String LOGIN = "PRVlas";
    private static final String FIO = "Vlasov Pavel Romanovich";
    private static final String ACCESS_DATE = "2023-12-25T08:12:43";
    private static final String APPL_TYPE = "mobile";

    private final List<LoginRec> actualList;

    public WriterTest() {
        service = new Writer(userRepo, loginRepo);

        actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));
    }

    @BeforeEach
    public void starter() {
        Mockito.reset(userRepo, loginRepo);
    }

    @Test
    @DisplayName("Сохранение в БД - новый пользователь")
    public void saveToDB_newUser() {
        Mockito.doReturn(null).when(userRepo).findByUsername(LOGIN); // пользователь не найден

        User user = new User(1L, LOGIN, FIO);
        Mockito.doReturn(user).when(userRepo).save(Mockito.any(User.class));  // создание нового пользователя

        service.make(actualList);

        // Сохранение нового пользователя
        Mockito.verify(userRepo, times(1)).save(Mockito.any(User.class));

        // Сохранение логина пользователя
        Mockito.verify(loginRepo, times(1)).save(Mockito.any(Login.class));
    }

    @Test
    @DisplayName("Сохранение в БД - пользователь уже есть в БД")
    public void saveToDB_onlyLogin() {
        User user = new User(1L, LOGIN, FIO);
        Mockito.doReturn(user).when(userRepo).findByUsername(LOGIN); // пользователь найден в БД

        service.make(actualList);

        // Отсутствует сохранение нового пользователя
        Mockito.verify(userRepo, times(0)).save(user);

        // Сохранение логина пользователя
        Mockito.verify(loginRepo, times(1)).save(Mockito.any(Login.class));
    }
}
