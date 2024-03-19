package ru.isu.productsaccounting.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ReportDates {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDealDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDealDate;

    public ReportDates() {}

    public Date getStartDealDate() {
        return startDealDate;
    }

    public void setStartDealDate(Date startDealDate) {
        this.startDealDate = startDealDate;
    }

    public Date getEndDealDate() {
        return endDealDate;
    }

    public void setEndDealDate(Date endDealDate) {
        this.endDealDate = endDealDate;
    }
}
