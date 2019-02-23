package prog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MyController1 {

    private Map<String, String> map = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        map.put("user1", "password1");
        map.put("user2", "password2");
    }

    //@RequestMapping("/")
    public String index() {
        return "index_old";
    }

    @RequestMapping("/old")
    public String onIndex() {
        return "index_old";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String onLogin(Model model,
                          @RequestParam("login") String login,
                          @RequestParam String password,
                          HttpServletRequest request) {
        String pass = map.get(login);

        model.addAttribute("login", login);
        if (password.equals(pass)) {
            model.addAttribute("message", map);
        } else {
            model.addAttribute("message", "Failure");
        }

        return "result_old";
    }
}
