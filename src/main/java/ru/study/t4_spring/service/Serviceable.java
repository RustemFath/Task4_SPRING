package ru.study.t4_spring.service;

import ru.study.t4_spring.record.LoginRec;

import java.util.List;

public interface Serviceable {
    List<LoginRec> make(List<LoginRec> list);
    Serviceable getNextService();
}
