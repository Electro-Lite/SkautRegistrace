package CZ.Scout.PrvniSmeckaOlomouc.Registrace.UserRegistration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistrationEntity, Long> {
	
    // Custom query
    @Query("SELECT u.id FROM UserRegistrationEntity u WHERE u.status = \"pending\" AND u.userUserGroup = :groupName AND u.year = :year")
    List<Long> findIdPendingForGroup(@Param("groupName") String groupName, @Param("year") Integer year);
    
    // Custom query
    @Query("SELECT u.id FROM UserRegistrationEntity u WHERE u.status = \"approved\" AND u.userUserGroup = :groupName AND u.year = :year")
    List<Long> findIdApprovedForGroup(@Param("groupName") String groupName, @Param("year") Integer year);
    
    // Custom query
    @Query("SELECT u FROM UserRegistrationEntity u WHERE u.userUserGroup = :groupName AND u.year = :year")
    List<UserRegistrationEntity> findForGroup(@Param("groupName") String groupName, @Param("year") Integer year);
}
