package com.tafakkoor.e_learn.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vocabulary")
@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class Vocabulary extends Auditable {
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Content story;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private AuthUser authUser;
    @Column(nullable = false)
    private String word;
    @Column(nullable = false)
    private String translation;
    private String definition;
}
