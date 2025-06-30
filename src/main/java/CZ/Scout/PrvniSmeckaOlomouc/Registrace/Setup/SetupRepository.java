package CZ.Scout.PrvniSmeckaOlomouc.Registrace.Setup;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SetupRepository extends JpaRepository<SetupEntity, Long>{
	@Query(value = "SELECT * FROM setup LIMIT 1", nativeQuery = true)
	SetupEntity findFirst();
}
