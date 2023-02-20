package unitTest;

import com.book.dto.UserDTO;
import com.book.model.UsersEntity;
import com.book.repository.UserRepository;
import com.book.service.UserService;
import com.book.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class UserTest {
    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UsersEntity usersEntity;
    private String jwtToken;
    private String authHeader;

    @BeforeClass
    public void setUp() {
        userRepository = mock(UserRepository.class);
        jwtUtils = mock(JwtUtils.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder, jwtUtils);
        usersEntity = new UsersEntity();
        usersEntity.setUserName("testuser");
        usersEntity.setPassword("testpassword");
        jwtToken = "fakeJwtToken";
        authHeader = "Bearer " + jwtToken;
    }

    @Test
    public void RegisterTestSuccessful() {
        when(userRepository.existsByUserName(usersEntity.getUserName())).thenReturn(false);
        when(passwordEncoder.encode(usersEntity.getPassword())).thenReturn("testpassword");
        when(userRepository.save(usersEntity)).thenReturn(usersEntity);

        assertTrue(userService.registerUser(usersEntity).isPresent());
    }

    @Test
    public void RegisterTestFailed() {
        when(userRepository.existsByUserName(usersEntity.getUserName())).thenReturn(true);

        assertTrue(userService.registerUser(usersEntity).isEmpty());
    }

    @Test
    public void changePasswordTestSuccessful() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(usersEntity.getPassword());
        userDTO.setNewPassword("newpassword");

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(usersEntity));
        when(passwordEncoder.matches(usersEntity.getPassword(), userDTO.getPassword())).thenReturn(true);

        assertTrue(userService.changePassword(authHeader, userDTO));
    }

    @Test
    public void changePasswordTestFailed() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("wrongpassword");
        userDTO.setNewPassword("newpassword");

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.empty());
        assertFalse(userService.changePassword(authHeader, userDTO));
    }
}
