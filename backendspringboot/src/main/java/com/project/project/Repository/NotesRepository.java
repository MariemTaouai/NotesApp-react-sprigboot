package com.project.project.Repository;

import com.project.project.Entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByUserId(Long userId);
}


