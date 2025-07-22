package CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup;

import java.time.Year;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup.GroupSetupEntity;
import CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup.GroupSetupRepository;

/**
 * Helper component for initializing and retrieving setup data.
 */
@Component
public class SetupDataHelper {
	@Autowired
	public SetupRepository 		setupRepository;
	@Autowired
	public GroupSetupRepository groupSetupRepository;
	/** Predefined group identifiers used during initialization. */
	public static String[] groupIds = {"G1","G2","G3", "G4"};
	
	/**
	 * Retrieves the first {@link SetupEntity} or creates a new one if not found.
	 *
	 * @return an existing or newly created {@link SetupEntity}
	 */
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
	
	/**
	 * Retrieves the {@link GroupSetupEntity} for the given group ID, or creates a new one if not found.
	 *
	 * @param groupId the group ID
	 * @return an existing or newly created {@link GroupSetupEntity}
	 */
	public GroupSetupEntity findOrCreateGroupSetup(String groupId) {
		GroupSetupEntity groupSetupEntity;
		
		Optional<GroupSetupEntity> optional = Optional.ofNullable(groupSetupRepository.findIdPendingForGroup(groupId));
		if(optional.isPresent()) {
			groupSetupEntity = optional.get();
	    }
		else{
			groupSetupEntity = new GroupSetupEntity();
			groupSetupEntity.setId(groupId);
		}
		
		return groupSetupEntity;
	}
	
	/**
	 * Initializes default data for setup and group configuration if the tables are empty.
	 */
    public void initializeSetupData() {
        if (groupSetupRepository.count() == 0) {
            for (String groupId : groupIds) {
                GroupSetupEntity groupSetupEntity = new GroupSetupEntity();
                groupSetupEntity.setId(groupId);
                groupSetupEntity.setName("Není nastaveno");
                groupSetupEntity.setDay("Pondělí");
                groupSetupEntity.setEnabled(true);
                groupSetupEntity.setTime("Není nastaveno");
                groupSetupEntity.setLeader("Není nastaveno");
                groupSetupEntity.setCoreColor("#E4C44C");
                groupSetupEntity.setImageUrl("https://th.bing.com/th/id/R.a11170b13c4e1e960977d9530f110961?rik=eOpW4YG3yFxpag&riu=http%3a%2f%2fwww.2ndtoowoombascouts.com.au%2fuploads%2fpics%2fboyscout-boy-scout-scout-neckerchief-smiley-emoticon-000692-large.gif&ehk=s%2b7jxn60u%2bE7S66%2f8koGAD99chRkjS7LP9YDBeYynIE%3d&risl=&pid=ImgRaw&r=0");
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
