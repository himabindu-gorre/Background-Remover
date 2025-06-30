package in.gorrehimabindu.removebg.Controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gorrehimabindu.removebg.Dto.UserDto;
import in.gorrehimabindu.removebg.Response.RemoveBgResponse;
import in.gorrehimabindu.removebg.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor

public class ClerkWebHookController {

    @Value("${clerk.webhook.secret}")
    private String webhookSecret;

    private final UserService userService;

    @PostMapping("/clerk")
    public ResponseEntity<?> handlerClerkWebhook(@RequestHeader("svix-id") String svixId,
                                                 @RequestHeader("svix-timestamp") String svixTimeStamp,
                                                 @RequestHeader("svix-signature") String svixSignature,
                                                 @RequestBody String payload) {

        RemoveBgResponse response = null;

        try {
            boolean isValid = verifyWebhookSignature(svixId, svixTimeStamp, svixSignature, payload);

            if(!isValid) {
                response = RemoveBgResponse.builder()
                        .statusCode(HttpStatus.UNAUTHORIZED)
                        .data("Invalid webhook signature")
                        .success(false)
                        .build();
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);

            String eventType = rootNode.path("type").asText();

            switch (eventType) {
                case "user.created":
                    handleUserCreated(rootNode.path("data"));
                    break;
                case "user.updated":
                    handleUserUpdated(rootNode.path("data"));
                    break;
                case "user.deleted":
                    handleUserDeleted(rootNode.path("data"));
                    break;
            }
            return  ResponseEntity.ok().build();
        } catch (Exception e) {
            response = RemoveBgResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .data("Something went wrong")
                                            .success(false)
                                                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

    }

    private void handleUserDeleted(JsonNode data) {
        //TODO: add the implementation later

        String clerkId = data.path("od").asText();
        userService.deleteUserByClerkId(clerkId);
    }

    private void handleUserUpdated(JsonNode data) {
        System.out.println("Data getting updated in the clerk webhook controller");
        String clerkId = data.path("id").asText();
        UserDto existingUser = userService.getUserByClerkId(clerkId);

        existingUser.setEmail(data.path("email_addresses").path(0).path("email_address").asText());
        existingUser.setFirstName(data.path("first_name").asText());
        existingUser.setLastName(data.path("last_name").asText());
        existingUser.setPhotoUrl(data.path("image_url").asText());

        userService.saveUser(existingUser);

    }

    private void handleUserCreated(JsonNode data) {

       UserDto newUser =  UserDto.builder()
                .clerkId(data.path("id").asText())
                .email(data.path("email_addresses").path(0).path("email_address").asText())
                .firstName(data.path("first_name").asText())
                .lastName(data.path("last_name").asText())
                .build();

       userService.saveUser(newUser);
    }

    private boolean verifyWebhookSignature(String svixId, String timestamp, String signature, String payload) {
        try {
            Webhook webhook = new Webhook(webhookSecret);

            // Build HttpHeaders from svix headers
            Map<String, List<String>> headersMap = new HashMap<>();
            headersMap.put("svix-id", List.of(svixId));
            headersMap.put("svix-timestamp", List.of(timestamp));
            headersMap.put("svix-signature", List.of(signature));

            HttpHeaders headers = HttpHeaders.of(headersMap, (k, v) -> true);

            webhook.verify(payload, headers); // throws if invalid
            return true;
        } catch (WebhookVerificationException e) {
            System.out.println("Webhook verification failed: " + e.getMessage());
            return false;
        }

        // return true;
    }
}
