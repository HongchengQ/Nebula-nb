package emu.nebula.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import emu.nebula.GameConstants;
import emu.nebula.Nebula;
import emu.nebula.data.GameData;
import emu.nebula.data.ResourceType;
import emu.nebula.data.resources.CharacterDef;
import emu.nebula.data.resources.ItemDef;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Handbook {

    public static void generate() {
        // txt
        generateTextFormat();

        // JSON
        generateJsonFormat();
    }

    private static void generateTextFormat() {
        // Temp vars
        Map<String, String> languageKey = null;
        List<Integer> list = null;

        // Save to file
        String file = "./Nebula Handbook.txt";

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8), true)) {
            // Format date for header
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            var time = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).format(dtf);

            // Header
            writer.println("# Nebula " + GameConstants.getGameVersion() + " Handbook");
            writer.println("# Created " + time);

            // Dump characters
            writer.println(System.lineSeparator());
            writer.println("# Characters");
            list = GameData.getCharacterDataTable().keySet().intStream().sorted().boxed().toList();
            languageKey = loadLanguageKey(CharacterDef.class);
            for (int id : list) {
                CharacterDef data = GameData.getCharacterDataTable().get(id);
                writer.print(data.getId());
                writer.print(" : ");
                writer.print(languageKey.getOrDefault(data.getName(), data.getName()));
                writer.print(" (");
                writer.print(data.getElementType().toString());
                writer.println(")");
            }

            // Dump items
            writer.println(System.lineSeparator());
            writer.println("# Items");
            list = GameData.getItemDataTable().keySet().intStream().sorted().boxed().toList();
            languageKey = loadLanguageKey(ItemDef.class);
            for (int id : list) {
                ItemDef data = GameData.getItemDataTable().get(id);
                writer.print(data.getId());
                writer.print(" : ");
                writer.print(languageKey.getOrDefault(data.getTitle(), data.getTitle()));

                writer.print(" [");
                writer.print(data.getItemType());
                writer.println("]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateJsonFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<String> langList = new ArrayList<>();
        langList.add("en_US");
        langList.add("ja_JP");
        langList.add("ko_KR");
        langList.add("zh_CN");

        for (String lang : langList) {
            // Generate characters JSON
            generateCharactersJson(gson, lang);
            // Generate items JSON
            generateItemsJson(gson, lang);
        }
    }
    private static void generateCharactersJson(Gson gson, String lang) {
        try {
            // 创建目录
            Path outputPath = Paths.get("./JSON output/" + lang);
            Files.createDirectories(outputPath);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputPath.resolve("Characters.json").toFile()), StandardCharsets.UTF_8), true)) {
                Map<String, String> languageKey = loadLanguageKeyToJSON(CharacterDef.class, lang);
                List<Integer> list = GameData.getCharacterDataTable().keySet().intStream().sorted().boxed().toList();

                JsonArray charactersArray = new JsonArray();

                for (int id : list) {
                    CharacterDef data = GameData.getCharacterDataTable().get(id);
                    JsonObject characterObj = new JsonObject();

                    characterObj.addProperty("id", data.getId());
                    characterObj.addProperty("name", languageKey.getOrDefault(data.getName(), data.getName()));
                    characterObj.addProperty("element", data.getElementType().toString());

                    charactersArray.add(characterObj);
                }

                JsonObject root = new JsonObject();
                root.add("characters", charactersArray);

                writer.println(gson.toJson(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateItemsJson(Gson gson, String lang) {
        try {
            // 创建目录
            Path outputPath = Paths.get("./JSON output/" + lang);
            Files.createDirectories(outputPath);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputPath.resolve("Items.json").toFile()), StandardCharsets.UTF_8), true)) {
                Map<String, String> languageKey = loadLanguageKeyToJSON(ItemDef.class, lang);
                List<Integer> list = GameData.getItemDataTable().keySet().intStream().sorted().boxed().toList();

                JsonArray itemsArray = new JsonArray();

                for (int id : list) {
                    ItemDef data = GameData.getItemDataTable().get(id);
                    JsonObject itemObj = new JsonObject();

                    itemObj.addProperty("id", data.getId());
                    itemObj.addProperty("title", languageKey.getOrDefault(data.getTitle(), data.getTitle()));
                    itemObj.addProperty("type", String.valueOf(data.getItemType()));

                    itemsArray.add(itemObj);
                }

                JsonObject root = new JsonObject();
                root.add("items", itemsArray);

                writer.println(gson.toJson(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Map<String, String> loadLanguageKeyToJSON(Class<?> resourceClass, String lang) {
        // Get type
        ResourceType type = resourceClass.getAnnotation(ResourceType.class);
        if (type == null) {
            return Map.of();
        }

        // Load
        Map<String, String> map = null;

        try {
            map = JsonUtils.loadToMap(Nebula.getConfig().getResourceDir() + "/language/" + lang + "/" + type.name(), String.class, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map == null) {
            return Map.of();
        }

        return map;
    }

    private static Map<String, String> loadLanguageKey(Class<?> resourceClass) {
        // Get type
        ResourceType type = resourceClass.getAnnotation(ResourceType.class);
        if (type == null) {
            return Map.of();
        }

        // Load
        Map<String, String> map = null;

        try {
            map = JsonUtils.loadToMap(Nebula.getConfig().getResourceDir() + "/language/en_US/" + type.name(), String.class, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map == null) {
            return Map.of();
        }

        return map;
    }
}
