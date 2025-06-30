package in.gorrehimabindu.removebg.Service.Impl;

import in.gorrehimabindu.removebg.Dto.UserDto;
import in.gorrehimabindu.removebg.Entity.UserEntity;
import in.gorrehimabindu.removebg.Repository.UserRepository;
import in.gorrehimabindu.removebg.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private  final UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {

        Optional<UserEntity> optionalUser = userRepository.findByClerkId(userDto.getClerkId());

        if(optionalUser.isPresent()) {

            UserEntity existingUser = optionalUser.get();

            existingUser.setEmail(userDto.getEmail());
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setPhotoUrl(userDto.getPhotoUrl());
//            existingUser.setCredits(userDto.getCredits());

            if(userDto.getCredits() != null) {
                existingUser.setCredits(userDto.getCredits());
            }
            existingUser = userRepository.save(existingUser);
            return mapToDto(existingUser);
        }
        UserEntity newUser = mapToEntity(userDto);
        userRepository.save(newUser);
        return mapToDto(newUser);
    }

    @Override
    public UserDto getUserByClerkId(String clerkId) {
       UserEntity userEntity =  userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

       return mapToDto(userEntity);
    }

    @Override
    public void deleteUserByClerkId(String clerkId) {
        UserEntity userEntity = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(userEntity);
    }

    private UserDto mapToDto(UserEntity newUser) {
       return UserDto.builder()
                .clerkId(newUser.getClerkId())
                .credits(newUser.getCredits())
                .email(newUser.getEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .build();
    }

    private UserEntity mapToEntity(UserDto userDto) {
      return UserEntity.builder()
                .clerkId(userDto.getClerkId())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .photoUrl(userDto.getPhotoUrl())
                .build();
    }
}
