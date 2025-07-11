package in.gorrehimabindu.removebg.Service.Impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.gorrehimabindu.removebg.Dto.UserDto;
import in.gorrehimabindu.removebg.Entity.OrderEntity;
import in.gorrehimabindu.removebg.Repository.OrderRepository;
import in.gorrehimabindu.removebg.Service.RazorpayService;
import in.gorrehimabindu.removebg.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class RazorpayServiceImpl implements RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final OrderRepository orderRepository;
    private final UserService userService;

    @Override
    public Order createOrder(Double amount, String currency) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", "order_rcptid" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1);

            return  razorpayClient.orders.create(orderRequest);
        }
        catch (RazorpayException e) {
            e.printStackTrace();
            throw new RazorpayException("Razorpay error:" + e.getMessage());

        }
    }

    @Override
    public Map<String, Object> verifyPayment(String razorpayOrderId) throws RazorpayException {
        Map<String, Object> returnValue = new HashMap<>();

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            Order orderInfo = razorpayClient.orders.fetch(razorpayOrderId);
            if (orderInfo.get("status").toString().equalsIgnoreCase("paid")) {
                OrderEntity existingOrder = orderRepository.findByOrderId(razorpayOrderId)
                        .orElseThrow(() -> new RuntimeException(("Order not found:" + razorpayOrderId)));

                if (existingOrder.getPayment()) {
                    returnValue.put("success", false);
                    returnValue.put("message", "Payment failed");

                    return returnValue;
                }

                UserDto userDto = userService.getUserByClerkId(existingOrder.getClerkId());
                userDto.setCredits(userDto.getCredits() + existingOrder.getCredits());

                userService.saveUser(userDto);
                existingOrder.setPayment(true);

                orderRepository.save(existingOrder);
                returnValue.put("success", true);
                returnValue.put("message", "Credits Added");
                return returnValue;
            }
        }
        catch (RazorpayException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while verifying the payment");
        }
        return returnValue;
    }
}
