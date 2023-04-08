package com.jsorrell.carpetskyadditions.settings;

import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;

public final class SkyBlockDefaults {
    public static void writeDefaults(Path rootSavePath) throws IOException {
        Path defaultsPath = FabricLoader.getInstance().getConfigDir().resolve("carpet");
        writeSkyBlockDefaults(
                rootSavePath.resolve(SkyAdditionsExtension.MOD_ID + ".conf"),
                defaultsPath.resolve("default_" + SkyAdditionsExtension.MOD_ID + ".conf"));
        writeCarpetDefaults(rootSavePath.resolve("carpet.conf"), defaultsPath.resolve("default_carpet.conf"));
    }

    private static void writeSkyBlockDefaults(Path configPath, Path defaultConfigPath) throws IOException {
        List<FieldPair> fieldPairs = new ArrayList<>();

        for (Field field : SkyAdditionsSettings.class.getDeclaredFields()) {
            SkyAdditionsSetting settingAnnotation = field.getAnnotation(SkyAdditionsSetting.class);
            if (settingAnnotation == null) continue;
            fieldPairs.add(new FieldPair(field.getName(), settingAnnotation.value()));
        }

        writeConfigFile(configPath, defaultConfigPath, fieldPairs);
    }

    private static void writeCarpetDefaults(Path configPath, Path defaultConfigPath) throws IOException {
        writeConfigFile(
                configPath,
                defaultConfigPath,
                List.of(new FieldPair("renewableSponges", "true"), new FieldPair("piglinsSpawningInBastions", "true")));
    }

    private static void writeConfigFile(Path configPath, Path defaultConfigPath, List<FieldPair> fieldPairs)
            throws IOException {
        // Open output config file
        OutputStream out;
        try {
            out = Files.newOutputStream(configPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        } catch (FileAlreadyExistsException e) {
            return;
        }

        // Read in all defaults from installation defaults file
        Map<String, String> globalDefaultSettings = new HashMap<>();
        try {
            InputStream in = Files.newInputStream(defaultConfigPath, StandardOpenOption.READ);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] defaultSettingFields = line.split("\\s+", 2);
                    if (1 < defaultSettingFields.length) {
                        // This copies commented lines over, which is fine
                        globalDefaultSettings.put(defaultSettingFields[0], defaultSettingFields[1]);
                    }
                }
            }
        } catch (IOException ignored) {
        }

        // Write out settings to file
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            // Copy global defaults to local defaults
            for (Map.Entry<String, String> defaultSetting : globalDefaultSettings.entrySet()) {
                writer.write(new FieldPair(defaultSetting.getKey(), defaultSetting.getValue()).asConfigLine());
                writer.newLine();
            }

            // Write SkyBlock defaults only if they aren't overwritten by the global defaults file
            for (FieldPair fieldPair : fieldPairs) {
                if (!globalDefaultSettings.containsKey(fieldPair.name)) {
                    writer.write(fieldPair.asConfigLine());
                    writer.newLine();
                }
            }
        }
    }
}
