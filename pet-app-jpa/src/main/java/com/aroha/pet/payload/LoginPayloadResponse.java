package com.aroha.pet.payload;


/**
 *
 * @author Jaydeep  | Date: 15 May, 2020  11:29:46 AM
 */
public class LoginPayloadResponse {

	private Long userId;
	private String name;
	private String loginTime;
	private String logOutTime;
	private String logedeInTime;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogedeInTime() {
		return logedeInTime;
	}

	public void setLogedeInTime(String logedeInTime) {
		this.logedeInTime = logedeInTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLogOutTime() {
		return logOutTime;
	}

	public void setLogOutTime(String logOutTime) {
		this.logOutTime = logOutTime;
	}


}
