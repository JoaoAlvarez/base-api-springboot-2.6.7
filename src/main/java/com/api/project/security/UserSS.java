package com.api.project.security;

import com.api.project.model.entity.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/** Classe de Usuario conforme o Contrato com o Spring Security*/
public class UserSS implements UserDetails{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userName;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.userName = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}

	public Integer getId() {
		return id;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	//SE A CONTA DO USUARIO ESTA EXPIRADA, PODE FAZER ALGUMA LOGICAR FUTURAMENTE PARA VALIDAR ISSO
	@Override
	public boolean isAccountNonExpired() {
		// TRUE = A CONTA NAO ESTA EXPIRADA
		return true;
	}

	//VALIDAR SE A CONTA ESTA BLOQUADA OU NAO
	@Override
	public boolean isAccountNonLocked() {
		// TRUE = NAO ESTA BLOQUEADA
		return true;
	}

	//VALIDAR SE AS CREDENCIAIS ESTA EXPIRADA OU NAO
	@Override
	public boolean isCredentialsNonExpired() {
		// TRUE = ELAS NAO ESTA EXPIRADAS
		return true;
	}

	// VALIDARA SE O USUARIO ESTA ATIVO
	@Override
	public boolean isEnabled() {
		// TRUE = USUARIO ATIVO
		return true;
	}
	
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}

}
