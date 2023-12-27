package ru.study.t4_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("logins")
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @Id
    private Long id;
    @Column("access_date")
    private Date accessDate;
    @Column("user_id")
    private Long userId;
    private String application;
}
