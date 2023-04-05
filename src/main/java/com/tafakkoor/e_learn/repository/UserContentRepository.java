package com.tafakkoor.e_learn.repository;

import com.tafakkoor.e_learn.domain.UserContent;
import com.tafakkoor.e_learn.enums.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContentRepository extends JpaRepository<UserContent, Long> {
//    List<UserContent> findByUserIdAndProgress( Long userId, Progress progress);

    UserContent findByUserIdAndProgressOrProgress(Long id, Progress inProgress, Progress takeTest);

}
