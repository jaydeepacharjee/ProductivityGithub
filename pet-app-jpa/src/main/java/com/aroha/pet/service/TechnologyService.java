package com.aroha.pet.service;

import com.aroha.pet.model.Technology;
import com.aroha.pet.payload.TechnologyPayload;
import com.aroha.pet.repository.TechnologyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jaydeep
 */
@Service
public class TechnologyService {

    @Autowired
    private TechnologyRepository techRepo;

    public List<TechnologyPayload> findAllTechnology() {
        List<Technology> list = techRepo.findAll();
        List<TechnologyPayload> load = new ArrayList<>();
        list.stream().map((tech) -> {
            return new TechnologyPayload(tech.getTechId(), tech.getTechnologyName());
        }).forEachOrdered((loadObj) -> {
            load.add(loadObj);
        });
        return load;
    }
    
    public Optional<Technology> findById(int technologyId){
        return techRepo.findById(technologyId);
    }
    
    public List<Technology>findTechnology(Long userId){
    	return techRepo.getTech(userId);
    }
}
