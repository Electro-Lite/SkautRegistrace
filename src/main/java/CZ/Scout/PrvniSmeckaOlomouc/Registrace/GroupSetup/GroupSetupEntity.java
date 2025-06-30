package CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "GroupSetup")
public class GroupSetupEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private Boolean enabled;
	private String name;
	private String day;
	private String time;
	private String leader;
	private String imageUrl;
	
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public Boolean getEnabled() { return enabled; }
	public void setEnabled(Boolean enabled) { this.enabled = enabled; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDay() { return day; }
	public void setDay(String day) { this.day = day; }

	public String getTime() { return time; }
	public void setTime(String time) { this.time = time; }

	public String getLeader() { return leader; }
	public void setLeader(String leader) { this.leader = leader; }

	public String getImageUrl() { return imageUrl; }
	public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
	
}
