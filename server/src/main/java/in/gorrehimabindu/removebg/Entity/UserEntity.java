package in.gorrehimabindu.removebg.Entity;

// This class represents the User entity mapped to the 'tbl_users' table in the database.
// Equivalent to a Mongoose schema in MongoDB but here using JPA for relational DB.
// Lombok is used to reduce boilerplate code by auto-generating getters, setters, constructors, builder, etc.
// Jakarta Persistence API (JPA) annotations are used to define the entity and its mappings.

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity  // Marks this class as a JPA entity mapped to a database table
@Table(name = "tbl_users") // Specifies the table name in the database
@Data  // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@Builder  // Lombok annotation to implement the builder pattern for easy object creation

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clerkId;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;
    private Integer credits;
    private String photoUrl;


    @PrePersist
    public void  prePersist() {
        if(credits == null) {
            credits = 5;
        }
    }
}
