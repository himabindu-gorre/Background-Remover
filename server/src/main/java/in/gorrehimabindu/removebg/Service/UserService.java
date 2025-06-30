package in.gorrehimabindu.removebg.Service;

import in.gorrehimabindu.removebg.Dto.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto getUserByClerkId(String clerkId);

    void deleteUserByClerkId(String clerkId);
}
