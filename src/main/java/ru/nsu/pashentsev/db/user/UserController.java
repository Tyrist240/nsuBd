package ru.nsu.pashentsev.db.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO auth(@RequestBody UserDTO userDTO) {
        return userService.auth(userDTO);
    }

    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping(value = "/fetch",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<UserIdDTO> getListOfUsers() {
        return userService.fetch();
    }

    @PostMapping(value = "/change/{id}/{userRole}")
    public void changeRole(@PathVariable("id") Integer userId, @PathVariable("userRole") String userRole) {
        userService.changeRole(userId, UserRole.valueOf(userRole));
    }

}
