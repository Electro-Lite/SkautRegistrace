package CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/**
 * Repository for performing CRUD operations on {@link GroupSetupEntity}.
 */
@Repository
public interface GroupSetupRepository extends JpaRepository<GroupSetupEntity, Long>{
	/**
     * Finds a group setup entity by its ID.
     *
     * @param groupName the ID of the group
     * @return the corresponding {@link GroupSetupEntity}, or {@code null} if not found
     */
    @Query("SELECT u FROM GroupSetupEntity u WHERE u.id = :groupId")
    GroupSetupEntity findIdPendingForGroup(@Param("groupId") String groupName);
}
