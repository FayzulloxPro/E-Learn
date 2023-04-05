package com.tafakkoor.e_learn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image extends Auditable{
    private String generatedFileName;
    private String originalFileName;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String filePath;
    private String mimeType;
}
