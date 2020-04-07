package com.aroha.pet.payload;

import com.aroha.pet.model.Role;

import java.sql.Date;
import java.time.Instant;
import java.util.Set;

public class UserProfile {

	private Long id;
	private String name;
	private Instant joinedAt;
	private Set<Role> roles;

	public UserProfile(Long id, String name, Instant date, Set<Role> roles) {
		this.id = id;
		this.name = name;
		this.joinedAt = date;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
