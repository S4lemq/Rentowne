package pl.rentowne.data_table.enums;

/**
 * Enum określający typ filtrowania danego pola
 */
public enum DTFilterType {
    START_WITH,
    EQUALS,
    NOT_EQUAL,
    BOOLEAN,
    CONTAINS,
    DATE_FROM,
    DATE_TO,
    GOE,
    LOE,
    INDEPENDENT, // wyrażenie jest niezależne, nie przyjmuje parametru
    IN,
    NOT_IN,
    IN_ADDITIONAL_OR, // wykorzystywane dla wygenerowania dodatkowego OR w multiselect
    NOT_IN_ADDITIONAL_OR, // wykorzystywane dla wygenerowania dodatkowego OR w multiselect
    CUSTOM_QUERY,
    DATE_FROM_NOT_NULL,
    DATE_TO_NOT_NULL
}
