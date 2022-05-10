package com.api.project.services;

import com.api.project.model.entity.Usuario;
import com.api.project.model.entity.enums.Perfil;
import com.api.project.repositories.UsuarioRepository;
import com.api.project.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**Classe de Implementacao do UserDetailsService, sequindo o contrato do SpringSecurity*/
@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;

//	@Autowired
//	private UsuarioInstituicaoRepository usuarioInstituicaoRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> cli = usuarioRepository.findByEmail(email);
		Set<Perfil> perfisUsuario = new HashSet<>();
		//CASO FOR O ADMIN LOGANDO
		if(cli.isPresent() && email.equalsIgnoreCase("admin@email.com")){
			perfisUsuario.add(Perfil.ADMIN);
			return new UserSS(cli.get().getId(), cli.get().getEmail(), cli.get().getSenha(), perfisUsuario);
		}

		if(cli == null){
			return null;
		}
//		Optional<List<UsuarioInstituicao>> userInst = usuarioInstituicaoRepository.findByIdUsuarioIdAndIdInstituicaoAtivaAndAtivo(cli.getId(), true,true);
//		if(!userInst.isPresent() || userInst.get().isEmpty()){
//			return null;
//		}
//		perfisUsuario = userInst.get().stream().map(el -> Perfil.toEnum(el.getPerfil())).collect(Collectors.toSet());
		return new UserSS(cli.get().getId(), cli.get().getEmail(), cli.get().getSenha(), perfisUsuario);
	}
}	