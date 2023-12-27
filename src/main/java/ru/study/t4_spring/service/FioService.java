package ru.study.t4_spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.utils.Utils;

import java.util.List;

@Component
@Qualifier("fioService")
public class FioService implements Serviceable {

    private final Serviceable nextService;

    public FioService(@Qualifier("applicationService") Serviceable nextService) {
        this.nextService = nextService;
    }

    @Override
    public List<LoginRec> make(List<LoginRec> list) {
        list.forEach(rec -> rec.setFio(Utils.toUpperCaseChar(rec.getFio())));
        return list;
    }

    @Override
    public Serviceable getNextService() {
        return nextService;
    }
}
