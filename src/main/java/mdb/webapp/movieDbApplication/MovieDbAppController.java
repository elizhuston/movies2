//package mdb.webapp.movieDbApplication;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//
//@Controller
//public class MovieDbAppController {
//	
//	   @RequestMapping(value="/")
//	    public String jspIndex() {
//	        return "index";
//	    }
//
//	    @RequestMapping(value="/login")
//	    public String login() {
//	        return "login";
//	    }
//
////	    @RequestMapping(value="/logout")
////	    public String logout(HttpServletRequest request, HttpServletResponse response) {
////	        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////	        if (auth != null){
////	            new SecurityContextLogoutHandler().logout(request, response, auth);
////	        }
////	        request.setAttribute("logout","logout");
////	        return "login";
////	    }
//
//}