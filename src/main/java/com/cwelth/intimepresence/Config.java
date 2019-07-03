package com.cwelth.intimepresence;

import com.cwelth.intimepresence.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
    public static final String CATEGORY_INITIALSPAWN = "initialSpawn";
    public static final String CATEGORY_WORLDGEN = "worldGen";

    public static int dimensionsList[] = new int[]{0};
    public static boolean isWhitelist = true;
    public static int initialDimension = 0;
    public static boolean keepInventoryOnDeathInForeignDimensions = true;
    public static boolean hardcoreTimeLimit = false;
    public static int initialTime = 0;

    public static boolean genOreEnabled = true;
    public static boolean genOreDropEnabled = true;

    public static boolean genOreInOverworld = true;
    public static int dim0amount = 4;
    public static int dim0chance = 20;
    public static int dim0minY = 0;
    public static int dim0maxY = 140;

    public static boolean genOreInNether = true;
    public static int dim01amount = 3;
    public static int dim01chance = 5;
    public static int dim01minY = 0;
    public static int dim01maxY = 100;

    public static boolean genOreInTheEnd = true;
    public static int dim1amount = 3;
    public static int dim1chance = 5;
    public static int dim1minY = 0;
    public static int dim1maxY = 100;

    public static boolean genOreInForeignDimensions = true;
    public static int dimXamount = 2;
    public static int dimXchance = 5;
    public static int dimXminY = 0;
    public static int dimXmaxY = 200;

    public static int spawnX = 0;
    public static int spawnY = 0;
    public static int spawnZ = 0;
    public static int spawnDimension = 0;
    public static boolean spawnForced = false;

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            initSpawnConfig(cfg);
            initWorldGen(cfg);
        } catch (Exception e1) {
            ModMain.logger.log(Level.WARNING, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        List<String> order = new ArrayList(Arrays.asList( "dimensionsList", "isWhiteList", "initialDimension", "initialTime", "keepInventoryOnDeathInForeignDimensions", "hardcoreTimeLimit" ));
        dimensionsList = cfg.get(CATEGORY_GENERAL, "dimensionsList", dimensionsList, "List of dimension IDs").getIntList();
        isWhitelist = cfg.getBoolean("isWhiteList", CATEGORY_GENERAL, isWhitelist, "List of dimensions acts as a white list. So in these dimensions timer is stopped (false - to set to blacklist instead)");
        initialDimension = cfg.getInt("initialDimension", CATEGORY_GENERAL, initialDimension, -32767, 32768, "Dimension where to return player in if timer ran out");
        initialTime = cfg.getInt("initialTime", CATEGORY_GENERAL, initialTime, 0, 7128000, "Initial time that is given to new players");
        keepInventoryOnDeathInForeignDimensions = cfg.getBoolean("keepInventoryOnDeathInForeignDimensions", CATEGORY_GENERAL, keepInventoryOnDeathInForeignDimensions, "Should inventory be kept for players who died in \"not safe\" dimension?");
        hardcoreTimeLimit = cfg.getBoolean("hardcoreTimeLimit", CATEGORY_GENERAL, hardcoreTimeLimit, "Should player be killed when time ran out. Inventory will not be kept!");
        cfg.setCategoryPropertyOrder(CATEGORY_GENERAL, order);
    }

    private static void initSpawnConfig(Configuration cfg){
        cfg.addCustomCategoryComment(CATEGORY_INITIALSPAWN, "Initial Spawn configuration");
        List<String> order = new ArrayList(Arrays.asList( "spawnForced", "spawnDimension", "spawnX", "spawnY", "spawnZ" ));
        spawnForced = cfg.getBoolean("spawnForced", CATEGORY_INITIALSPAWN, spawnForced, "Set player spawn during first logon");
        spawnX = cfg.getInt("spawnX", CATEGORY_INITIALSPAWN, spawnX, -2000000, 2000000, "X coordinate");
        spawnY = cfg.getInt("spawnY", CATEGORY_INITIALSPAWN, spawnY, -2000000, 2000000, "Y coordinate");
        spawnZ = cfg.getInt("spawnZ", CATEGORY_INITIALSPAWN, spawnZ, -2000000, 2000000, "Z coordinate");
        spawnDimension = cfg.getInt("spawnDimension", CATEGORY_INITIALSPAWN, spawnDimension, -32767, 32768, "Dimension ID");
        cfg.setCategoryPropertyOrder(CATEGORY_INITIALSPAWN, order);
    }

    private static void initWorldGen(Configuration cfg)
    {
        cfg.addCustomCategoryComment(CATEGORY_WORLDGEN, "World gen configuration");
        List<String> order = new ArrayList(Arrays.asList( "genOreDropEnabled", "genOreEnabled", "genOreInOverworld", "dim0amount", "dim0chance", "dim0minY", "dim0maxY" ));

        genOreDropEnabled = cfg.getBoolean("genOreDropEnabled", CATEGORY_WORLDGEN, genOreDropEnabled, "Should Dimensional Ore drop from Endermen");

        genOreEnabled = cfg.getBoolean("genOreEnabled", CATEGORY_WORLDGEN, genOreEnabled, "Should Dimensional Ore generate in World at all");

        genOreInOverworld = cfg.getBoolean("genOreInOverworld", CATEGORY_WORLDGEN, genOreInOverworld, "Should Dimensional Ore generate in Overworld");
        dim0amount = cfg.getInt("dim0amount", CATEGORY_WORLDGEN, dim0amount, 0, 10, "Vein size");
        dim0chance = cfg.getInt("dim0chance", CATEGORY_WORLDGEN, dim0chance, 0, 100, "Chance to spawn");
        dim0minY = cfg.getInt("dim0minY", CATEGORY_WORLDGEN, dim0minY, 0, 255, "Minimum height");
        dim0maxY = cfg.getInt("dim0maxY", CATEGORY_WORLDGEN, dim0maxY, 0, 255, "Maximum height");

        order.addAll(Arrays.asList("genOreInNether", "dim01amount", "dim01chance", "dim01minY", "dim01maxY"));
        genOreInNether = cfg.getBoolean("genOreInNether", CATEGORY_WORLDGEN, genOreInNether, "Should Dimensional Ore generate in Nether");
        dim01amount = cfg.getInt("dim01amount", CATEGORY_WORLDGEN, dim01amount, 0, 10, "Vein size");
        dim01chance = cfg.getInt("dim01chance", CATEGORY_WORLDGEN, dim01chance, 0, 100, "Chance to spawn");
        dim01minY = cfg.getInt("dim01minY", CATEGORY_WORLDGEN, dim01minY, 0, 255, "Minimum height");
        dim01maxY = cfg.getInt("dim01maxY", CATEGORY_WORLDGEN, dim01maxY, 0, 255, "Maximum height");

        order.addAll(Arrays.asList("genOreInTheEnd", "dim1amount", "dim1chance", "dim1minY", "dim1maxY"));
        genOreInTheEnd = cfg.getBoolean("genOreInTheEnd", CATEGORY_WORLDGEN, genOreInTheEnd, "Should Dimensional Ore generate in The End");
        dim1amount = cfg.getInt("dim1amount", CATEGORY_WORLDGEN, dim1amount, 0, 10, "Vein size");
        dim1chance = cfg.getInt("dim1chance", CATEGORY_WORLDGEN, dim1chance, 0, 100, "Chance to spawn");
        dim1minY = cfg.getInt("dim1minY", CATEGORY_WORLDGEN, dim1minY, 0, 255, "Minimum height");
        dim1maxY = cfg.getInt("dim1maxY", CATEGORY_WORLDGEN, dim1maxY, 0, 255, "Maximum height");

        order.addAll(Arrays.asList("genOreInForeignDimensions", "dimXamount", "dimXchance", "dimXminY", "dimXmaxY"));
        genOreInForeignDimensions = cfg.getBoolean("genOreInForeignDimensions", CATEGORY_WORLDGEN, genOreInForeignDimensions, "Should Dimensional Ore try to generate in other dimensions");
        dimXamount = cfg.getInt("dimXamount", CATEGORY_WORLDGEN, dimXamount, 0, 10, "Vein size");
        dimXchance = cfg.getInt("dimXchance", CATEGORY_WORLDGEN, dimXchance, 0, 100, "Chance to spawn");
        dimXminY = cfg.getInt("dimXminY", CATEGORY_WORLDGEN, dimXminY, 0, 255, "Minimum height");
        dimXmaxY = cfg.getInt("dimXmaxY", CATEGORY_WORLDGEN, dimXmaxY, 0, 255, "Maximum height");

        cfg.setCategoryPropertyOrder(CATEGORY_WORLDGEN, order);
    }

}
