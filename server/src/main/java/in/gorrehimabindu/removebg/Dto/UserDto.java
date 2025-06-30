package in.gorrehimabindu.removebg.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {
    // Data Transfer Object (DTO) is used to transfer data between layers (e.g., Controller -> Service)
    // Similar to how in MERN you define a JSON schema for data transfer in API requests/responses

    private String clerkId;
    private String email;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private Integer credits;
}
