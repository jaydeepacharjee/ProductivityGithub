package com.aroha.pet.payload;

/**
 *
 * @author Jaydeep
 */
public class TechnologyPayload {

    private int techId;
    private String technologyName;

    public TechnologyPayload(int techId, String technologyName) {
        this.techId = techId;
        this.technologyName = technologyName;
    }

    public TechnologyPayload() {
    }
    
    
    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

}