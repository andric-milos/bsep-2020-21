package com.bezbednost.ftn.bsep.validation;

import com.bezbednost.ftn.bsep.model.RegistrationRequest;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class RegistrationRequestValidator {

    public static RegistrationRequest trim(RegistrationRequest request) {
        request.setFirstName(request.getFirstName().trim());
        request.setLastName(request.getLastName().trim());
        request.setEmail(request.getEmail().trim());
        request.setCountry(request.getCountry().trim());
        request.setCity(request.getCity().trim());
        request.setOrganization(request.getOrganization().trim());

        return request;
    }


    // returns true if RegistrationRequest is valid, false if it's not
    public static boolean validate(RegistrationRequest request) {
        if (request.getFirstName() == null)
            return false;
        else if (request.getFirstName().trim().equals(""))
            return false;

        if (request.getLastName() == null)
            return false;
        else if (request.getLastName().trim().equals(""))
            return false;

        if (request.getCountry() == null)
            return false;
        else if (request.getCountry().trim().equals(""))
            return false;

        if (request.getCity() == null)
            return false;
        else if (request.getCity().trim().equals(""))
            return false;

        if (request.getOrganization() == null)
            return false;
        else if (request.getOrganization().trim().equals(""))
            return false;

        // email validation
        Pattern pattern = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher matcher = pattern.matcher(request.getEmail().trim());

        if (request.getEmail() == null)
            return false;
        else if (request.getEmail().trim().equals(""))
            return false;
        else if (!matcher.matches())
            return false;

        // password validation
        if (request.getPassword() == null)
            return false;
        else if (request.getPassword().equals(""))
            return false;
        else if (!Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%&]).{10,}").matcher(request.getPassword()).matches())
            return false;

        return true;
    }
}
