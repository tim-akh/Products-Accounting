package ru.isu.productsaccounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.isu.productsaccounting.exception.InvalidFormation;
import ru.isu.productsaccounting.model.Deal;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.model.ReportDates;
import ru.isu.productsaccounting.service.DealService;
import ru.isu.productsaccounting.service.ProductService;
import ru.isu.productsaccounting.validator.ReportDatesValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final DealService dealService;
    private final ProductService productService;

    public ReportController(DealService dealService, ProductService productService) {
        this.dealService = dealService;
        this.productService = productService;
    }

    @GetMapping("/createReport")
    public String showForm(Model model) {
        model.addAttribute("reportDates", new ReportDates());
        return "report_form";
    }

    @PostMapping("/showReport")
    public String createReport(@ModelAttribute("reportDates") @Valid ReportDates reportDates,
                               Errors errors, Model model) throws InvalidFormation {
        if (errors.hasErrors()) {
            return "report_form";
        }
        if (reportDates.getStartDealDate().compareTo(reportDates.getEndDealDate()) > 0) {
            throw new InvalidFormation("Даты необходимо вводить в порядке возрастания!");
        }
        model.addAttribute("reportDates", reportDates);
        List<Deal> deals = dealService.findAllByDealDateRange(reportDates.getStartDealDate(),
                reportDates.getEndDealDate());
        if (deals.isEmpty()) {
            throw new InvalidFormation("В указанный период сделки не производились!");
        }
        model.addAttribute("listDeals", deals);
        float resultFlow = 0f;
        Map<String, Float> resultFlows = new HashMap<>();
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            resultFlows.put(product.getName(), 0f);
        }
        String key;
        float total;
        for (Deal deal : deals) {
            key = deal.getProduct().getName();
            total = deal.getPriceForUnit() * deal.getQuantity();
            switch (deal.getOperation()) {
                case "Покупка":
                    resultFlow -= total;
                    resultFlows.put(key, resultFlows.get(key) - total);
                    break;
                case "Продажа":
                    resultFlow += total;
                    resultFlows.put(key, resultFlows.get(key) + total);
                    break;
                default:
                    throw new InvalidFormation("Неизвестная операция!");
            }
        }
        model.addAttribute("resultFlow", resultFlow);
        model.addAttribute("resultFlows", resultFlows);
        return "report";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new ReportDatesValidator());
    }
}
