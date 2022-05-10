package com.api.project.config;

import java.util.Arrays;
import java.util.Collections;

import com.api.project.security.JWTAuthenticationFilter;
import com.api.project.security.JWTAuthorizationFilter;
import com.api.project.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private JWTUtil jwtUtil;

	//O Spring por padrao ja busca alguma classe que implemente essa Interface
	//Caso exista ele importa ela no lugar da Intaface
	@Autowired
	private UserDetailsService userDetailsService;

	private static final String[] PUBLIC_MATCHERS = { "h2-console/**" };

	// LISTA DE ENDPINTS QUE SO VAO PODER SER ACESSADOS SEM TOKEN VIA GET. Ex.:  "/cidades/**"
	private static final String[] PUBLIC_MATCHERS_GET = {"/"};

	// LISTA DE ENDPINTS QUE SO VAO PODER SER ACESSADOS SEM TOKEN VIA POST. Ex.:  "/cidades/**"
	private static final String[] PUBLIC_MATCHERS_POST = {"/auth/detail", "/auth/recuperar_senha/**"};

	/**Filtro de Acesso as URLS*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		http.cors().and().csrf().disable();

		// AUTORIZAR O ACESSO SEM TOKEN DAS URL DO 'PUBLIC_MATCHERS,
		// POREM PARA O RESTO EXIGIR ALTENTICACAO
		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
				.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
				.antMatchers(PUBLIC_MATCHERS).permitAll()
				.anyRequest().authenticated();

		//ADICIONANDO O FILTRO DE AUTENTIFICACAO DE TOKEN NAS REQUISICOES
		// O authenticationManager() jah eh um metodo do WebSecurityConfigurerAdapter
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));

		//ADICIONANDO O FILTRO DE -AUTORIZACAO- DE TOKEN NAS REQUISICOES
		// O authenticationManager() jah eh um metodo do WebSecurityConfigurerAdapter
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));


		// PARA O SISTEMA NAO ARMEZENAR A SESSAO DO USUARIO.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// super.configure(http);
	}

	/**Metodo que define o UserDetailsService que esta implementando a interface quem eh o algorito de codificacao da senha
	 * @throws Exception */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedMethods(Arrays.asList(
				HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.DELETE.name()
		));
		corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
		corsConfiguration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
		corsConfiguration.setAllowCredentials(true);
		source.registerCorsConfiguration("/**",corsConfiguration );
		return source;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
