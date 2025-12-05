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
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import emu.nebula.GameConstants;
import emu.nebula.Nebula;
import emu.nebula.data.GameData;
import emu.nebula.data.ResourceType;
import emu.nebula.data.resources.CharacterDef;
import emu.nebula.data.resources.DiscDef;
import emu.nebula.data.resources.ItemDef;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import emu.nebula.data.resources.PotentialDef;
import lombok.Getter;
import lombok.Setter;

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
            generateCharactersJson(gson, lang);
            generateItemsJson(gson, lang);
            generateDiscsJson(gson, lang);
            generateSubNoteSkillsJson(gson, lang);
            generatePotentialsJson(gson, lang);
        }
    }
    
    private static void generateSubNoteSkillsJson(Gson gson, String lang) {
        try {
            // 创建目录
            Path outputPath = Paths.get("./JSON output/" + lang);
            Files.createDirectories(outputPath);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputPath.resolve("SubNoteSkills.json").toFile()), StandardCharsets.UTF_8), true)) {
                Map<String, String> languageKey = loadLanguageKeyToJSON("SubNoteSkill.json", lang);
                
                // 解析子技能数据
                Map<Integer, SubNoteSkill> skillsMap = new HashMap<>();
                Pattern pattern = Pattern.compile("SubNoteSkill\\.(\\d+)\\.(\\d+)");
                
                for (Map.Entry<String, String> entry : languageKey.entrySet()) {
                    Matcher matcher = pattern.matcher(entry.getKey());
                    if (matcher.matches()) {
                        int skillId = Integer.parseInt(matcher.group(1));
                        int part = Integer.parseInt(matcher.group(2));
                        
                        SubNoteSkill skill = skillsMap.computeIfAbsent(skillId, k -> new SubNoteSkill(skillId));
                        
                        switch (part) {
                            case 1:
                                skill.setName(entry.getValue());
                                break;
                            case 2:
                                skill.setDescription(entry.getValue());
                                break;
                            case 3:
                                // 第三部分是带参数的描述，我们只需要前两部分
                                break;
                        }
                    }
                }
                
                // 构建JSON对象
                JsonArray skillsArray = new JsonArray();
                for (SubNoteSkill skill : skillsMap.values()) {
                    JsonObject skillObj = new JsonObject();
                    skillObj.addProperty("id", skill.getId());
                    skillObj.addProperty("name", skill.getName() + ": " + skill.getDescription());
                    skillObj.addProperty("element", "NONE"); // 根据数据示例，默认为NONE
                    skillsArray.add(skillObj);
                }
                
                JsonObject root = new JsonObject();
                root.add("subNoteSkills", skillsArray);
                
                writer.println(gson.toJson(root));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private static void generateDiscsJson(Gson gson, String lang) {
        try {
            // 创建目录
            Path outputPath = Paths.get("./JSON output/" + lang);
            Files.createDirectories(outputPath);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputPath.resolve("Discs.json").toFile()), StandardCharsets.UTF_8), true)) {
                Map<String, String> languageKey = loadLanguageKeyToJSON(ItemDef.class, lang);
                List<Integer> list = GameData.getDiscDataTable().keySet().intStream().sorted().boxed().toList();

                JsonArray charactersArray = new JsonArray();

                for (int id : list) {
                    DiscDef data = GameData.getDiscDataTable().get(id);
                    ItemDef itemData = GameData.getItemDataTable().get(id);
                    JsonObject characterObj = new JsonObject();

                    characterObj.addProperty("id", data.getId());
                    characterObj.addProperty("name", languageKey.getOrDefault(itemData.getTitle(), itemData.getTitle()));
                    characterObj.addProperty("element", data.getElementType().toString());

                    charactersArray.add(characterObj);
                }

                JsonObject root = new JsonObject();
                root.add("discs", charactersArray);

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

    private static void generatePotentialsJson(Gson gson, String lang) {
        try {
            // 创建目录
            Path outputPath = Paths.get("./JSON output/" + lang);
            Files.createDirectories(outputPath);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputPath.resolve("Potentials.json").toFile()), StandardCharsets.UTF_8), true)) {
                Map<String, String> languageKey = loadLanguageKeyToJSON(ItemDef.class, lang);
                List<Integer> list = GameData.getPotentialDataTable().keySet().intStream().sorted().boxed().toList();

                JsonArray itemsArray = new JsonArray();

                for (int id : list) {
                    PotentialDef data = GameData.getPotentialDataTable().get(id);
                    ItemDef itemData = GameData.getItemDataTable().get(id);
                    JsonObject itemObj = new JsonObject();

                    itemObj.addProperty("id", data.getId());
                    itemObj.addProperty("title", languageKey.getOrDefault(itemData.getTitle(), itemData.getTitle()));
                    itemObj.addProperty("type", "NONE");

                    itemsArray.add(itemObj);
                }

                JsonObject root = new JsonObject();
                root.add("potentials", itemsArray);

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
    
    private static Map<String, String> loadLanguageKeyToJSON(String fileName, String lang) {
        // Load
        Map<String, String> map = null;

        try {
            map = JsonUtils.loadToMap(Nebula.getConfig().getResourceDir() + "/language/" + lang + "/" + fileName, String.class, String.class);
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
    
    /**
     * 子技能数据类
     */
    @Getter
    private static class SubNoteSkill {
        private final int id;
        @Setter
        private String name;
        @Setter
        private String description;
        
        public SubNoteSkill(int id) {
            this.id = id;
        }

    }
}