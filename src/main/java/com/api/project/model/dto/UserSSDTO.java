package com.api.project.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/** Classe de Usuario conforme o Contrato com o Spring Security*/
@Getter
@Setter
@NoArgsConstructor
public class UserSSDTO {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;

	public Integer getId() {
		return id;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getUsername() {
		return email;
	}


}
