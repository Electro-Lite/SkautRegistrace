package CZ.Scout.PrvniSmeckaOlomouc.Registrace.UserRegistration;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing user registration data.
 */
@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistrationEntity, Long> {
	/**
     * Retrieves the IDs of users with 'pending' status in the given group and year.
     *
     * @param groupName the ID of the group
     * @param year the registration year
     * @return list of user IDs
     */
    @Query("SELECT u.id FROM UserRegistrationEntity u WHERE u.status = 'pending' AND u.userGroup = :groupId AND u.year = :year")
    List<Long> findIdPendingForGroup(@Param("groupId") String groupName, @Param("year") Integer year);

    /**
     * Retrieves the IDs of users with 'approved' status in the given group and year.
     *
     * @param groupName the ID of the group
     * @param year the registration year
     * @return list of user IDs
     */
    @Query("SELECT u.id FROM UserRegistrationEntity u WHERE u.status = 'approved' AND u.userGroup = :groupId AND u.year = :year")
    List<Long> findIdApprovedForGroup(@Param("groupId") String groupName, @Param("year") Integer year);

    /**
     * Retrieves all user registrations for the specified group and year.
     *
     * @param groupName the ID of the group
     * @param year the registration year
     * @return list of user registration entities
     */
    @Query("SELECT u FROM UserRegistrationEntity u WHERE u.userGroup = :groupId AND u.year = :year")
    List<UserRegistrationEntity> findForGroup(@Param("groupId") String groupName, @Param("year") Integer year);
}
