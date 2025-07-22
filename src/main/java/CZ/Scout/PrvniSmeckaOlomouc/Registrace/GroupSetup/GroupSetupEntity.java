package CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing configuration for a user group.
 */
@Entity
@Table(name = "GroupSetup")
public class GroupSetupEntity {
	/**
     * Unique identifier for the group.
     */
	@Id
    private String id;

	private Boolean enabled;
	private String name;
	private String day;
	private String time;
	private String leader;
	private String coreColor; //Core color
	private String lightColor;
	private String hoverColor;
	private String hoverLightColor;
	
	/**
     * URL to an image representing the group.
     */
	@Column(columnDefinition = "TEXT")
	private String imageUrl;
	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
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
	
	public String getCoreColor() { return coreColor; }
	/**
     * Sets the core color and automatically computes
     * light, hover, and hover-light variants.
     *
     * @param colorHex the core color in hex format
     */
	public void setCoreColor(String colorHex) {
	    this.coreColor = colorHex;

	    float[] hsl = ColorUtils.hexToHsl(colorHex);

	    // lightColor = desaturated and lightened version
	    float[] lightHsl = hsl.clone();
//	    lightHsl[0] = lightHsl[0] - 0.1f; // reduce hue
	    lightHsl[1] *= 0.50f; // reduce saturation
	    lightHsl[2] = 0.93f; // increase lightness

	    // hoverColor = darken core
	    float[] hoverHsl = hsl.clone();
	    hoverHsl[2] = Math.max(0.0f, hoverHsl[2] - 0.075f); // darken

	    // hoverLightColor = slightly darker lightColor
	    float[] hoverLightHsl = lightHsl.clone();
	    hoverLightHsl[1] *= 1.25f; // increase saturation
	    hoverLightHsl[2] = Math.max(0.0f, hoverLightHsl[2] - 0.05f);

	    // Convert back to hex
	    this.lightColor = ColorUtils.hslToHex(lightHsl);
	    this.hoverColor = ColorUtils.hslToHex(hoverHsl);
	    this.hoverLightColor = ColorUtils.hslToHex(hoverLightHsl);
	}
	
	public String getLightColor() {return lightColor;}
	protected void setLightColor(String lightColor) {this.lightColor = lightColor;}

	public String getHoverColor() {return hoverColor;}
	protected void setHoverColor(String hoverColor) {this.hoverColor = hoverColor;}

	public String getHoverLightColor() {return hoverLightColor;}
	protected void setHoverLightColor(String hoverLightColor) {this.hoverLightColor = hoverLightColor;}

	
}
