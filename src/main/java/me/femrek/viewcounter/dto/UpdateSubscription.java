package me.femrek.viewcounter.dto;

import lombok.Data;

@Data
public class UpdateSubscription {
    private String name;

    public String validate() {
        if (name == null || name.isEmpty()) {
            return "Name cannot be empty";
        } else if (name.length() > 100) {
            return "Name cannot be longer than 100 characters";
        }

        return null;
    }
}
