package ru.isu.productsaccounting.exception;

public class InvalidReference extends Exception {
    public InvalidReference(String errorMsg) {
        super(errorMsg);
    }
}
