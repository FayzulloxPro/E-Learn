package com.tafakkoor.e_learn.repository;

import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.domain.Vocabulary;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    List<Vocabulary> findAllByStoryIdAndAuthUserAndDeleted(long id, AuthUser authUser, boolean deleted);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Vocabulary SET word = ?1, translation = ?2, definition = ?3 WHERE id = ?4" , nativeQuery = true)
    void updateVocabulary(String word, String translation, String definition, Long id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Vocabulary SET deleted = true, deleted_at=now(), deleted_by=?1 WHERE id = ?2" , nativeQuery = true)
    void setAsDelete(Long userId, Long id);
}
