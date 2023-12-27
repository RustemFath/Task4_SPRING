package ru.study.t4_spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.service.ApplicationService;
import ru.study.t4_spring.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApplicationServiceTest {
    private final ApplicationService service = new ApplicationService(null);

    private static final String LOGIN = "PRVlas";
    private static final String FIO = "Vlasov Pavel Romanovich";
    private static final String ACCESS_DATE = "2023-12-25T08:12:43";
    private static final String APPL_TYPE1 = "mobile";
    private static final String APPL_TYPE2 = "web";
    private static final String APPL_TYPE3 = "pc";
    private static final String APPL_TYPE4 = "other:pc";

    @Test
    @DisplayName("Тип приложения из зарегистрированного списка")
    public void typeAppl_ofRegisterList() {
        List<LoginRec> actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE1, null));
        actualList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE2, null));
        actualList = service.make(actualList);
        actualList = actualList.stream().sorted(Comparator.comparing(LoginRec::getApplication)).toList();

        List<LoginRec> expectedList = new ArrayList<>();
        expectedList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE1, null));
        expectedList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE2, null));
        expectedList = expectedList.stream().sorted(Comparator.comparing(LoginRec::getApplication)).toList();

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Тип приложения не из зарегистрированного списка")
    public void typeAppl_NoOfRegisterList() {
        List<LoginRec> actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE3, null));
        actualList = service.make(actualList);

        final List<LoginRec> expectedList = new ArrayList<>();
        expectedList.add(new LoginRec(LOGIN, FIO, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE4, null));

        Assertions.assertEquals(expectedList, actualList);
    }
}
