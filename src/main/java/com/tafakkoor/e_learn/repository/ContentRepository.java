package com.tafakkoor.e_learn.repository;

import com.tafakkoor.e_learn.domain.Content;
import com.tafakkoor.e_learn.enums.ContentType;
import com.tafakkoor.e_learn.enums.Levels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByLevelAndContentTypeAndDeleted(Levels level, ContentType contentType, boolean deleted);

    Content findByIdAndContentType(Long id, ContentType story);
}
