package com.aroha.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query(value = "select * from question where question_id=?1 ", nativeQuery = true)
    public Question findQuestion(int questionId);

    @Query(value = "select * from question where scenario_id=?1", nativeQuery = true)
    public List<Question> checkDuplicate(int scenarioId);
     
}
