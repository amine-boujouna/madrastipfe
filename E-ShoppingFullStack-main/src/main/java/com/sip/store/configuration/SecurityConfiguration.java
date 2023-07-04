package com.sip.store.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DataSource dataSource;
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)   // méthode responsable de l'authentification
            throws Exception {
        auth.
                jdbcAuthentication() // création d'une authentification JDBC
                .usersByUsernameQuery(usersQuery) // recherche du user par son email lors du login
                .authoritiesByUsernameQuery(rolesQuery)  // récuperation des roles du user qui se connecte
                .dataSource(dataSource) // notre datasource
                .passwordEncoder(bCryptPasswordEncoder); // en utilisant l'algorithme de cryptage bCrptPasswordEncoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // méthode responsable de l'autorisation


                http.csrf().
                disable().
                authorizeRequests()
                .antMatchers("/").permitAll().antMatchers("/role/Add").permitAll()
                .antMatchers("/basicauth").permitAll()
                        .antMatchers("/getuserbyusername/{username}").permitAll()
                        .antMatchers("/imgprofile/{id}").permitAll()
                        .antMatchers("/profile/{id}").permitAll()
                        .antMatchers("/file/{filename:.+}").permitAll()
                        .antMatchers("/upload").permitAll()
                        .antMatchers("/Files").permitAll()
                        .antMatchers("/Delete/{id}").permitAll()
                        .antMatchers("/update/{fileId}").permitAll()
                        .antMatchers("/getallfiledb").permitAll()
                        .antMatchers("/files/{id}").permitAll()
                        .antMatchers("/Getemplois").permitAll()
                        .antMatchers("/ajouterMeet").permitAll()
                        .antMatchers("/Getmeets").permitAll()
                        .antMatchers("/supprimmerMeet/{idmeet}").permitAll()
                        .antMatchers("/getmeetbyid/{id}").permitAll()
                        .antMatchers("/meet/{id}").permitAll()
                        .antMatchers("/role/list").permitAll()
                .antMatchers("/Registration").permitAll()
                .antMatchers("/files/{filename:.+}").permitAll()
                .antMatchers("/getfilebyid/{id}").permitAll()
                .antMatchers("/api/profile").permitAll()
                .antMatchers("/api/getAll").permitAll()
                .antMatchers("/api/profile/{id}").permitAll()
                .antMatchers("/api/imgprofile/{id}").permitAll()
                .antMatchers("/login").permitAll() // accès pour tous users
                .antMatchers("/registration").permitAll() // accès pour tous users
                .antMatchers("/role/**").hasAnyAuthority("SUPERADMIN")
                .antMatchers("/accounts/**").hasAnyAuthority("SUPERADMIN")
                .antMatchers("/providers/**").hasAnyAuthority("ADMIN","SUPERADMIN")  // Authority = role
                .antMatchers("/article/add").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/article/edit/**").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/article/delete/**").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/article/show/**").hasAnyAuthority("ADMIN","SUPERADMIN","AGENT")
                .antMatchers("/article/list").hasAnyAuthority("ADMIN","SUPERADMIN","AGENT")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    // laisser l'accès aux ressources
    @Override
    public void configure(WebSecurity web) throws Exception {   // méthode responsable de l'acces aux ressources
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/icons/**", "/plugins/**");
    }

}
