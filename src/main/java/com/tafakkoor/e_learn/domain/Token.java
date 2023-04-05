package com.tafakkoor.e_learn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Token extends Auditable {
    @ManyToOne
    private AuthUser user;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime validTill;


}
