package com.aroha.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.Domain;
import com.aroha.pet.payload.DomainRequest;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Integer> {

    @Query("select new com.aroha.pet.payload.DomainRequest(d.domainId,f.functionId,s.scenarioId,q.questionId)"
            + "from Domain d left join d.functions f left join f.scenario s left join s.ques q where q.questionId=?1")
    public DomainRequest updateDomainData(int questionId);

    public Boolean existsBydomainName(String domainName);
    
    @Query(value="select * from domain d inner join technology t on d.technology_id=t.tech_id where d.technology_id=?1",nativeQuery = true)
    public List<Domain>getDomainOntechnology(int technologyId);
    


}
