package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Thread;

import com.january0001.project.forumbackend.entity.ThreadCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Integer> {


    List<Thread> findByThreadCategory(ThreadCategory category); //deprecation inbound, lazy fetching must be avoided for author getting

    @Query("SELECT t FROM Thread t JOIN FETCH t.author WHERE t.threadCategory = :category")
    List<Thread> findByThreadCategoryWithAuthor(@Param("category") ThreadCategory category);


}
