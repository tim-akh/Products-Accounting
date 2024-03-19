package ru.isu.productsaccounting.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.isu.productsaccounting.model.ReportDates;

public class ReportDatesValidator implements Validator {


    @Override
    public boolean supports(Class<?> type) {
        return ReportDates.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ReportDates reportDates = (ReportDates) o;
        if (reportDates.getStartDealDate() == null) {
            errors.rejectValue("startDealDate", "reportDates.startDealDate",
                    "Дату начала периода необходимо заполнить!");
        }
        if (reportDates.getEndDealDate() == null) {
            errors.rejectValue("endDealDate", "reportDates.endDealDate",
                    "Дату окончания периода необходимо заполнить!");
        }

    }
}
