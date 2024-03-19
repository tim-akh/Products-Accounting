package ru.isu.productsaccounting.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        int errorCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String errorMsg;
        switch (errorCode) {
            case 400:
                errorMsg = "Error 400: Bad Request";
                break;
            case 404:
                errorMsg = "Error 404: Not Found";
                break;
            case 500:
                errorMsg = "Error 500: Internal Server Error";
                break;
            case 502:
                errorMsg = "Error 502: Bad Gateway";
                break;
            default:
                errorMsg = "Error " + errorCode;
                break;
        }
        model.addAttribute("errorMsg", errorMsg);
        return "error";
    }

    @GetMapping("/access_denied")
    public String handleDeniedAccess(Model model) {
        model.addAttribute("errorMsg", "У Вас нет прав для доступа к данной странице!");
        return "error";
    }
}
