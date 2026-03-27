package com.digitaltherapyassistant.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final int JWT_HEADER_OFFSET = 7;

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
                                    UserDetailsService userDetailsService
    ){
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException{

        logger.info("JWT Filter running for: {}", request.getRequestURI());

        authenticateRequest(request);

        filterChain.doFilter(request, response);
    }

    //Every Time Something Other than /auth is used
    public void authenticateRequest(HttpServletRequest request){
        try{
            String jwt = extractJwtFromRequest(request);

            logger.info("Extracted jwt");

            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
                String username = tokenProvider.getUsernameFromToken(jwt);
                logger.info("Got " + username + " from token");

                //Acutally Loads by Email Instead Because Multiple Users can have same Name using CustomUserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Authenticated user '{}' via JWT", username);
            }
            else{
                logger.info("No JWT Token");
            }
        }
        catch(Exception e){
            logger.error("Could not set user authentication in security context: {}", e.getMessage());
        }
        
    }

    private String extractJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(JWT_HEADER_OFFSET);
        }
        return null;
    }
}
