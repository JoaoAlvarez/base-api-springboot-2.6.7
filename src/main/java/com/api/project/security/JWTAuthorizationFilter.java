package com.api.project.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	/**Metodo que intercepta a requisicao e ver se o token ta valido,
	 * o metodo doFilterInternal eh executado antes de deixar a requisicao proceder*/
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String headerAuthorization = request.getHeader("Authorization");
		if (headerAuthorization != null && headerAuthorization.startsWith("Bearer ")) {
			// subtring(7) retira os 7 primeiros digitos do string, ou seja, a palavra "Bearer "
			UsernamePasswordAuthenticationToken auth = getAuthentication(headerAuthorization.substring(7));
			
			// se Auth tiver != null quer dizer que esta tudo ok com o token
			if(auth != null) {
				//Liberando a autorizao do usuario ao endpoint
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		//procede com a requisicao normalmente
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if(jwtUtil.tokenValido(token)) {
			String userName = jwtUtil.getUserName(token);
			
			//Buscando na base de dados o Usuario
			UserDetails user = userDetailsService.loadUserByUsername(userName);
			
			//credentials == null porque estamos controlando as requisicoes por perfis, que eh o terceiro argumento
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
	

}
