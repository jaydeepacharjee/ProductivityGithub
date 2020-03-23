package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aroha.pet.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query(value = "select * from question where question_id=?1 ", nativeQuery = true)
    public Question findQuestion(int questionId);

    @Query(value = "select count(*) from question where scenario_id=?1 and question_desc =?2", nativeQuery = true)
    public Integer checkDuplicate(int scenarioId, String questionName);
    
    @Query(value="select * from question where technology_id=?1",nativeQuery = true)
    public List<Question> getAllQuestion(int technologyId);
    
}
