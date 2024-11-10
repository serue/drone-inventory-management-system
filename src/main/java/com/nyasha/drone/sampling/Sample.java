package com.nyasha.drone.sampling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/drone/sample")
public class Sample {
   @GetMapping
    public String sample() {
       return "sample";
   }
}
