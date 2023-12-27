package ru.study.t4_spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.service.FioService;
import ru.study.t4_spring.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FioServiceTest {
    private final FioService service = new FioService(null);

    private static final String LOGIN = "PRVlas";
    private static final String FIO1 = "Vlasov Pavel Romanovich";
    private static final String FIO2 = "vlasov pavel romanovich";
    private static final String ACCESS_DATE = "2023-12-25T08:12:43";
    private static final String APPL_TYPE = "mobile";

    @Test
    @DisplayName("ФИО начинаются с большой буквы")
    public void convert_normFio() {
        List<LoginRec> actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO1, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));
        actualList = service.make(actualList);

        List<LoginRec> expectedList = new ArrayList<>();
        expectedList.add(new LoginRec(LOGIN, FIO1, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("ФИО начинаются с маленькой буквы")
    public void convert_unNormFio() {
        List<LoginRec> actualList = new ArrayList<>();
        actualList.add(new LoginRec(LOGIN, FIO2, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));
        actualList = service.make(actualList);

        List<LoginRec> expectedList = new ArrayList<>();
        expectedList.add(new LoginRec(LOGIN, FIO1, Utils.mapStringToDate(ACCESS_DATE), APPL_TYPE, null));
        Assertions.assertEquals(expectedList, actualList);
    }
}
