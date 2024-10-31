package com.Fixura.FixuraBackend.Security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import com.Fixura.FixuraBackend.Util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
  
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        String correo = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
          correo = jwtUtil.extractUserEmail(authorizationHeader);
        }

        if(correo != null && SecurityContextHolder.getContext().getAuthentication() == null){
          UserDetails userDetails = userDetailsService.loadUserByUsername(correo);
          List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(correo, null, authorities);

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    
  }
}
