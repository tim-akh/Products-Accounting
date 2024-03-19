package ru.isu.productsaccounting.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.isu.productsaccounting.model.Deal;

public class DealValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return Deal.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Deal deal = (Deal) o;
        if (deal.getDealDate() == null) {
            errors.rejectValue("dealDate", "deal.dealDate",
                    "Дату сделки необходимо заполнить!");
        }



    }
}
