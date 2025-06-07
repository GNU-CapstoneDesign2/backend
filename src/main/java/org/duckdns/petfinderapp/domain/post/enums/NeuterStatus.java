package org.duckdns.petfinderapp.domain.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NeuterStatus {
    NEUTERED("Y"),
    NOT_NEUTERED("N"),
    UNKNOWN("U");

    private final String apiValue;

    public static NeuterStatus fromApiValue(String value) {
        for (NeuterStatus status : NeuterStatus.values()) {
            if (status.getApiValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown NeuterStatus value: " + value);
    }
}
