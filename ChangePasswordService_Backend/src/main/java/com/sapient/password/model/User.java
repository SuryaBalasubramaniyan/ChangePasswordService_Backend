package com.sapient.password.model;

public class User {

	private Long userID;
	private String emailID;
	private PasswordHistory passwordHistory;

	public User() {
		super();
	}

	public User(Long userID, PasswordHistory passwordHistory) {
		super();
		this.userID = userID;
		this.passwordHistory = passwordHistory;
	}

	public User(String emailID, PasswordHistory passwordHistory) {
		super();
		this.emailID = emailID;
		this.passwordHistory = passwordHistory;
	}

	public User(Long userID, String emailID, PasswordHistory passwordHistory) {
		super();
		this.userID = userID;
		this.emailID = emailID;
		this.passwordHistory = passwordHistory;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public PasswordHistory getPasswordHistory() {
		return passwordHistory;
	}

	public void setPasswordHistory(PasswordHistory passwordHistory) {
		this.passwordHistory = passwordHistory;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", emailID=" + emailID + ", passwordHistory=" + passwordHistory + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailID == null) ? 0 : emailID.hashCode());
		result = prime * result + ((passwordHistory == null) ? 0 : passwordHistory.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (emailID == null) {
			if (other.emailID != null) {
				return false;
			}
		} else if (!emailID.equals(other.emailID)) {
			return false;
		}
		if (passwordHistory == null) {
			if (other.passwordHistory != null) {
				return false;
			}
		} else if (!passwordHistory.equals(other.passwordHistory)) {
			return false;
		}
		if (userID == null) {
			if (other.userID != null) {
				return false;
			}
		} else if (!userID.equals(other.userID)) {
			return false;
		}
		return true;
	}

}
