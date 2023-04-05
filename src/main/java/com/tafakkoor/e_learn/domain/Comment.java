package com.tafakkoor.e_learn.domain;

import com.tafakkoor.e_learn.enums.CommentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Comment extends Auditable {
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentType commentType;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AuthUser userId;
    @Column(name = "content_id", nullable = false)
    private Long contentId;

    private Long parentId;

}