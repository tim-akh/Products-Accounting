package ru.isu.productsaccounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.isu.productsaccounting.service.ReserveService;

@Controller
@RequestMapping("/reserve")
public class ReserveController {

    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @GetMapping("/listReserves")
    public String showReserve(Model model) {
        model.addAttribute("listReserves", reserveService.getAllReserves());
        return "list_reserves";
    }
}
