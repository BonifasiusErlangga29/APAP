package apap.tk.rumahsehat.controller;

import apap.tk.rumahsehat.setting.Setting;
import apap.tk.rumahsehat.model.UserModel;
import apap.tk.rumahsehat.service.UserService;
import apap.tk.rumahsehat.security.xml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {
    String login = "login";

    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(PageController.class);
    
    @GetMapping("/")
    public String home (Principal principal){
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("User {} mengakses halaman home", user.getNama());

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return login;
    }

    private WebClient webClient = WebClient.builder().build();



    @GetMapping("/validate-ticket")
    public String adminLoginSSO(
        @RequestParam(value = "ticket", required = false) String ticket,
        HttpServletRequest request, Model model
    )
    {
        var serviceResponse = this.webClient.get().uri(
                String.format(
                        Setting.SERVER_VALIDATE_TICKET,
                        ticket,
                        Setting.CLIENT_LOGIN
                )
        ).retrieve().bodyToMono(ServiceResponse.class).block();

        if (serviceResponse != null) {

            var attributes = serviceResponse.getAuthenticationSuccess().getAttributes();
            
            List<String> whitelist = new ArrayList<>();
            whitelist.add("alifiyah.nur");
            whitelist.add("bonifasius.erlangga");
            whitelist.add("danela.syafika");
            whitelist.add("karlina.rana");
            whitelist.add("muchlisah.lusiambali");
            whitelist.add("widya.ayu01");
    
            String username = serviceResponse.getAuthenticationSuccess().getUser();
    
            if (whitelist.contains(username)) {
                UserModel user = userService.getUserByUsername(username);
        
                if (user == null) {
                    user = new UserModel();
                    user.setEmail(username + "@ui.ac.id");
                    user.setNama(attributes.getNama());
                    user.setPassword("rumahsehat");
                    user.setUsername(username);
                    user.setRole("Admin");
                    userService.addUser(user);
                }
        
                var authentication = new UsernamePasswordAuthenticationToken(username, "rumahsehat");
                
                var securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
        
                var httpSession = request.getSession(true);
                httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                
                return "redirect:/";
            }
    
            model.addAttribute("erroradmin", "Anda tidak memiliki akses sebagai Admin");
            return login;
        }
        else {
            model.addAttribute("erroradmin", "Anda tidak memiliki akses sebagai Admin");
            return login;
        }
    }

    @GetMapping(value = "login-sso")
    public ModelAndView loginSSO() {
        return new ModelAndView("redirect:" + Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
    }

    @GetMapping(value = "logout-sso")
    public ModelAndView logoutSSO(Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());
        if (!user.getRole().equals("Admin")) {
            return new ModelAndView("redirect:/logout");
        }
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }
}
