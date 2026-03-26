package com.example.booktracker.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Custom error controller to handle HTTP errors and display user-friendly error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handle errors and display appropriate error page.
     *
     * @param request HTTP request
     * @param model model to pass data to the view
     * @return error view name
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorCode", 403);
                model.addAttribute("errorMessage", "Access Denied");
                model.addAttribute("errorDescription", "You don't have permission to access this page. Please contact your administrator if you believe this is an error.");
                return "error/403";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorCode", 404);
                model.addAttribute("errorMessage", "Page Not Found");
                model.addAttribute("errorDescription", "The page you're looking for doesn't exist or has been moved.");
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorCode", 500);
                model.addAttribute("errorMessage", "Internal Server Error");
                model.addAttribute("errorDescription", "Something went wrong on our end. Please try again later.");
                return "error/500";
            }
        }
        
        // Generic error for other status codes
        model.addAttribute("errorCode", status != null ? status : "Error");
        model.addAttribute("errorMessage", "An Error Occurred");
        model.addAttribute("errorDescription", "Something unexpected happened. Please try again or contact support.");
        return "error/generic";
    }
}