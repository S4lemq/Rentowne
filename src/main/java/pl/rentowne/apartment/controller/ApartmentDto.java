package pl.rentowne.apartment.controller;

public class ApartmentDto {

    private String country;
    private String street;

    public ApartmentDto(String country, String street) {
        this.country = country;
        this.street = street;
    }

    public ApartmentDto() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
