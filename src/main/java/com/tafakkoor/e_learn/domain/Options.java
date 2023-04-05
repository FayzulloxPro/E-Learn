package com.tafakkoor.e_learn.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "variants")
@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class Options extends Auditable{
    @Column(nullable = false)
    private String value;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isCorrect;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Questions questions;
}
