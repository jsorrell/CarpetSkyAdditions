package com.jsorrell.skyblock.settings;

import com.jsorrell.skyblock.Build;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class SkyBlockDefaults {
  public static void writeDefaults(Path rootSavePath) throws IOException {
    writeSkyBlockDefaults(rootSavePath.resolve(Build.ID + ".conf"));
    writeCarpetDefaults(rootSavePath.resolve("carpet.conf"));
  }

  private static void writeSkyBlockDefaults(Path configPath) throws IOException {
    OutputStream out;
    try {
      out = Files.newOutputStream(configPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
    } catch (FileAlreadyExistsException e) {
      return;
    }

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
      for (Field field : SkyBlockSettings.class.getDeclaredFields()) {
        SkyBlockSetting settingAnnotation = field.getAnnotation(SkyBlockSetting.class);
        if (settingAnnotation == null) continue;
        writer.write(field.getName());
        writer.write(" ");
        writer.write(settingAnnotation.value());
        writer.newLine();
      }
    }
  }

  private static void writeCarpetDefaults(Path configPath) throws IOException {
    OutputStream out;
    try {
      out = Files.newOutputStream(configPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
    } catch (FileAlreadyExistsException e) {
      return;
    }

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
      writer.write(new FieldPair("renewableSponges", "true").asConfigLine());
      writer.newLine();
      writer.write(new FieldPair("piglinsSpawningInBastions", "true").asConfigLine());
      writer.newLine();
    }
  }
}
