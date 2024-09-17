package com.bvb.agroGenius.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.GoogleUserDto;
import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.UserService;
import com.bvb.agroGenius.utils.UserUtils;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService services;

	@PostMapping("/signIn")
	public ResponseEntity<Object> signIn(HttpServletResponse response, @RequestBody UserDto userDto) {
		try {
			String statusMessage = "";
			String jwt = services.signIn(userDto);

			if (jwt == null) {
				statusMessage = "Error while Signing In.";
				return ResponseEntity.badRequest().body("Invalid Credentials");
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("access_token", jwt);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, access_token");
			statusMessage = "User Signed In Successfully.";
			return ResponseEntity.ok().headers(headers).body(statusMessage);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Internal server error " + e.getLocalizedMessage());
		}
	}
	/*
	 * @GetMapping("/signInWithGoogle") public AgroGeniusResponse
	 * signInWithGoogle(@RequestParam("code") String code, @RequestParam("scope")
	 * String scope,
	 * 
	 * @RequestParam("authuser") String authUser, @RequestParam("prompt") String
	 * prompt) {
	 * 
	 * String jwt = null; try { jwt = services.signInWithGoogle(code); } catch
	 * (AgroGeniusException e) { return new
	 * AgroGeniusResponse("Error while signing in. " + e.getLocalizedMessage(),
	 * HttpStatus.BAD_REQUEST); }
	 * 
	 * return new AgroGeniusResponse(jwt, HttpStatus.OK); }
	 */

	@PostMapping("/signInWithGoogle")
	public ResponseEntity<Object> signInWithGoogle(HttpServletResponse response, @RequestBody GoogleUserDto dto) {

		try {
			
			String statusMessage = "";
			String jwt = services.signInWithGoogle(dto.getAccessToken());
			
			if (jwt == null) {
				statusMessage = "Error while Signing In.";
				return ResponseEntity.badRequest().body("Invalid Credentials");
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("access_token", jwt);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, access_token");
			statusMessage = "User Signed In Successfully.";
			return ResponseEntity.ok().headers(headers).body(statusMessage);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Internal server error " + e.getLocalizedMessage());
		}
	}

	@PostMapping("/signUp")
	public AgroGeniusResponse addUser(@RequestBody UserDto dto) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = services.addNewUser(dto);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = "Failed to add User !!" + exception.getLocalizedMessage();
		} catch (Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@GetMapping("/{userEmailId}")
	public AgroGeniusResponse getByemailId(@PathVariable(name = "userEmailId") String userEmailId) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			UserDto userDto = UserUtils.convertUserEntityToDto(services.getUserById(userEmailId));
			if (userDto == null) {
				throw new AgroGeniusException("Users not found");
			}
			status = HttpStatus.OK;
			return new AgroGeniusResponse(userDto, status);
		} catch (AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@GetMapping("/getAll")
	public AgroGeniusResponse getAllUsers() {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			Set<UserDto> listOfDto = services.getAllUser();
			if (listOfDto.isEmpty()) {
				throw new AgroGeniusException("No users found");
			}
			status = HttpStatus.OK;
			return new AgroGeniusResponse(listOfDto, status);
		} catch (AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@PutMapping("/{emailId}")
	public AgroGeniusResponse updateUser(@PathVariable(name = "emailId") String emailId, @RequestBody UserDto userDto) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = services.updateUser(emailId, userDto);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = "Failed to Update User !!" + exception.getLocalizedMessage();
		} catch (Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@DeleteMapping("/id/{emailId}")
	public AgroGeniusResponse deleteUser(@PathVariable(name = "emailId") String emailId) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = services.deleteUser(emailId);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = "Failed to add User !!" + exception.getLocalizedMessage();
		} catch (Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);

	}

}
