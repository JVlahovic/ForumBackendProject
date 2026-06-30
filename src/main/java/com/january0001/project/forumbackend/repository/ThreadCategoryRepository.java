package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.ThreadCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThreadCategoryRepository extends JpaRepository<ThreadCategory, Integer> {

    List<ThreadCategory> findByAccessCtrl(String accessCtrl);

    List<ThreadCategory> findByAccessCtrlIn(List<String> accessCtrl);
}
