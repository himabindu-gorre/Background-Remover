package in.gorrehimabindu.removebg.Controller;

import in.gorrehimabindu.removebg.Dto.UserDto;
import in.gorrehimabindu.removebg.Response.RemoveBgResponse;
import in.gorrehimabindu.removebg.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// @RestController: This annotation indicates that this class is a RESTFul controller, which will handle HTTP requests
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService; // Injecting UserService to manage user-related logic

    // @PostMapping: This annotation maps POST requests to the 'createOrUpdateUser' method
    @PostMapping
    public ResponseEntity<?> createOrUpdateUser(@RequestBody UserDto userDTO, Authentication authentication) {
        System.out.println("API Hit");

        RemoveBgResponse response = null;
        try {
            System.out.println("inside try block");
            if (!authentication.getName().equals(userDTO.getClerkId())) {
                response =  RemoveBgResponse.builder()
                        .success(false)
                        .data("User does not have permission to access this resource")
                        .statusCode(HttpStatus.FORBIDDEN)
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            System.out.println("inside try block");

            UserDto user = userService.saveUser(userDTO);

            response =  RemoveBgResponse.builder()
                    .success(true) // Indicates the operation was successful
                    .data(user) // The user data after saving or updating
                    .statusCode(HttpStatus.OK) // Return status CREATED (HTTP 201)
                    .build(); // Build the response using the builder pattern
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch(Exception exception) {
            System.out.println("inside catch block");

            // Catch any exception that might occur and return an error response
            response = RemoveBgResponse.builder()
                    .success(false) // Indicates the operation failed
                    .data(exception.getMessage()) // Provide the exception message as data in the response
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR) // Return status INTERNAL_SERVER_ERROR (HTTP 500)
                    .build(); // Build the error response using the builder pattern
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/credits")
    public ResponseEntity<?> getUserCredits(Authentication authentication) {
        RemoveBgResponse bgResponse = null;
        try {
            if (authentication.getName().isEmpty() || authentication.getName() == null) {
                bgResponse = RemoveBgResponse.builder()
                        .statusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission to access this resource")
                        .success(false)
                        .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(bgResponse);
            }

            String clerkId = authentication.getName();
            UserDto existingUser = userService.getUserByClerkId(clerkId);

            Map<String, Integer> map = new HashMap<>();
            map.put("credits", existingUser.getCredits());
            bgResponse = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .data(map)
                    .success(true)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).
                    body(bgResponse);
        } catch(Exception e) {
            bgResponse = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .data("Something went wrong")
                    .success(false)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(bgResponse);
        }
    }
}
