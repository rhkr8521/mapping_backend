package com.mapping.mapping.user.repository;


import com.mapping.mapping.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    public boolean existsByUserEmailAndUserPassword(String userEmail, String userPassword);
    public boolean existsByUserNickname(String userNickname);
    public UserEntity findByUserEmail(String userEmail);
    public UserEntity findByUserNickname(String userNickname);

}

