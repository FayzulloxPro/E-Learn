package com.tafakkoor.e_learn.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter

@RequiredArgsConstructor


public class Questions extends Auditable {
    @Column( nullable = false )
    private String title;
    @JoinColumn(name = "content_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Content content;


}
