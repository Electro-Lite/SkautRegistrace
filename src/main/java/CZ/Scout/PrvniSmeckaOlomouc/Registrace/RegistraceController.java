package CZ.Scout.PrvniSmeckaOlomouc.Registrace;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup.GroupSetupEntity;
import CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup.SetupDataHelper;
import CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup.SetupEntity;
import CZ.Scout.PrvniSmeckaOlomouc.Registrace.UserRegistration.UserRegistrationEntity;
import CZ.Scout.PrvniSmeckaOlomouc.Registrace.UserRegistration.UserRegistrationRepository;
/**
 * Main Spring MVC controller for handling registration, group setup,
 * and administrative functions of the scout group scheduling system.
 *
 * <p>Provides endpoints for users and admins to view schedules,
 * register for groups, and manage registration requests.</p>
 */
@Controller
public class RegistraceController {
	/** Repository interface for handling user registration persistence. */
	@Autowired
    private UserRegistrationRepository userRegRepository;
	/** Utility class for accessing and modifying setup and group configuration data. */
	@Autowired
	SetupDataHelper setupDataHelper;
	/** List of supported group IDs. */
	private String[] groupIds = SetupDataHelper.groupIds;
	 /**
     * Displays the main overview page for users, showing available groups and registration stats.
     *
     * @param model the UI model to populate
     * @return the overview HTML page
     */
    @GetMapping("/rozvrh")
    public String hello(Model model) {
    	prepareOverviewModel(model);
    	return "overview.html";
    }
    /**
     * Redirects the user to their default homepage based on their role (admin or user).
     *
     * @param model the UI model to populate
     * @return the default HTML page depending on access level
     */
    @GetMapping("/loginMapper")
    public String loginMapper(Model model) {
    	if(isAdmin()){    		
    		prepareSchedulerModel(model);
    		return "scheduler.html";
    	}
    	else {
    		prepareOverviewModel(model);
    		return "overview.html";
		}
    }
    /**
     * Displays the admin setup page for managing year, group information, and credentials.
     *
     * @param model the UI model to populate
     * @return the admin setup HTML page, or login page if not authorized
     */
    @GetMapping("/setup")
    public String setup(Model model) {
    	if(isAdmin()){    		
    		prepareSetupModel(model);
    		return "setup.html";
    	}
    	else {
    		return "login.html";
		}
    }
    /**
     * Handles the form submission from the setup page to apply configuration changes.
     *
     * @param groups the request parameters from the setup form
     * @param model the UI model to populate
     * @return the updated admin setup HTML page
     */
    @PostMapping("/saveSetup")
    public String saveSetup(@RequestParam Map<String, String> groups,
    						Model model) {
    	if(isAdmin()){    		
        	GroupSetupEntity groupEntity;
    		SetupEntity setupEntity = setupDataHelper.findOrCreateSetup();
    		String adminPwd = groups.get("userPassword");
    		String userPwd = groups.get("userPassword");
    		setupEntity.setYear(Integer.parseInt( groups.get("activeYear")));
    		if (adminPwd != "" && adminPwd!= null)
    		{
    			setupEntity.setAdminPwd(adminPwd);
    		}
    		if (userPwd != "" && userPwd!= null)
    		{
    			setupEntity.setUserPwd(userPwd);    			
    		}
        	for (String groupId : groupIds) {
        		groupEntity = setupDataHelper.findOrCreateGroupSetup(groupId);
        		groupEntity.setEnabled( groups.get("groups[" + groupId + "].enabled")!=null && groups.get("groups[" + groupId + "].enabled").equals("on"));
        		groupEntity.setName(groups.get("groups[" + groupId + "].name"));
        		groupEntity.setDay(groups.get("groups[" + groupId + "].weekday"));
        		groupEntity.setTime(groups.get("groups[" + groupId + "].time"));
        		groupEntity.setLeader(groups.get("groups[" + groupId + "].leader"));
        		groupEntity.setCoreColor(groups.get("groups[" + groupId + "].coreColor"));
        		groupEntity.setImageUrl(groups.get("groups[" + groupId + "].leaderImage"));
        		setupDataHelper.groupSetupRepository.save(groupEntity);
        	}
    		setupDataHelper.setupRepository.save(setupEntity);
    		prepareSetupModel(model);
    		return "setup.html";
    	}
    	else {
    		return "login.html";
		}
    }
    /**
     * Displays the login page.
     * Use the query parameter {@code ?login=admin} to log in with different credentials.
     *
     * @return the login HTML page
     */
    @GetMapping("/login")
    public String login() {
        return "login.html"; // maps to src/main/resources/templates/login.html
    }
    /**
     * Displays the admin scheduler page with grouped registration requests.
     *
     * @param model the UI model to populate
     * @return the scheduler HTML page, or login page if unauthorized
     */
    @GetMapping("/scheduler")
    public String scheduler(Model model) {
    	if(isAdmin())
    	{
    		prepareSchedulerModel(model);
        	return "scheduler.html"; // maps to src/main/resources/templates/login.html
    	}
    	return "login.html";
    }
    /**
     * Updates the status of a user registration request.
     *
     * @param id the registration ID
     * @param status the new status to apply
     * @param model the UI model to populate
     * @return the updated scheduler HTML page
     */
    @PostMapping("/setStatus")
    @Transactional
    public String setRegistrationStatus(@RequestParam(value = "id") String id,
    									@RequestParam(value = "status") String status,
    									Model model) {
    	if(isAdmin())
    	{
    		Optional<UserRegistrationEntity> optional = userRegRepository.findById(Long.parseLong(id));
    		if(optional.isPresent()) {
    	        UserRegistrationEntity registration = optional.get();
    	        registration.setStatus(status);
    	        userRegRepository.save(registration);
    	    }
    		prepareSchedulerModel(model);
        	return "scheduler.html"; // maps to src/main/resources/templates/login.html
    	}
    	return "login.html";
    }
    /**
     * Displays the registration form for a given group.
     *
     * @param group the group ID
     * @param model the UI model to populate
     * @return the registration form HTML page
     */
    @GetMapping("/register")
    public String register(	@RequestParam(value = "group") String group,
    		Model model) {
    	model.addAttribute("user", new UserRegistrationEntity());
    	GroupSetupEntity groupSetupEntity = setupDataHelper.findOrCreateGroupSetup(group); //Group should be validated...
    	model.addAttribute("group", groupSetupEntity);
    	prepareGroupSetup(model);
    	return "RegistraceClen.html";
    }
    /**
     * Deletes a registration request from the database.
     *
     * @param id the registration ID
     * @param model the UI model to populate
     * @return the updated scheduler HTML page
     */
    @PostMapping("/deleteRegistration")
    @Transactional
    public String setRegistrationStatus(@RequestParam(value = "id") String id,
    									Model model) {
    	if(isAdmin())
    	{
    		Optional<UserRegistrationEntity> optional = userRegRepository.findById(Long.parseLong(id));
    		if(optional.isPresent()) {
    	        UserRegistrationEntity registration = optional.get();
    	        userRegRepository.delete(registration);
    	    }
    		prepareSchedulerModel(model);
        	return "scheduler.html"; // maps to src/main/resources/templates/login.html
    	}
    	return "login.html";
    }
    /**
     * Submits a new registration request and stores it in the database.
     *
     * @param name registrant's name
     * @param parentName name of the parent
     * @param contact contact information
     * @param preference group preference note
     * @param notes additional notes (optional)
     * @param groupId target group ID
     * @param model the UI model to populate
     * @return the updated overview page with confirmation popup
     */
    @PostMapping("/submitRegistration")
    @Transactional
    public String handlePost(@RequestParam(value = "name") String name,
    						 @RequestParam(value = "parentName") String parentName,
                             @RequestParam(value = "contact") String contact,
                             @RequestParam(value = "preference") String preference,
                             @RequestParam(value = "notes", required = false) String notes,
                             @RequestParam(value = "groupId", required = false) String groupId,
                             Model model) {
        UserRegistrationEntity user = new UserRegistrationEntity();
        user.setName(name);
        user.setParentName(parentName);
        user.setContact(contact);
        user.setPreference(preference);
        user.setNotes(notes);
        user.setUserGroup(groupId);
        user.setYear(setupDataHelper.findOrCreateSetup().getYear());
        user.setStatus("pending");
        
        userRegRepository.save(user);
        
        model.addAttribute("group", groupId);
        model.addAttribute("showPopup", true);
        
        prepareOverviewModel(model);
        return "overview"; // no ".html" if you're returning a Thymeleaf template
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
    /**
     * Checks if the currently authenticated user has admin privileges.
     *
     * @return true if the user has ROLE_ADMIN, false otherwise
     */
    private Model prepareOverviewModel(Model model)
    {
    	List<Long> queryResult;
    	int year = setupDataHelper.findOrCreateSetup().getYear();
    	int count;
    	//Pending
    	for (String groupId : groupIds) {
    		count=0;
    		queryResult = userRegRepository.findIdPendingForGroup(groupId, year);
    		if(queryResult != null) {
    			count = queryResult.size();
    		}
    		model.addAttribute(groupId+"Pending", count);
    	}
    	//Approved
    	for (String groupId : groupIds) {
    		count=0;
    		queryResult = userRegRepository.findIdApprovedForGroup(groupId, year);
    		if(queryResult != null) {
    			count = queryResult.size();
    		}
    		model.addAttribute(groupId+"Approved", count);
    	}
    	prepareGroupSetup(model);
    	return model;
    }
    /**
     * Prepares the model attributes for the user overview page.
     * @param model
     * @return updated model
     */
    private Model prepareSchedulerModel(Model model)
    {
    	List<UserRegistrationEntity> userRegistration;
    	int year = setupDataHelper.findOrCreateSetup().getYear();
    	Map<String, List<UserRegistrationEntity>> mapGroups = new LinkedHashMap<>();
    	
    	for (String groupId : groupIds) {
    		
    		userRegistration = userRegRepository.findForGroup(groupId, year);
    		mapGroups.put(groupId, userRegistration);
    		model.addAttribute(groupId, userRegistration);
    	}
    	model.addAttribute("groups", mapGroups);
    	prepareGroupSetup(model);
    	return model;
    }
    /**
     * Prepares the model attributes for the scheduler (admin) page.
     * @param model
     * @return updated model
     */
    private Model prepareGroupSetup(Model model) {
    	Map<String, GroupSetupEntity> mapGroupSetup = new LinkedHashMap<>();
    	GroupSetupEntity groupSetup;
    	Integer enebledGroupCount=0;
    	for (String groupId : groupIds) {
    		groupSetup = setupDataHelper.findOrCreateGroupSetup(groupId);
    		enebledGroupCount += groupSetup.getEnabled() ? 1 : 0 ;
    		mapGroupSetup.put(groupId, groupSetup);
    	}
    	model.addAttribute("groupSetup", mapGroupSetup);
    	model.addAttribute("enabledGroupsCount", enebledGroupCount);
    	return model;
    }
    /**
     * Prepares the model attributes for the admin setup page
     * @param model
     * @return updated model
     */
    private Model prepareSetupModel(Model model)
    {
    	SetupEntity setupEntity = setupDataHelper.findOrCreateSetup();

    	model.addAttribute("user", new UserRegistrationEntity());
    	model.addAttribute("activeYear", setupEntity.getYear());
    	
    	GroupSetupEntity groupEntity;
    	for (String groupId : groupIds) {
    		groupEntity = setupDataHelper.findOrCreateGroupSetup(groupId);
    		model.addAttribute("groups_" + groupId + "_name", groupEntity.getName());
    		model.addAttribute("groups_" + groupId + "_enabled", groupEntity.getEnabled());
    		model.addAttribute("groups_" + groupId + "_day", groupEntity.getDay());
    		model.addAttribute("groups_" + groupId + "_time", groupEntity.getTime());
    		model.addAttribute("groups_" + groupId + "_leader", groupEntity.getLeader());
    		model.addAttribute("groups_" + groupId + "_coreColor", groupEntity.getCoreColor());
    		model.addAttribute("groups_" + groupId + "_leaderImage", groupEntity.getImageUrl());
    	}
    	
    	return model;
    }
    
    
}
	