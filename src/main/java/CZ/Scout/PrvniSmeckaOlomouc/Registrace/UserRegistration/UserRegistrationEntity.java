package CZ.Scout.PrvniSmeckaOlomouc.Registrace.UserRegistration;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a user registration entry.
 * Maps to the "RegistraceClen" table in the database.
 */
@Entity
@Table(name = "RegistraceClen")
public class UserRegistrationEntity {
	/** Primary key for the registration entry. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when the registration entry was created.
     * Automatically set before persisting.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Additional notes submitted with the registration.
     */
    @Column(columnDefinition = "TEXT")
    private String notes;
    /** Name of the registering person (typically the child). */
    private String 	name;
    /** Name of the parent or legal guardian. */
    private String 	parentName;
    /** Contact information for the parent or guardian. */
    private String 	contact;
    /** Indicates the suitability of the selected group (e.g., preferred time, compatibility). */
    private String 	preference;
    /** Current status of the registration (e.g., "pending", "approved", "rejected"). */
    private String 	status;
    /** Identifier of the group the person is registering to. */
    private String 	userGroup;
    /** The registration year. */
    private Integer year;
    
    /** Automatically sets the creation timestamp when the entity is persisted. */
	// Auto generated
    @PrePersist
    protected void onCreate() {
    	this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    /** 
     * Gets the ID of the registration.
     * @return the ID
     */
    public Long getId() { return id; }
    /**
     * Sets the ID of the registration.
     * @param id the ID to set
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the creation timestamp.
     * @return creation time
     */
    public LocalDateTime getCreatedAt() {return createdAt;}
    /**
     * Sets the creation timestamp.
     * @param createdAt creation time
     */
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;};
    
    /**
     * Gets the registrant's name.
     * @return name
     */
    public String getName() { return name; }
    /**
     * Sets the registrant's name.
     * @param name registrant name
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Gets the parent's name.
     * @return parent's name
     */
    public String getParentName() { return parentName; }
    /**
     * Sets the parent's name.
     * @param parentName parent's name
     */
    public void setParentName(String parentName) { this.parentName = parentName; }
    
    /**
     * Gets the contact information.
     * @return contact info
     */
    public String getContact() {return contact; }
    /**
     * Sets the contact information.
     * @param contact contact info
     */
    public void setContact(String contact) {this.contact = contact;}

    /**
     * Gets the group preference.
     * @return preference
     */
    public String getPreference() {return preference;}
    /**
     * Sets the group preference.
     * @param preference preference value
     */
    public void setPreference(String preference) {this.preference = preference;}

    /**
     * Gets the notes.
     * @return additional notes
     */
    public String getNotes() {return notes;}
    /**
     * Sets the notes.
     * @param notes additional notes
     */
    public void setNotes(String notes) { this.notes = notes; }
    
    /**
     * Gets the registration status.
     * @return status
     */
    public String getStatus() {return status;}
    /**
     * Sets the registration status.
     * @param status status value
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Gets the group ID.
     * @return group identifier
     */
    public String getUserGroup() {return userGroup;}
    /**
     * Sets the group ID.
     * @param userUserGroup group identifier
     */
    public void setUserGroup(String userUserGroup) { this.userGroup = userUserGroup; }
    
    /**
     * Gets the registration year.
     * @return year
     */
    public Integer getYear() {return year;}
    /**
     * Sets the registration year.
     * @param year year value
     */
	public void setYear(Integer year) {this.year = year;}
	
}
