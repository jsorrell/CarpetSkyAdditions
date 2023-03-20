package com.jsorrell.carpetskyadditions.settings;

import java.util.Optional;

public abstract class SettingFixer {
    public abstract String[] names();

    // Return empty if the field should be deleted
    public abstract Optional<FieldPair> fix(FieldPair currentVal);
}
