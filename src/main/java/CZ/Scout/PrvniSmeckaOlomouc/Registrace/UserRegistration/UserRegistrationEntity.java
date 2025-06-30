package CZ.Scout.PrvniSmeckaOlomouc.Registrace.UserRegistration;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "RegistraceClen")
public class UserRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String 	name;
    private String 	parentName;
    private String 	contact;
    private String 	preference;
    private String 	status;
    private String 	userUserGroup;
    private Integer year;


	// Auto generated
    @PrePersist
    protected void onCreate() {
    	this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;};
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getparentName() { return parentName; }
    public void setparentName(String parentName) { this.parentName = parentName; }
    
    public String getContact() {return contact; }
    public void setContact(String contact) {this.contact = contact;}

    public String getPreference() {return preference;}
    public void setPreference(String preference) {this.preference = preference;}

    public String getNotes() {return notes;}
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getStatus() {return status;}
    public void setStatus(String status) { this.status = status; }

    public String getUserGroup() {return userUserGroup;}
    public void setUserGroup(String userUserGroup) { this.userUserGroup = userUserGroup; }
    
    public Integer getYear() {return year;}
	public void setYear(Integer year) {this.year = year;}
}
