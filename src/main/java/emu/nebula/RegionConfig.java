package emu.nebula;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegionConfig {
    private String name;
    private int dataVersion;
    private String serverMetaKey;
    private String serverGarbleKey;
    
    private static Object2ObjectMap<String, RegionConfig> REGIONS = new Object2ObjectOpenHashMap<>();
    
    public RegionConfig(String name) {
        this.name = name;
        this.serverMetaKey = "";
        this.serverGarbleKey = "";
    }
    
    public RegionConfig setDataVersion(int i) {
        this.dataVersion = i;
        return this;
    }
    
    public RegionConfig setServerMetaKey(String key) {
        this.serverMetaKey = key;
        return this;
    }
    
    public RegionConfig setServerGarbleKey(String key) {
        this.serverGarbleKey = key;
        return this;
    }
    
    public static RegionConfig getRegion(String name) {
        String regionName = name.toLowerCase();
        return REGIONS.computeIfAbsent(regionName, r -> new RegionConfig(regionName));
    }
}
