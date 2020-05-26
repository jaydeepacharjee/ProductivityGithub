package com.aroha.pet.model.audit;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class LoginDateAudit implements Serializable {

    @CreatedDate
    @Column(insertable = true, updatable = true)
    private Instant loginDateTime;

    private Instant logoutDateTime;

    public Instant getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(Instant loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public Instant getLogoutDateTime() {
        return logoutDateTime;
    }

    public void setLogoutDateTime(Instant logoutDateTime) {
        this.logoutDateTime = logoutDateTime;
    }

}
