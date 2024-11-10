package com.nyasha.drone.backend.order;

import com.nyasha.drone.backend.shared.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drone/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public String orders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders/index";
    }
    @GetMapping("details/{id}")
    public String details(Model model, @PathVariable int id) {
        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);
        return "orders/details";
    }
    @GetMapping("/new")
    public String newOrder(Model model) {
        model.addAttribute("order", new OrderRequest());
        model.addAttribute("categories", CategoryType.values());
        return "orders/new";
    }
    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("order") OrderRequest request, Model model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            //
        }
        orderService.save(request);
        return "redirect:/drone/orders";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderService.delete(id);
        return "redirect:/drone/orders";
    }
    @PostMapping("/update/{id}")
    public String updateOrder(Model model,@PathVariable int id) {
        Order order =  orderService.update(id);
        model.addAttribute("order", order);
        return "orders/details";
    }
}
