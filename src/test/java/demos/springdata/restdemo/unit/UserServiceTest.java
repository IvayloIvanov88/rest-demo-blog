package demos.springdata.restdemo.unit;

import demos.springdata.restdemo.exception.InvalidEntityException;
import demos.springdata.restdemo.model.User;
import demos.springdata.restdemo.repository.UserRepository;
import demos.springdata.restdemo.service.UserService;
import demos.springdata.restdemo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private User testUser;
    private UserService userService;
    @Mock
    private UserRepository mockedUserRepository;


    @BeforeEach
    public void init() {
        userService = new UserServiceImpl(mockedUserRepository);

        testUser = new User();
        testUser.setId(123L);
        testUser.setUsername("Random user");
        testUser.setPassword("1234");
        mockedUserRepository.save(testUser);

    }

    @Test
    public void getUserWithCorrectUsernameShouldReturnCorrect() {
        Mockito.when(mockedUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.ofNullable(testUser));

        User actual = userService.getUserByUsername(testUser.getUsername());

        Assertions.assertEquals(testUser.getUsername(), actual.getUsername());
        Assertions.assertEquals(testUser.getId(), actual.getId());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }


    @Test
    public void getUserByIdShouldReturnCorrectUser() {
        Mockito.when(mockedUserRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));

        User actual = userService.getUserById(testUser.getId());

        Assertions.assertEquals(testUser.getUsername(), actual.getUsername());
        Assertions.assertEquals(testUser.getId(), actual.getId());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    public void createUserShouldCrateUser() {
        User user = new User();
        user.setUsername("User");
        user.setId(666L);
        user.setPassword("123");

        Mockito.when(mockedUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        Mockito.when(mockedUserRepository.save(user)).thenReturn(user);


        User actual = userService.createUser(user);

        Assertions.assertEquals(user.getUsername(), actual.getUsername());
        Assertions.assertEquals(user.getId(), actual.getId());

    }

    @Test
    public void createUserShouldThrowInvalidEntityExceptionIfUserIsAlreadyExist() throws InvalidEntityException {

        Mockito.when(mockedUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(InvalidEntityException.class, () -> userService.createUser(testUser));
    }
}
