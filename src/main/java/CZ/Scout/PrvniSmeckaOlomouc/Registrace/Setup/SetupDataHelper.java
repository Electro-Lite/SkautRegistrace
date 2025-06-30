package CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup;

import java.time.Year;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup.GroupSetupEntity;
import CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup.GroupSetupRepository;

@Component
public class SetupDataHelper {
	@Autowired
	public SetupRepository 		setupRepository;
	@Autowired
	public GroupSetupRepository groupSetupRepository;
	
	public SetupEntity findOrCreateSetup() {
		SetupEntity setupEntity;
		
		Optional<SetupEntity> optional = Optional.ofNullable(setupRepository.findFirst());
		if(optional.isPresent()) {
			setupEntity = optional.get();
	    }
		else{
			setupEntity = new SetupEntity();
			setupRepository.save(setupEntity);
		}
		
		return setupEntity;
	}
	
	public GroupSetupEntity findOrCreateGroupSetup(String groupName) {
		GroupSetupEntity groupSetupEntity;
		
		Optional<GroupSetupEntity> optional = Optional.ofNullable(groupSetupRepository.findIdPendingForGroup(groupName));
		if(optional.isPresent()) {
			groupSetupEntity = optional.get();
	    }
		else{
			groupSetupEntity = new GroupSetupEntity();
			groupSetupEntity.setName(groupName);
			groupSetupRepository.save(groupSetupEntity);
		}
		
		return groupSetupEntity;
	}
	
    public void initializeSetupData() {
        if (groupSetupRepository.count() == 0) {
            String[] groupNames = {"Vlci","Mungové","Mustangové", "Bobři"};
            for (String groupName : groupNames) {
                GroupSetupEntity groupSetupEntity = new GroupSetupEntity();
                groupSetupEntity.setName(groupName);
                groupSetupEntity.setDay("Pondělí");
                groupSetupEntity.setEnabled(true);
                groupSetupEntity.setImageUrl("Není nastaveno");
                groupSetupEntity.setLeader("Není nastaveno");
                groupSetupEntity.setTime("Není nastaveno");
                groupSetupRepository.save(groupSetupEntity);
            }
            System.err.println("Group Setup initialized with default values.");
        }
        if (setupRepository.count() == 0) {
            SetupEntity setupEntity = new SetupEntity();
            setupEntity.setAdminPwd("");
            setupEntity.setUserPwd("");
            setupEntity.setYear(Year.now().getValue());
            setupRepository.save(setupEntity);
            System.err.println("Setup initialized with default values.");
        }
    }
}
