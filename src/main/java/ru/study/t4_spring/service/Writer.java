package ru.study.t4_spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.study.t4_spring.dto.Login;
import ru.study.t4_spring.dto.User;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.repo.LoginRepository;
import ru.study.t4_spring.repo.UserRepository;

import java.util.List;

@Component
@Qualifier("writer")
public class Writer implements Serviceable {
    private final UserRepository userRepo;
    private final LoginRepository loginRepo;

    public Writer(UserRepository userRepo, LoginRepository loginRepo) {
        this.userRepo = userRepo;
        this.loginRepo = loginRepo;
    }

    @Override
    public List<LoginRec> make(List<LoginRec> list) {
        list.forEach(rec -> {
            User user = userRepo.findByUsername(rec.getLogin());
            if (user == null) {
                user = userRepo.save(new User(null, rec.getLogin(), rec.getFio()));
            }
            loginRepo.save(new Login(null, rec.getAccessDate(), user.getId(), rec.getApplication()));
        });
        return list;
    }

    @Override
    public Serviceable getNextService() {
        return null;
    }
}
