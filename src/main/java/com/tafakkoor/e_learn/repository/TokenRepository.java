package com.tafakkoor.e_learn.repository;

import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, AuthUser> {

    @Query("DELETE FROM Token u WHERE (u.createdAt + INTERVAL ('10 MINUTE')) <= NOW()")
    int deleteByCreatedAtBefore();

    Optional<Token> findByUser( AuthUser user );

    Optional<Token> findByToken( String token );


     static void main( String[] args ) {

    }

}
