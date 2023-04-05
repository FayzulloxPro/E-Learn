package com.tafakkoor.e_learn.domain;

import com.tafakkoor.e_learn.enums.ContentType;
import com.tafakkoor.e_learn.enums.Levels;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Content extends Auditable {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(nullable = false)
    private Integer score;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Levels level;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    @JoinColumn(nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Document document;
}