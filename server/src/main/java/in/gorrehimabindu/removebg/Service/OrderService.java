package in.gorrehimabindu.removebg.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

public interface OrderService {

    Order createOrder(String planId, String clerkId) throws RazorpayException;

}
