package com.tafakkoor.e_learn.repository;

import com.tafakkoor.e_learn.domain.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByContentId(Long id);

    List<Comment> findAllByContentIdAndDeleted(Long id, boolean b);

    @Modifying
    @Transactional
    @Query(value = "insert into comment (comment, commenttype, user_id, content_id, parentid) values (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void saveComment(String comment, String commentType, long userId, long contentId, long parentId);

    @Modifying
    @Transactional
    @Query(value = "update comment set deleted = true where id = ?1", nativeQuery = true)
    void setAsDelete(long id);

    @Modifying
    @Transactional
    @Query(value = "update comment set comment = ?1 where id = ?2", nativeQuery = true)
    void updateComment(String comment, long id);
}
