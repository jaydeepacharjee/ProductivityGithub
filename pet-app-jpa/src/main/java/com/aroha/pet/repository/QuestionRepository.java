package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aroha.pet.model.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query(value = "select * from question where question_id=?1 ", nativeQuery = true)
    public Question findQuestion(int questionId);
}
