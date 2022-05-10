package com.api.project.resources;

import com.api.project.model.dto.EmailDTO;
import com.api.project.model.dto.UserSSDTO;
import com.api.project.security.JWTUtil;
import com.api.project.security.UserSS;
import com.api.project.services.AuthenticationService;
import com.api.project.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AthorizationResource {

	@Autowired
	private JWTUtil jwtUtil;
		
	@Autowired
	private AuthorizationService authorizationService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResponseEntity<UserSSDTO> userDetail() {
		UserSS user = AuthenticationService.authenticated();
		UserSSDTO dto = new UserSSDTO();
		dto.setEmail(user.getUsername());
		dto.setAuthorities(user.getAuthorities());
		dto.setId(user.getId());
		return ResponseEntity.ok().body(dto);
	}

	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = AuthenticationService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/recuperar_senha", method = RequestMethod.POST)
	public ResponseEntity<Void> recuperarSenha(@Valid @RequestBody EmailDTO objDto) {
		authorizationService.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build();
	}
}
