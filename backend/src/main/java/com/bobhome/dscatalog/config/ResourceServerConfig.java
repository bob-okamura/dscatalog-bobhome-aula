package com.bobhome.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = { "/oauth/token" };
	private static final String[] OPERATOR_OR_ADMIN  = { "/products/**", "/categories/**" };
	private static final String[] ADMIN  = { "/users/**" };
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()//liberado acesso para todos que estiverem nessa rota
		.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()//libera somente o método GET nessa rota
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")//podem acessar quem for OPERATOR ou ADMIN(cadasrtrados na base de dados)
		.antMatchers(ADMIN).hasRole("ADMIN")//só pode acessar essa rota quem estiver como ADMIN
		.anyRequest().authenticated();//só acessa qualquer rota quem estiver logado
	}

	
	
}
