package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.unused;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")

public class TestController {
    @GetMapping("/test")
    public String showForm(){
        return "test";
    }
}
