package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Thread;

import com.january0001.project.forumbackend.entity.ThreadCategory;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Integer> {


    List<Thread> findByThreadCategory(ThreadCategory category);

}
