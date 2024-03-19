package ru.isu.productsaccounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.isu.productsaccounting.exception.InvalidAction;
import ru.isu.productsaccounting.exception.InvalidFormation;
import ru.isu.productsaccounting.exception.InvalidReference;
import ru.isu.productsaccounting.model.Deal;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.model.Reserve;
import ru.isu.productsaccounting.service.DealService;
import ru.isu.productsaccounting.service.ProductService;
import ru.isu.productsaccounting.service.ReserveService;
import ru.isu.productsaccounting.validator.DealValidator;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;
    private final ProductService productService;
    private final ReserveService reserveService;

    public DealController(DealService dealService, ProductService productService, ReserveService reserveService) {
        this.dealService = dealService;
        this.productService = productService;
        this.reserveService = reserveService;
    }


    @GetMapping("/listDeals")
    public String showDeals(Model model) {
        model.addAttribute("listDeals", dealService.getAllDeals());
        return "list_deals";
    }

    @GetMapping("/add")
    public String addDeal(Model model) {
        Deal deal = new Deal();
        model.addAttribute("deal", deal);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("listProducts", products);
        return "add_deal";
    }

    @PostMapping("/saveDeal")
    public String saveDeal(@ModelAttribute("deal") @Valid Deal deal, Errors errors, Model model) throws InvalidFormation {
        if (errors.hasErrors()) {
            List<Product> products = productService.getAllProducts();
            model.addAttribute("listProducts", products);
            return "add_deal";
        }
        if (isPartOfIndivisible(deal)) {
            throw new InvalidFormation("Нельзя проводить сделки с частями упаковок или штук!");
        }
        Reserve foundReserve = reserveService.findReserveByProductAndUnit(deal.getProduct(), deal.getUnit());
        if (foundReserve == null) {
            switch (deal.getOperation()) {
                case "Покупка":
                    Reserve reserve = new Reserve(deal.getUnit(), deal.getQuantity(), deal.getProduct());
                    dealService.saveDeal(deal);
                    reserveService.saveReserve(reserve);
                    break;
                case "Продажа":
                    throw new InvalidFormation("В запасе недостаточно продуктов!");
                default:
                    throw new InvalidFormation("Неизвестная операция!");
            }
        } else {
            switch (deal.getOperation()) {
                case "Покупка":
                    foundReserve.setQuantity(foundReserve.getQuantity() + deal.getQuantity());
                    dealService.saveDeal(deal);
                    reserveService.saveReserve(foundReserve);
                    break;
                case "Продажа":
                    if (foundReserve.getQuantity() > deal.getQuantity()) {
                        foundReserve.setQuantity(foundReserve.getQuantity() - deal.getQuantity());
                        dealService.saveDeal(deal);
                        reserveService.saveReserve(foundReserve);

                    } else if (foundReserve.getQuantity() == deal.getQuantity()) {
                        dealService.saveDeal(deal);
                        reserveService.deleteReserve(foundReserve);

                    } else {
                        throw new InvalidFormation("В запасе недостаточно продуктов!");
                    }
                    break;
                default:
                    throw new InvalidFormation("Неизвестная операция!");
            }
        }
        return "redirect:/deal/listDeals";
    }

    @GetMapping("/delete/{id}")
    public String deleteDeal(@PathVariable(value = "id") Deal deal) throws InvalidReference, InvalidFormation, InvalidAction {
        if (deal == null) {
            throw new InvalidReference("По указанному id сделка не найдена!");
        }
        Reserve foundReserve = reserveService.findReserveByProductAndUnit(deal.getProduct(), deal.getUnit());
        // на случай проблемы с таблицей запасов
        if (foundReserve == null) {
            dealService.deleteDeal(deal);
            return "redirect:/deal/listDeals";
        }

        switch (deal.getOperation()) {
            case "Покупка":
                if (foundReserve.getQuantity() > deal.getQuantity()) {
                    foundReserve.setQuantity(foundReserve.getQuantity() - deal.getQuantity());
                    reserveService.saveReserve(foundReserve);
                    dealService.deleteDeal(deal);
                } else if (foundReserve.getQuantity() == deal.getQuantity()) {
                    reserveService.deleteReserve(foundReserve);
                    dealService.deleteDeal(deal);
                } else {
                    throw new InvalidAction("Эту сделку невозможно удалить: в запасе будет" +
                            " отрицательное количество продуктов!");
                }
                break;
            case "Продажа":
                foundReserve.setQuantity(foundReserve.getQuantity() + deal.getQuantity());
                reserveService.saveReserve(foundReserve);
                dealService.deleteDeal(deal);
                break;
            default:
                throw new InvalidFormation("Неизвестная операция!");
        }
        return "redirect:/deal/listDeals";
    }

    @ModelAttribute("units")
    public List<String> getUnits() {
        List<String> units = new LinkedList(Arrays.asList("штука", "упаковка", "кг"));
        return units;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new DealValidator());
    }

    private Boolean isPartOfIndivisible(Deal deal) {
        return (((deal.getUnit().equals("упаковка") || (deal.getUnit().equals("штука")))) &&
                (deal.getQuantity() != Math.round(deal.getQuantity())));
    }
}
