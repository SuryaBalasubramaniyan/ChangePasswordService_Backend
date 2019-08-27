package com.sapient.data.model;

public class PasswordHistory {

	private Long passId;
	private String pwd1; // current password
	private String salt1; // current salt
	private String pwd2;
	private String salt2;
	private String pwd3;
	private String salt3;

	public PasswordHistory() {
		super();
	}

	public PasswordHistory(Long passId, String pwd1, String salt1) {
		super();
		this.passId = passId;
		this.pwd1 = pwd1;
		this.salt1 = salt1;
	}

	public String getPwd1() {
		return pwd1;
	}

	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}

	public Long getPassId() {
		return passId;
	}

	public void setPassId(Long passId) {
		this.passId = passId;
	}

	public String getSalt1() {
		return this.salt1;
	}

	public void setSalt1(String salt1) {
		this.salt1 = salt1;
	}

	public String getPwd2() {
		return this.pwd2;
	}

	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}

	public String getSalt2() {
		return this.salt2;
	}

	public void setSalt2(String salt2) {
		this.salt2 = salt2;
	}

	public String getPwd3() {
		return this.pwd3;
	}

	public void setPwd3(String pwd3) {
		this.pwd3 = pwd3;
	}

	public String getSalt3() {
		return this.salt3;
	}

	public void setSalt3(String salt3) {
		this.salt3 = salt3;
	}

	@Override
	public String toString() {
		return "Password [passId=" + passId + ", pwd1=" + pwd1 + ", salt1=" + salt1 + ", pwd2=" + pwd2 + ", salt2="
				+ salt2 + ", pwd3=" + pwd3 + ", salt3=" + salt3 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((passId == null) ? 0 : passId.hashCode());
		result = prime * result + ((pwd1 == null) ? 0 : pwd1.hashCode());
		result = prime * result + ((salt1 == null) ? 0 : salt1.hashCode());
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
		PasswordHistory other = (PasswordHistory) obj;
		if (passId == null) {
			if (other.passId != null) {
				return false;
			}
		} else if (!passId.equals(other.passId)) {
			return false;
		}
		if (pwd1 == null) {
			if (other.pwd1 != null) {
				return false;
			}
		} else if (!pwd1.equals(other.pwd1)) {
			return false;
		}
		if (salt1 == null) {
			if (other.salt1 != null) {
				return false;
			}
		} else if (!salt1.equals(other.salt1)) {
			return false;
		}
		return true;
	}

}
