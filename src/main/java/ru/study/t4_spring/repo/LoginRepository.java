package ru.study.t4_spring.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.t4_spring.dto.Login;

import java.util.List;

@Repository
public interface LoginRepository extends CrudRepository<Login, Long> {
    List<Login> findAllByUserId(Long userId);
}
