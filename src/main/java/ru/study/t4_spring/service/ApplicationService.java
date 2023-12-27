package ru.study.t4_spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.study.t4_spring.record.LoginRec;

import java.util.List;

@Component
@Qualifier("applicationService")
public class ApplicationService implements Serviceable {

    private final Serviceable nextService;

    public ApplicationService(@Qualifier("accessDateService") Serviceable nextService) {
        this.nextService = nextService;
    }

    @Override
    public List<LoginRec> make(List<LoginRec> list) {
        list.forEach(rec -> rec.setApplication(convertApplication(rec.getApplication())));
        return list;
    }

    @Override
    public Serviceable getNextService() {
        return nextService;
    }

    private String convertApplication(String str) {
        String outStr = "";
        if (str != null) outStr = str;
        if (outStr.equals("web") || outStr.equals("mobile")) return outStr;
        return "other:" + outStr;
    }
}
