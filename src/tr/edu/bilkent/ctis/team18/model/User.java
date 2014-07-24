package tr.edu.bilkent.ctis.team18.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.annotations.SerializedName;

public class User extends JsonResponseAbstract{
	
	@SerializedName("cduser")
	private int code;
	
	@SerializedName("nmuser")
	private String name;
	
	@SerializedName("nmloginname")
	private String loginName;
	
	@SerializedName("cdrole")
	private String role;
	
	@SerializedName("blthumbnail")
	private String thumbnail;
	
	@SerializedName("nmpassword")
	private String password;

	public User(){
		
	}
	
	public User(int code, String name, String password) {
		super();
		this.code = code;
		this.name = name;
		this.password = password;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = md5(password);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	
}
