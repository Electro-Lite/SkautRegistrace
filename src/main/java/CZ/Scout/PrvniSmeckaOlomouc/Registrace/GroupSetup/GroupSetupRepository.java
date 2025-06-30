package CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSetupRepository extends JpaRepository<GroupSetupEntity, Long>{
    @Query("SELECT u FROM GroupSetupEntity u WHERE u.name = :groupName")
    GroupSetupEntity findIdPendingForGroup(@Param("groupName") String groupName);
}
