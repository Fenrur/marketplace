package fr.marketplace.bi;

import org.apache.commons.lang3.Validate;

public record PostalAddress(String fistName, String lastName, String streetNumber, String street,
                            String stateOrProvince, String postalCode, String country, String city) {

    public PostalAddress {
        Validate.notBlank(fistName);
        Validate.notBlank(lastName);
        Validate.notBlank(streetNumber);
        Validate.notBlank(street);
        Validate.notBlank(stateOrProvince);
        Validate.notBlank(postalCode);
        Validate.notBlank(country);
        Validate.notBlank(city);
    }
}
