package com.sdc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sdc.config.JwtService;
import com.sdc.entity.Admin;
import com.sdc.models.LoginModel;
import com.sdc.repo.AdminRepository;
import com.sdc.services.CustomUserDetailsService;
import com.sdc.utils.AdminUserDetails;
import com.sdc.utils.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AdminRepository adminRepository;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(
			@RequestBody LoginModel request,
			HttpServletResponse response) {

		try {

			System.out.println("\n========== LOGIN HIT ==========");
			System.out.println("Email: " + request.getEmail());
			System.out.println("Password Received: " + request.getPassword());

			// STEP 1 - DB CHECK
			Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);

			if (admin == null) {
				System.out.println("❌ ADMIN NOT FOUND IN DB");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ApiResponse(false, "Admin not found", null));
			}

			System.out.println("✅ ADMIN FOUND");
			System.out.println("DB Email: " + admin.getEmail());
			System.out.println("DB Password Hash: " + admin.getPassword());

			// STEP 2 - PASSWORD CHECK
			boolean passwordMatch =
					passwordEncoder.matches(request.getPassword(), admin.getPassword());

			System.out.println("Password Match: " + passwordMatch);

			if (!passwordMatch) {
				System.out.println("❌ PASSWORD MISMATCH");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ApiResponse(false, "Invalid credentials", null));
			}

			System.out.println("✅ PASSWORD VERIFIED");

			// STEP 3 - SPRING SECURITY AUTH
			Authentication authentication =
					authenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(
									request.getEmail(),
									request.getPassword()
							)
					);

			System.out.println("✅ SPRING AUTH SUCCESS");

			UserDetails userDetails =
					(UserDetails) authentication.getPrincipal();

			// STEP 4 - JWT GENERATE
			String token = jwtService.generateToken(userDetails);

			System.out.println("✅ JWT GENERATED");
			System.out.println("Token: " + token);

			// STEP 5 - COOKIE
			ResponseCookie cookie = ResponseCookie.from("jwt", token)
					.httpOnly(true)
					.secure(false) // change to true after HTTPS setup
					.path("/")
					.maxAge(3600)
					.sameSite("Lax")
					.build();

			response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

			System.out.println("✅ COOKIE SET");

			Map<String, Object> responseData = new HashMap<>();

			if (userDetails instanceof AdminUserDetails adminUserDetails) {
				Admin loggedAdmin = adminUserDetails.getAdmin();

				responseData.put("adminId", loggedAdmin.getAdminId());
				responseData.put("name", loggedAdmin.getName());
				responseData.put("email", loggedAdmin.getEmail());
			}

			System.out.println("✅ LOGIN SUCCESSFUL");
			System.out.println("===============================\n");

			return ResponseEntity.ok(
					new ApiResponse(true, "Login successful", responseData)
			);

		} catch (AuthenticationException e) {

			System.out.println("❌ AUTHENTICATION FAILED");
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid credentials", null));

		} catch (Exception e) {

			System.out.println("❌ LOGIN EXCEPTION");
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(false, "Login failed", null));
		}
	}

	@GetMapping("/verify-token")
	public ResponseEntity<ApiResponse> verifyToken(
			@CookieValue(name = "jwt", required = false) String token) {

		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Token missing", null));
		}

		String username = jwtService.extractUsername(token);

		UserDetails userDetails =
				customUserDetailsService.loadUserByUsername(username);

		if (!jwtService.isTokenValid(token, userDetails)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ApiResponse(false, "Invalid token", null));
		}

		Map<String, Object> data = new HashMap<>();
		data.put("email", userDetails.getUsername());

		return ResponseEntity.ok(
				new ApiResponse(true, "Token valid", data)
		);
	}
}