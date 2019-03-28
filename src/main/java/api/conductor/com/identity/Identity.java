package api.conductor.com.identity;

import java.io.Serializable;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class Identity implements User, Serializable {
	private static final long serialVersionUID = 6709711271509594173L;
	private String name;
	private Set<String> roles;
	private String affiliation;
	private Enrollment enrollment;
	private String mspId;

	public Identity(String name, String affiliation, String mspId, Enrollment enrollment) {
		this.name = name;
		this.affiliation = affiliation;
		this.enrollment = enrollment;
		this.mspId = mspId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public String getMspId() {
		return mspId;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

	public String toString() {
		return "AppUser{" + "name='" + name + '\'' + "\n, roles=" + roles + "\n"+ "'affiliation='" + affiliation + '\'' + "\n, enrollment=" + enrollment + "\n, mspId='" + mspId
				+ '\'' + '}';
	}

	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return null;
	}
}
