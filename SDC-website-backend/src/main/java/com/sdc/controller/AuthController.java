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

	// 🔥 LOGIN (CLEAN VERSION)
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody LoginModel request,
											 HttpServletResponse response) {

		try {
			System.out.println("===== LOGIN HIT =====");
			System.out.println("Email: " + request.getEmail());

			// ✅ Spring Security authentication (BEST WAY)
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),
							request.getPassword()
					)
			);

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			// ✅ Generate JWT
			String token = jwtService.generateToken(userDetails);
			System.out.println("Generated token: " + token);

			// ✅ Set HttpOnly Cookie
			ResponseCookie cookie = ResponseCookie.from("jwt", token)
					.httpOnly(true)
					.secure(false) // true in production (HTTPS)
					.path("/")
					.maxAge(3600)
					.sameSite("Lax")
					.build();

			response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

			// ✅ Response data
			Map<String, Object> responseData = new HashMap<>();

			if (userDetails instanceof AdminUserDetails adminUserDetails) {
				Admin admin = adminUserDetails.getAdmin();
				responseData.put("adminId", admin.getAdminId());
				responseData.put("name", admin.getName());
				responseData.put("email", admin.getEmail());
			}

			return ResponseEntity.ok(
					new ApiResponse(true, "Login successful", responseData)
			);

		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid credentials", null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(false, "Login failed", null));
		}
	}

	// 🔐 VERIFY TOKEN
	@GetMapping("/verify-token")
	public ResponseEntity<ApiResponse> verifyToken(
			@CookieValue(name = "jwt", required = false) String token) {

		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Token missing", null));
		}

		String username = jwtService.extractUsername(token);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

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