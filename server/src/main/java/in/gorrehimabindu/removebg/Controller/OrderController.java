package in.gorrehimabindu.removebg.Controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import in.gorrehimabindu.removebg.Dto.RazorpayOrderDto;
import in.gorrehimabindu.removebg.Response.RemoveBgResponse;
import in.gorrehimabindu.removebg.Service.OrderService;
import in.gorrehimabindu.removebg.Service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor

public class OrderController {
    public final OrderService orderService;
    private final RazorpayService razorpayService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam String planId, Authentication authentication) throws RazorpayException {
        Map<String, Object> responseMap = new HashMap<>();
        RemoveBgResponse response = null;

        // validation
        if (authentication.getName().isEmpty() || authentication.getName() == null) {
            RemoveBgResponse.builder()
                    .statusCode(HttpStatus.FORBIDDEN)
                    .success(false)
                    .data("User does not have permission/access to this resource for razorpay")
                    .build();

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
        }

        try {
            Order order = orderService.createOrder(planId, authentication.getName());
            RazorpayOrderDto responseDTO = convertToDTO(order);

            response = RemoveBgResponse.builder()
                    .success(true)
                    .data(responseDTO)
                    .statusCode(HttpStatus.CREATED)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(e.getMessage())
                    .success(false)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private RazorpayOrderDto convertToDTO(Order order) {
        return RazorpayOrderDto.builder()
                .id(order.get("id"))
                .entity(order.get("entity"))
                .amount(order.get("amount"))
                .currency(order.get("currency"))
                .status(order.get("status"))
                .created_at(order.get("created_at"))
                .receipt(order.get("receipt"))
                .build();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody Map<String, Object> request) throws RazorpayException {
        System.out.println("Api hitting to verify order");
        try {
            String razorpayOrderId = request.get("razorpay_order_id").toString();
            Map<String, Object> returnValue = razorpayService.verifyPayment(razorpayOrderId);

            return ResponseEntity.ok(returnValue);
        } catch (RazorpayException e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        /**
         response = RemoveBgResponse.builder()
         .success(true)
         .statusCode(HttpStatus.OK)
         .data(returnValue)
         .build();

         **/
    }
}