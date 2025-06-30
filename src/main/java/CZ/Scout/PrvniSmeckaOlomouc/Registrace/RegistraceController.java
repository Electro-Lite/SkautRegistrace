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

@Controller
public class RegistraceController {
	@Autowired
    private UserRegistrationRepository repository;
	@Autowired
	SetupDataHelper setupDataHelper;
	
	private String[] groupNames = {"Vlci","Mungové","Mustangové", "Bobři"};
	
    @GetMapping("/rozvrh")
    public String hello(Model model) {
    	prepareOverviewModel(model);
    	return "overview.html";
    }
    
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
        	for (String groupName : groupNames) {
        		groupEntity = setupDataHelper.findOrCreateGroupSetup(groupName);
        		groupEntity.setEnabled( groups.get("groups[" + groupName + "].enabled")!=null && groups.get("groups[" + groupName + "].enabled").equals("on"));
        		groupEntity.setDay(groups.get("groups[" + groupName + "].weekday"));
        		groupEntity.setTime(groups.get("groups[" + groupName + "].time"));
        		groupEntity.setLeader(groups.get("groups[" + groupName + "].leader"));
        		groupEntity.setImageUrl(groups.get("groups[" + groupName + "].leaderImage"));
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
    
    
    @GetMapping("/login")
    public String login() {
        return "login.html"; // maps to src/main/resources/templates/login.html
    }

    @GetMapping("/scheduler")
    public String scheduler(Model model) {
    	if(isAdmin())
    	{
    		prepareSchedulerModel(model);
        	return "scheduler.html"; // maps to src/main/resources/templates/login.html
    	}
    	return "login.html";
    }
    
    @PostMapping("/setStatus")
    @Transactional
    public String setRegistrationStatus(@RequestParam(value = "id") String id,
    									@RequestParam(value = "status") String status,
    									Model model) {
    	if(isAdmin())
    	{
    		Optional<UserRegistrationEntity> optional = repository.findById(Long.parseLong(id));
    		if(optional.isPresent()) {
    	        UserRegistrationEntity registration = optional.get();
    	        registration.setStatus(status);
    	        repository.save(registration);
    	    }
    		prepareSchedulerModel(model);
        	return "scheduler.html"; // maps to src/main/resources/templates/login.html
    	}
    	return "login.html";
    }
    
    @GetMapping("/register")
    public String register(	@RequestParam(value = "group") String group,
    		Model model) {
    	model.addAttribute("user", new UserRegistrationEntity());
    	model.addAttribute("group",group);
    	return "RegistraceClen.html";
    }
    
    @PostMapping("/deleteRegistration")
    @Transactional
    public String setRegistrationStatus(@RequestParam(value = "id") String id,
    									Model model) {
    	if(isAdmin())
    	{
    		Optional<UserRegistrationEntity> optional = repository.findById(Long.parseLong(id));
    		if(optional.isPresent()) {
    	        UserRegistrationEntity registration = optional.get();
    	        repository.delete(registration);
    	    }
    		prepareSchedulerModel(model);
        	return "scheduler.html"; // maps to src/main/resources/templates/login.html
    	}
    	return "login.html";
    }
    
    @PostMapping("/submitRegistration")
    @Transactional
    public String handlePost(@RequestParam(value = "name") String name,
    						 @RequestParam(value = "parentName") String parentName,
                             @RequestParam(value = "contact") String contact,
                             @RequestParam(value = "preference") String preference,
                             @RequestParam(value = "notes", required = false) String notes,
                             @RequestParam(value = "group", required = false) String group,
                             Model model) {
        UserRegistrationEntity user = new UserRegistrationEntity();
        user.setName(name);
        user.setName(parentName);
        user.setContact(contact);
        user.setPreference(preference);
        user.setNotes(notes);
        user.setUserGroup(group);
        user.setYear(setupDataHelper.findOrCreateSetup().getYear());
        user.setStatus("pending");
        
        repository.save(user);
        
        model.addAttribute("group", group);
        model.addAttribute("showPopup", true);
        
        prepareOverviewModel(model);
        return "overview"; // no ".html" if you're returning a Thymeleaf template
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
    
    private Model prepareOverviewModel(Model model)
    {
    	List<Long> queryResult;
    	int year = setupDataHelper.findOrCreateSetup().getYear();
    	int count;
    	//Pending
    	for (String group : groupNames) {
    		count=0;
    		queryResult = repository.findIdPendingForGroup(group, year);
    		if(queryResult != null) {
    			count = queryResult.size();
    		}
    		model.addAttribute(group+"Pending", count);
    	}
    	//Approved
    	for (String group : groupNames) {
    		count=0;
    		queryResult = repository.findIdApprovedForGroup(group, year);
    		if(queryResult != null) {
    			count = queryResult.size();
    		}
    		model.addAttribute(group+"Approved", count);
    	}
    	prepareGroupSetup(model);
    	return model;
    }
    
    private Model prepareSchedulerModel(Model model)
    {
    	List<UserRegistrationEntity> userRegistration;
    	int year = setupDataHelper.findOrCreateSetup().getYear();
    	Map<String, List<UserRegistrationEntity>> mapGroups = new LinkedHashMap<>();
    	
    	for (String groupName : groupNames) {
    		
    		userRegistration = repository.findForGroup(groupName, year);
    		mapGroups.put(groupName, userRegistration);
    		model.addAttribute(groupName, userRegistration);
    	}
    	model.addAttribute("groups", mapGroups);
    	model.addAttribute("user", new UserRegistrationEntity());
    	prepareGroupSetup(model);
    	return model;
    }
    private Model prepareGroupSetup(Model model) {
    	Map<String, GroupSetupEntity> mapGroupSetup = new LinkedHashMap<>();
    	GroupSetupEntity groupSetup;
    	Integer enebledGroupCount=0;
    	for (String groupName : groupNames) {
    		groupSetup = setupDataHelper.findOrCreateGroupSetup(groupName);
    		enebledGroupCount += groupSetup.getEnabled() ? 1 : 0 ;
    		mapGroupSetup.put(groupName, groupSetup);
    	}
    	model.addAttribute("groupSetup", mapGroupSetup);
    	model.addAttribute("enabledGroupsCount", enebledGroupCount);
    	return model;
    }
    private Model prepareSetupModel(Model model)
    {
    	SetupEntity setupEntity = setupDataHelper.findOrCreateSetup();

    	model.addAttribute("user", new UserRegistrationEntity());
    	model.addAttribute("activeYear", setupEntity.getYear());
    	
    	GroupSetupEntity groupEntity;
    	for (String groupName : groupNames) {
    		groupEntity = setupDataHelper.findOrCreateGroupSetup(groupName);
    		model.addAttribute("groups_" + groupName + "_enabled", groupEntity.getEnabled());
    		model.addAttribute("groups_" + groupName + "_day", groupEntity.getDay());
    		model.addAttribute("groups_" + groupName + "_time", groupEntity.getTime());
    		model.addAttribute("groups_" + groupName + "_leader", groupEntity.getLeader());
    		model.addAttribute("groups_" + groupName + "_leaderImage", groupEntity.getImageUrl());
    	}
    	
    	return model;
    }
    
    
}
	