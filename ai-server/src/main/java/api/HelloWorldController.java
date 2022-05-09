package api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/helloworld")
@RestController
public class HelloWorldController {

    @GetMapping
    public String helloWorld() {
        return "Hello World!";
    }
}
