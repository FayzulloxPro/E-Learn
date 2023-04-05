package com.tafakkoor.e_learn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuthPermission extends Auditable{
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String code;
}
