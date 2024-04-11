package com.javatechie.filter;

import com.javatechie.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
        	 
        	
            if (validator.isSecured.test(exchange.getRequest())) {
            	 HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst("token");
            	 
            	

                 if (tokenCookie == null ||  tokenCookie.getValue().isBlank()) {
                     throw new RuntimeException("Missing or invalid authorization token in cookies");
                 }

                 String authToken = tokenCookie.getValue();
                 
                 String userRole = jwtUtil.extractUserRole(authToken);
                 
                 
                //  if (exchange.getRequest().getURI().getPath().startsWith("/admin") &&
                //          !"ROLE_ADMIN".equals(userRole)) {
                //      throw new RuntimeException("User does not have sufficient privileges");
                //  }

                try {
                  
                    jwtUtil.validateToken(authToken);

                } catch (ExpiredJwtException e) {
                    // Handle JWT expiration error
                    System.out.println("JWT token has expired");
                    // Add custom logic to handle JWT expiration, such as returning a specific HTTP response
                    // For example:
                    System.out.println("JWT token has expired");
                    // Return 401 Unauthorized status
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                    // or
                    // throw new RuntimeException("JWT token has expired");
                    // or
                    // Redirect the user to a login page, etc.
                
                
                }
                
                catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }
    


    public static class Config {

    }
}
