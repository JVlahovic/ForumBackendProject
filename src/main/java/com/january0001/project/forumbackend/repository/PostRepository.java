package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.january0001.project.forumbackend.entity.Thread;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findByThread(Thread thread, Pageable pageable);

}
