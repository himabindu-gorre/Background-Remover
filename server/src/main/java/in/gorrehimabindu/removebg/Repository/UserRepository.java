package in.gorrehimabindu.removebg.Repository;

import in.gorrehimabindu.removebg.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository interface for UserEntity.
// Extends JpaRepository to provide CRUD operations and JPA functionality.
// The entity type is UserEntity and the primary key type is Long.

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByClerkId(String clerkId);

    Optional<UserEntity> findByClerkId(String clerkId);
}
