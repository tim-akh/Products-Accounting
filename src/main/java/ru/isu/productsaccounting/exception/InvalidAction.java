package ru.isu.productsaccounting.exception;

public class InvalidAction extends Exception {
    public InvalidAction(String errorMsg) {
        super(errorMsg);
    }
}
