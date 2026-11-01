package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Post;
import com.january0001.project.forumbackend.entity.Thread;

import com.january0001.project.forumbackend.entity.ThreadCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Integer> {


    List<Thread> findByThreadCategory(ThreadCategory category); //deprecation inbound, lazy fetching must be avoided for author getting

    @Query("SELECT t FROM Thread t JOIN FETCH t.author WHERE t.threadCategory = :category")
    List<Thread> findByThreadCategoryWithAuthor(@Param("category") ThreadCategory category);

    @Query("SELECT t FROM Thread t JOIN FETCH t.author WHERE t.id = :id")
    Optional<Thread> findByIdWithAuthor(@Param("id") Integer id);

    @Query("SELECT t FROM Thread t JOIN FETCH t.author JOIN FETCH t.threadCategory " +
            "WHERE t.author.username = :username ORDER BY t.createdAt DESC")
    List<Thread> findByAuthorUsernameWithRefs(@Param("username") String username);

}
