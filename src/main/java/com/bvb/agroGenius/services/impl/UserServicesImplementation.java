package com.bvb.agroGenius.services.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.RoleRepository;
import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Role;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.security.SecurityConstants;
import com.bvb.agroGenius.service.UserService;
import com.bvb.agroGenius.utils.GoogleOAuthUtil;
import com.bvb.agroGenius.utils.UserUtils;
import com.nimbusds.jose.shaded.gson.JsonObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class UserServicesImplementation implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServicesImplementation.class);

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String signIn(UserDto userDto) {
		String jwt = null;
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmailId(), userDto.getPassword()));
		if (null != authentication) {
			User user = repository.findByEmailId(userDto.getEmailId()).get();
			SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			jwt = Jwts.builder().setIssuer("AGRO_GENIUS").setSubject("JWT Token")
					.claim("userId", user.getId())
					.claim("username", authentication.getName()).claim("email", userDto.getEmailId())
					.claim("authorities", populateAuthorities(authentication.getAuthorities())).setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + 30000000)).signWith(key).compact();
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwt;
	}

	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}

	@Override
	public User getUserById(String emailId) throws AgroGeniusException {
		User user = null;
		try {
			user = repository.findByEmailId(emailId).get();

			if (user == null) {
				throw new AgroGeniusException("User not found");
			}
		} catch (Exception exception) {
			throw new AgroGeniusException(exception.getMessage());
		}
		return user;
	}

	@Override
	public Set<UserDto> getAllUser() throws AgroGeniusException {
		try {
			Set<UserDto> listOfDto = repository.findAll().stream().map(UserUtils::convertUserEntityToDto)
					.collect(Collectors.toSet());

			logger.info("List of user data returned successfully");
			return listOfDto;
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	@Override
	public String addNewUser(UserDto dto) throws AgroGeniusException {
		User user = UserUtils.convertUserDtoToEntity(dto);
		try {
			user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
			user.setCreatedAt(LocalDateTime.now());
			user.setLastModifiedAt(LocalDateTime.now());
			user.setIsEmailVerified(Boolean.FALSE);
			user.setIsPhoneNumberVerified(Boolean.FALSE);
			user.setEnabled(Boolean.TRUE);

			Role userRole = roleRepository.findByName("USER");
			user.getRoles().add(userRole);

			repository.save(user);

			logger.info(" User {} registered successfully ", dto.getFullName());
			return "User '" + dto.getFullName() + "' created successfully";

		} catch (DataIntegrityViolationException exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("User data has been registerd already.." + exception.getLocalizedMessage());
		}
	}

	@Override
	@Transactional
	public String updateUser(String emailId, UserDto dto) throws AgroGeniusException {

		try {
			User user = UserUtils.convertUserDtoToEntity(dto);
			User existingUser = repository.findByEmailId(emailId).get();

			if (existingUser == null) {
				throw new AgroGeniusException("User not Found");
			}

			// Update Name
			if (user.getFullName() != null && user.getFullName() != existingUser.getFullName()) {
				existingUser.setFullName(dto.getFullName());
			}
			// Update Email
			if (user.getEmailId() != null && user.getEmailId().length() > 0
					&& !Objects.equals(user.getEmailId(), existingUser.getEmailId())) {
				existingUser.setEmailId(user.getEmailId());
			}

			// Update PhoneNumber
			if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 0
					&& !Objects.equals(user.getPhoneNumber(), existingUser.getPhoneNumber())) {
				existingUser.setPhoneNumber(user.getPhoneNumber());
			}

			// Update Profile pic
			if (user.getProfilePic() != null && user.getProfilePic().length() > 0
					&& !Objects.equals(user.getProfilePic(), existingUser.getProfilePic())) {
				existingUser.setProfilePic(user.getProfilePic());
			}

			logger.info("{} updated successfully", existingUser.getFullName());

			return "User '" + existingUser.getFullName() + "' updated successfully";

		} catch (Exception exception) {
			throw new AgroGeniusException("Cannot update user data " + exception.getLocalizedMessage());
		}
	}

	@Override
	public String deleteUser(String emailId) throws AgroGeniusException {
		Optional<User> user = repository.findByEmailId(emailId);
		try {
			if (user.isPresent()) {
				repository.deleteById(user.get().getId());
				String name = user.get().getFullName();

				logger.error("{} data deleted", user.get().getFullName());
				return "User " + name + " Has deleted successfully.";
			} else {
				throw new AgroGeniusException("User doesn't exist.");
			}
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("User doesn't exist." + exception.getLocalizedMessage());
		}
	}

	@Override
	public String signInWithGoogle(String accessToken) throws AgroGeniusException {
		UserDto userDto = null;
		try {
			// userDto = getUserDtoFromGoogleCode(code);
			userDto = getUserDtoFromGoogleAccessToken(accessToken);
			Optional<User> userFromDB = repository.findByEmailId(userDto.getEmailId());
			if (userFromDB.isEmpty()) {
				userDto.setIsGoogleUser(true);
				this.addNewUser(userDto);

			}
		} catch (Exception e) {
			throw new AgroGeniusException("Error while signing in. " + e.getLocalizedMessage());
		}
		return signIn(userDto);

	}

	private UserDto getUserDtoFromGoogleAccessToken(String accessToken) {
		UserDto usertDto = new UserDto();
		JsonObject jsonObject = GoogleOAuthUtil.getProfileDetailsGoogle(accessToken);
		return populateUserDetailsFromGoogleResPonse(usertDto, jsonObject);

	}

	/*
	 * private UserDto getUserDtoFromGoogleCode(String code) { UserDto usertDto =
	 * new UserDto(); String response =
	 * GoogleOAuthUtil.getOauthAccessTokenGoogle(code); JsonObject json =
	 * JsonParser.parseString(response).getAsJsonObject(); String accessToken =
	 * json.get("access_token").toString();
	 * 
	 * JsonObject jsonObject = GoogleOAuthUtil.getProfileDetailsGoogle(accessToken);
	 * return populateUserDetailsFromGoogleResPonse(usertDto, jsonObject);
	 * 
	 * }
	 */

	private UserDto populateUserDetailsFromGoogleResPonse(UserDto userDto, JsonObject jsonObject) {
		userDto.setFullName(jsonObject.get("given_name").toString().replaceAll("\"", "")
				+ jsonObject.get("family_name").toString().replaceAll("\"", ""));
		Double phone = Math.random();
		userDto.setPhoneNumber(phone.toString().substring(2, 12) + "_RANDOM");
		userDto.setEmailId(jsonObject.get("email").toString().replaceAll("\"", ""));
		userDto.setPassword(userDto.getEmailId());
		userDto.setIsGoogleUser(Boolean.TRUE);
		userDto.setProfilePic(jsonObject.get("picture").toString().replaceAll("\"", ""));
		return userDto;
	}

	@Override
	public Set<UserDto> getDemo() {

		String id = "1 or 1 = 1";
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = ?1", User.class);

		Set<UserDto> user = query.setParameter(1, id).getResultList().stream().map(UserUtils::convertUserEntityToDto)
				.collect(Collectors.toSet());

		return user;
	}
}
