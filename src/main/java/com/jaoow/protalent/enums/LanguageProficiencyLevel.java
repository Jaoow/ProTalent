package com.jaoow.protalent.enums;


import java.util.HashMap;

public enum LanguageProficiencyLevel {
    A1,
    A2,
    B1,
    B2,
    C1,
    C2;

    private static final HashMap<String, LanguageProficiencyLevel> MAP = new HashMap<>();

    public static LanguageProficiencyLevel getLanguageProficiencyLevel(String proficiencyLevel) {
        return MAP.get(proficiencyLevel);
    }

    static {
        for (LanguageProficiencyLevel proficiencyLevel : LanguageProficiencyLevel.values()) {
            MAP.put(proficiencyLevel.name(), proficiencyLevel);
        }
    }
}
