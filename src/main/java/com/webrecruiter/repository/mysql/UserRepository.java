/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.repository.mysql;

import com.webrecruiter.model.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author irina
 */
public interface UserRepository extends JpaRepository<User,Long>{
    
    @Modifying
    @Query("update User user set user.userPassword = ?1 where user.id = ?2")
    @Transactional
    void changePassword(String userPassword, Long userId);
    
    public User findOneById(long id);
}
