package com.icare.controller.webcontroller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.Year


@Controller
class WebController {

    @GetMapping("/")
    fun home(model: Model): String {
        // Add dynamic data to the model
        model.addAttribute("message", "Hello, Spring Boot Mohamed")
        model.addAttribute("currentYear", Year.now().value)
        return "index" // Refers to index.html
    }

    @PostMapping("/submit")
    fun submitForm(@RequestParam name: String?, model: Model,redirectAttributes: RedirectAttributes): String {
        // Add the submitted data to the model
        model.addAttribute("name", name)
        redirectAttributes.addFlashAttribute("name", name);

        // Redirect to the result page
        return "redirect:/result"
    }

    @GetMapping("/result")
    fun resultPage(model: Model?): String {
        // The name attribute is already added in the submitForm method
        return "result" // Refers to result.html
    }
}