package com.nyasha.drone.backend.category;

import com.nyasha.drone.backend.shared.CategoryType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drone/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String getCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "categories/index";
    }

    @GetMapping("/{id}")
    public String getCategory(@PathVariable int id, Model model) {
        Category category = categoryService.getCategory(id);
        model.addAttribute("category", category);
        return "categories/detail";
    }
    @GetMapping("/create")
    public String newCategory(Model model) {
        model.addAttribute("category", new CategoryRequest());
        model.addAttribute("types", CategoryType.values());
        return "categories/create";
    }
    @PostMapping("/create")
    public String saveCategory(@ModelAttribute("category") @Valid CategoryRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", request);
            model.addAttribute("types", CategoryType.values());
            return "categories/create";
        }
        categoryService.save(request);
        return "redirect:/drone/categories";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id, Model model) {
        Category category = categoryService.getCategory(id);
        model.addAttribute("category", category);
        return "/categories/delete";
    }
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.delete(id);
        return "redirect:/drone/categories";
    }
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable int id, Model model) {
        CategoryRequest categoryRequest = categoryService.getCategoryRequest(id);
        model.addAttribute("categoryRequest", categoryRequest);
        model.addAttribute("types", CategoryType.values());
        return "categories/edit";
    }
    @PostMapping("/edit/{id}")
    public String saveChanges(@ModelAttribute("category") @Valid CategoryRequest request, @PathVariable int id) {
        categoryService.update(request,id);
        return "redirect:/drone/categories";
    }
}
