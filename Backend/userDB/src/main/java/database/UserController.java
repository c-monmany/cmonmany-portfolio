package database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.sun.org.apache.xerces.internal.util.URI;

import database.User;

@RestController 
public class UserController {
	
	@Autowired
	UserDB userRepository;

	/*
	 * to get User details by ID from the database
	 */
	@GetMapping("/user/{id}")  //("/user/{id}") is the endpoint 
	User getUserName(@PathVariable Integer id) {
		return userRepository.findById(id).get();	
	}
	
/*
 * 	to get all the Users details from the database
 */
	@RequestMapping("/user")
	List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
//		System.out.println("\n\n\n\n" + users + "\n\n\n\n");
		return users;
	}
	
/*
 * Registers user to the database and checks if there is an
 * existing user registered with the same parameters 
 */
	@PostMapping("/register")
	public Status registerUser(@RequestBody User newUser) {
		
		List<User> users = userRepository.findAll();

        System.out.println("New user: " + newUser.toString());
        for (User user : users) {
            if (user.username.equals(newUser.username)) {
                System.out.println("User Already exists!");
                return Status.USER_ALREADY_EXISTS;
            }
        }
//        newUser.setLoggedIn(true);
        System.out.println("Registered user: " + newUser.toString());
        
        userRepository.save(newUser);
        return Status.SUCCESS;
    }
	
	/*
	 * Log in call 
	 */
    @PostMapping("/user/login")
    public Status loginUser(@RequestBody User user) {
        List<User> users = userRepository.findAll();
        
        System.out.println("User list");
    	System.out.println("\n\n" + users + "\n\n\n");
    	
    	System.out.println(user.username);
    	System.out.println();

        for (User other : users) {
            if (other.equals(user)) {
                user.setLoggedIn(true);
                userRepository.save(user);
                return Status.SUCCESS;
            }
        }
        
        return Status.FAILURE;
    }

    /* 
     * Log out call 
     */
    @PostMapping("/user/logout")
    public Status logUserOut(@RequestBody User user) {
        List<User> users = userRepository.findAll();

        for (User other : users) {
            if (other.username.equals(user.username)) {
                user.setLoggedIn(false);
                userRepository.save(user);
                return Status.SUCCESS;
            }
        }

        return Status.FAILURE;
    }

	/*
	 * to delete user detail from the database
	 */
	@DeleteMapping("/user/{id}")
	String deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
		return "deleted user: " + id;
	}

//	Delete ALL users
    @DeleteMapping("/user/all")
    public Status deleteUsers() {
    	userRepository.deleteAll();
        return Status.SUCCESS;
    }

	
}