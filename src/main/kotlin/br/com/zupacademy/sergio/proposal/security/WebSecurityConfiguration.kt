package br.com.zupacademy.sergio.proposal.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
  override fun configure(httpSecurity: HttpSecurity) {
    httpSecurity {

      csrf { disable() }

      authorizeRequests {
        authorize(HttpMethod.GET, "/**", hasAuthority("SCOPE_proposal:read"))
        authorize(HttpMethod.POST, "/**", hasAuthority("SCOPE_proposal:write"))
        authorize(anyRequest, authenticated)
      }

      sessionManagement {
        sessionCreationPolicy = SessionCreationPolicy.STATELESS
      }

      oauth2ResourceServer { jwt { } }

    }
  }
}
