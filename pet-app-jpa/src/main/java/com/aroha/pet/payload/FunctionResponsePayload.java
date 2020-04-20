package com.aroha.pet.payload;

import java.util.Objects;

/**
 *
 * @author jaydeep
 */
public class FunctionResponsePayload {
    
    private int function_id;
    private String functionName;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.function_id;
        hash = 41 * hash + Objects.hashCode(this.functionName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FunctionResponsePayload other = (FunctionResponsePayload) obj;
        if (this.function_id != other.function_id) {
            return false;
        }
        if (!Objects.equals(this.functionName, other.functionName)) {
            return false;
        }
        return true;
    }

    public int getFunction_id() {
        return function_id;
    }

    public void setFunction_id(int function_id) {
        this.function_id = function_id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    
    

}
