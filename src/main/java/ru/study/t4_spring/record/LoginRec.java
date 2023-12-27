package ru.study.t4_spring.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LoginRec {
    private String login;
    private String fio;
    private Date accessDate;
    private String application;
    private String filename;

    @Override
    public String toString() {
        return "LoginRec{" +
                "login='" + login + '\'' +
                ", fio='" + fio + '\'' +
                ", accessDate=" + accessDate +
                ", application='" + application + '\'' +
                '}';
    }
}
