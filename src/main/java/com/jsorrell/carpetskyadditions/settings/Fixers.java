package com.jsorrell.carpetskyadditions.settings;

import com.google.common.collect.Iterables;
import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;

public final class Fixers {
    // TODO should we also fix global defaults?
    public static void fixSettings(Path rootSavePath) throws IOException {
        Map<String, ArrayList<SettingFixer>> fixerMap = getFixerMap();
        if (fixerMap.size() == 0) return;

        Path configPath = rootSavePath.resolve(SkyAdditionsExtension.MOD_ID + ".conf");
        // Read from File
        List<String> rules;
        try {
            rules = new ArrayList<>(Files.readAllLines(configPath, StandardCharsets.UTF_8));
        } catch (NoSuchFileException e) {
            return;
        }

        boolean rulesWereChanged = false;

        for (int i = 0; i < rules.size(); ++i) {
            // Parse rule
            String ruleLine = rules.get(i);
            ruleLine = ruleLine.replaceAll("[\\r\\n]", "");

            // This shouldn't really happen, but it's best to not modify a locked conf
            if ("locked".equalsIgnoreCase(ruleLine)) {
                return;
            }
            FieldPair fieldPair = new FieldPair(ruleLine);

            // Apply fixers in memory
            List<SettingFixer> fixers = fixerMap.get(fieldPair.getName());
            if (fixers == null) continue;

            FieldPair oldFieldPair = new FieldPair(fieldPair);

            boolean remove = false;
            for (SettingFixer fixer : fixers) {
                Optional<FieldPair> fieldPairOpt = fixer.fix(fieldPair);
                if (fieldPairOpt.isEmpty()) {
                    remove = true;
                    break;
                }
                fieldPair = fieldPairOpt.get();
            }

            if (remove) {
                rulesWereChanged = true;
                rules.set(i, null);
                SkyAdditionsSettings.LOG.info("Removing old rule " + oldFieldPair.getName());
            } else if (!fieldPair.equals(oldFieldPair)) {
                rulesWereChanged = true;
                rules.set(i, fieldPair.asConfigLine());
                SkyAdditionsSettings.LOG.info("Changing old rule \"" + oldFieldPair + "\" to \"" + fieldPair + "\"");
            }
        }

        // Write back to file
        if (rulesWereChanged) {
            Files.write(configPath, Iterables.filter(rules, Objects::nonNull), StandardCharsets.UTF_8);
        }
    }

    private static Map<String, ArrayList<SettingFixer>> getFixerMap() {
        Map<String, ArrayList<SettingFixer>> fixerMap = new HashMap<>();
        for (Field field : SkyAdditionsSettings.class.getDeclaredFields()) {
            SkyAdditionsSetting settingAnnotation = field.getAnnotation(SkyAdditionsSetting.class);
            if (settingAnnotation == null || settingAnnotation.fixer().length == 0) continue;
            for (int i = 0; i < settingAnnotation.fixer().length; ++i) {
                Class<? extends SettingFixer> fixerClass = settingAnnotation.fixer()[i];
                try {
                    Constructor<? extends SettingFixer> fixerConstructor = fixerClass.getDeclaredConstructor();
                    fixerConstructor.setAccessible(true);
                    SettingFixer fixer = fixerConstructor.newInstance();
                    Set<String> fieldNames = new HashSet<>(List.of(fixer.names()));

                    for (String name : fieldNames) {
                        ArrayList<SettingFixer> fixerList = fixerMap.getOrDefault(name, new ArrayList<>());
                        fixerList.add(fixerConstructor.newInstance());
                        fixerMap.put(name, fixerList);
                    }
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fixerMap;
    }
}
