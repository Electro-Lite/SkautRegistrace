package CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="setup")
public class SetupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer year;
    private String userPwd;
    private String adminPwd;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getUserPwd() { return userPwd; }
    public void setUserPwd(String userPwd) { 
    	this.userPwd = passwordEncoder().encode(userPwd);; 
    	}

    public String getAdminPwd() { return adminPwd; }
    public void setAdminPwd(String adminPwd) { 
    	this.adminPwd = passwordEncoder().encode(adminPwd); 
    	}
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
