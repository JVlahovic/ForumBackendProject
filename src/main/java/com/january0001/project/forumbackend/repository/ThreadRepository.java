package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Integer> {
}
