package com.anxstra.gson.config;

import com.anxstra.entities.Setting;
import com.anxstra.exceptions.FieldIsMissingException;
import com.anxstra.exceptions.RequiredFileIsMissing;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import static com.anxstra.gson.config.PathConstants.SETTINGS_FILE_ROOT_NAME;
import static com.anxstra.gson.config.PathConstants.SETTING_FILE_NAME;

public class AppConfigurer {

    private static Setting setting;

    private AppConfigurer() {
    }

    public static Setting getSetting() {
        if (setting == null) {
            try {
                setting = loadSettings();
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                System.exit(0);
            }
        }
        return setting;
    }

    private static Setting loadSettings() throws IOException {
        InputStream inputStream = Thread.currentThread()
                                        .getContextClassLoader()
                                        .getResourceAsStream(SETTING_FILE_NAME);
        if (inputStream == null) {
            throw new RequiredFileIsMissing("settings.json file cannot be found");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            JsonObject documentRoot = GsonConfigurer.getGson()
                                                    .fromJson(reader, JsonObject.class);
            JsonObject settingObject = documentRoot.getAsJsonObject(SETTINGS_FILE_ROOT_NAME);
            verifySetting(settingObject);
            return GsonConfigurer.getGson()
                                 .fromJson(settingObject, Setting.class);

        }
    }

    private static void verifySetting(JsonObject setting) throws IOException {
        Class<Setting> clazz = Setting.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!setting.has(field.getName())) {
                throw new FieldIsMissingException("Json property " + field.getName() + " is missing");
            }
        }
    }
}
