package advsolar.common;

import advsolar.common.blocks.BlockAdvSolarPanel;
import advsolar.common.blocks.BlockMolecularTransformer;
import advsolar.common.items.ItemAdvSolarPanel;
import advsolar.common.items.ItemAdvanced;
import advsolar.common.items.ItemAdvancedSolarHelmet;
import advsolar.common.items.ItemMolecularTransformer;
import advsolar.common.tiles.*;
import advsolar.utils.MTRecipeConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;

@Mod(modid="AdvancedSolarPanel", name="Advanced Solar Panels", dependencies="required-after:IC2; after:gregtech_addon; after:AppliedEnergistics;", version="1.7.10-3.5.1")
public class AdvancedSolarPanel
{
  @SidedProxy(clientSide="advsolar.client.ASPClientProxy", serverSide="advsolar.common.ASPServerProxy")
  public static ASPServerProxy proxy;
  public static Block blockAdvSolarPanel;
  public static Block blockMolecularTransformer;
  public static Item itemAdvanced;
  public static Item advancedSolarHelmet;
  public static Item hybridSolarHelmet;
  public static Item ultimateSolarHelmet;
  public static ItemStack itemSunnarium;
  public static ItemStack itemSunnariumPart;
  public static ItemStack itemSunnariumAlloy;
  public static ItemStack ingotIridium;
  public static ItemStack itemIrradiantUranium;
  public static ItemStack itemEnrichedSunnarium;
  public static ItemStack itemEnrichedSunnariumAlloy;
  public static ItemStack itemIrradiantGlassPane;
  public static ItemStack itemIridiumIronPlate;
  public static ItemStack itemReinforcedIridiumIronPlate;
  public static ItemStack itemIrradiantReinforcedPlate;
  public static ItemStack itemQuantumCore;
  public static ItemStack itemUranIngot;
  public static ItemStack itemUHSP;
  public static ItemStack itemMTCore;
  public static ItemStack itemMolecularTransformer;
  public static Configuration config;
  public static String configFileName;
  public static int advGenDay;
  public static int advGenNight;
  public static int advStorage;
  public static int advOutput;
  public static int hGenDay;
  public static int hGenNight;
  public static int hStorage;
  public static int hOutput;
  public static int uhGenDay;
  public static int uhGenNight;
  public static int uhStorage;
  public static int uhOutput;
  public static int qpGenDay;
  public static int qpGenNight;
  public static int qpStorage;
  public static int qpOutput;
  public static int qgbaseProduction;
  public static int qgbaseMaxPacketSize;
  public static int blockMolecularTransformerRenderID;
  public static CreativeTabs ic2Tab;
  private static boolean disableAdvancedSolarHelmetRecipe;
  private static boolean disableHybridSolarHelmetRecipe;
  private static boolean disableUltimateSolarHelmetRecipe;
  private static boolean disableAdvancedSolarPanelRecipe;
  private static boolean disableHybridSolarPanelRecipe;
  private static boolean disableUltimateSolarPanelRecipe;
  private static boolean disableQuantumSolarPanelRecipe;
  private static boolean disableMolecularTransformerRecipe;
  private static boolean disableDoubleSlabRecipe;
  private static boolean enableSimpleAdvancedSolarPanelRecipes;
  private static boolean enableHardRecipes;
  public static final String CATEGORY_RECIPES = "recipes settings";
  public static final String CATEGORY_QGENERATOR = "quantum generator";
  public static final Side side = FMLCommonHandler.instance().getEffectiveSide();
  
  public static ItemStack setItemsSize(ItemStack itemStack, int newSize)
  {
    ItemStack newStack = itemStack.copy();
    newStack.stackSize = newSize;
    return newStack;
  }
  
  public static void getIC2Tab()
  {
    for (int i = 0; i < CreativeTabs.creativeTabArray.length; i++) {
      if (CreativeTabs.creativeTabArray[i].getTabLabel() == "IC2") {
        ic2Tab = CreativeTabs.creativeTabArray[i];
      }
    }
  }
  
  public static boolean isSimulating()
  {
    return !FMLCommonHandler.instance().getEffectiveSide().isClient();
  }
  
  public static void addLog(String logLine)
  {
    System.out.println("[AdvancedSolarPanel] " + logLine);
  }
  
  @Mod.Instance("AdvancedSolarPanel")
  public static AdvancedSolarPanel instance = new AdvancedSolarPanel();
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    try
    {
      config.load();
      configFileName = event.getSuggestedConfigurationFile().getAbsolutePath();
      advGenDay = config.get("general", "AdvancedSPGenDay", 8).getInt(8);
      advGenNight = config.get("general", "AdvancedSPGenNight", 1).getInt(1);
      advStorage = config.get("general", "AdvancedSPStorage", 32000).getInt(32000);
      advOutput = config.get("general", "AdvancedSPOutput", 32).getInt(32);
      hGenDay = config.get("general", "HybrydSPGenDay", 64).getInt(64);
      hGenNight = config.get("general", "HybrydSPGenNight", 8).getInt(8);
      hStorage = config.get("general", "HybrydSPStorage", 100000).getInt(100000);
      hOutput = config.get("general", "HybrydSPOutput", 128).getInt(128);
      uhGenDay = config.get("general", "UltimateHSPGenDay", 512).getInt(512);
      uhGenNight = config.get("general", "UltimateHSPGenNight", 64).getInt(64);
      uhStorage = config.get("general", "UltimateHSPStorage", 1000000).getInt(1000000);
      uhOutput = config.get("general", "UltimateHSPOutput", 512).getInt(512);
      qpGenDay = config.get("general", "QuantumSPGenDay", 4096).getInt(4096);
      qpGenNight = config.get("general", "QuantumSPGenNight", 2048).getInt(2048);
      qpStorage = config.get("general", "QuantumSPStorage", 10000000).getInt(10000000);
      qpOutput = config.get("general", "QuantumSPOutput", 8192).getInt(8192);
      
      qgbaseProduction = config.get("quantum generator", "quantumGeneratorDefaultProduction", 512).getInt(512);
      qgbaseMaxPacketSize = config.get("quantum generator", "quantumGeneratorDefaultPacketSize", 512).getInt(512);
      disableAdvancedSolarHelmetRecipe = config.get("recipes settings", "Disable Advanced Solar Helmet recipe", false).getBoolean(false);
      disableHybridSolarHelmetRecipe = config.get("recipes settings", "Disable Hybrid Solar Helmet recipe", false).getBoolean(false);
      disableUltimateSolarHelmetRecipe = config.get("recipes settings", "Disable Ultimate Solar Helmet recipe", false).getBoolean(false);
      disableAdvancedSolarPanelRecipe = config.get("recipes settings", "Disable AdvancedSolarPanel recipe", false).getBoolean(false);
      disableHybridSolarPanelRecipe = config.get("recipes settings", "Disable HybridSolarPanel recipe", false).getBoolean(false);
      disableUltimateSolarPanelRecipe = config.get("recipes settings", "Disable UltimateSolarPanel recipe", false).getBoolean(false);
      disableQuantumSolarPanelRecipe = config.get("recipes settings", "Disable QuantumSolarPanel recipe", false).getBoolean(false);
      disableMolecularTransformerRecipe = config.get("recipes settings", "Disable MolecularTransformer recipe", false).getBoolean(false);
      disableDoubleSlabRecipe = config.get("recipes settings", "Disable DoubleSlab recipe", false).getBoolean(false);
      enableSimpleAdvancedSolarPanelRecipes = config.get("recipes settings", "Enable simple Advanced Solar Panel recipe", false).getBoolean(false);
      enableHardRecipes = config.get("recipes settings", "Enable hard recipes", true).getBoolean(true);
    }
    catch (Exception e)
    {
      System.out.println("[AdvancedSolarPanels] error occurred parsing config file");
      throw new RuntimeException(e);
    }
    finally
    {
      config.save();
    }
    if (side == Side.CLIENT) {
      getIC2Tab();
    }
    blockMolecularTransformer = new BlockMolecularTransformer();
    GameRegistry.registerBlock(blockMolecularTransformer, ItemMolecularTransformer.class, "BlockMolecularTransformer");
    GameRegistry.registerTileEntity(TileEntityMolecularTransformer.class, "Molecular Transformer");
    blockAdvSolarPanel = new BlockAdvSolarPanel();
    GameRegistry.registerBlock(blockAdvSolarPanel, ItemAdvSolarPanel.class, "BlockAdvSolarPanel");
    GameRegistry.registerTileEntity(TileEntityAdvancedSolarPanel.class, "Advanced Solar Panel");
    GameRegistry.registerTileEntity(TileEntityHybridSolarPanel.class, "Hybrid Solar Panel");
    GameRegistry.registerTileEntity(TileEntityUltimateSolarPanel.class, "Ultimate Hybrid Solar Panel");
    GameRegistry.registerTileEntity(TileEntityQuantumSolarPanel.class, "Quantum Solar Panel");
    GameRegistry.registerTileEntity(TileEntityQGenerator.class, "Quantum Generator");
    
    advancedSolarHelmet = new ItemAdvancedSolarHelmet(ItemArmor.ArmorMaterial.DIAMOND, proxy.addArmor("advancedSolarHelmet"), 0, 1).setUnlocalizedName("advancedSolarHelmet");
    GameRegistry.registerItem(advancedSolarHelmet, "advanced_solar_helmet");
    hybridSolarHelmet = new ItemAdvancedSolarHelmet(ItemArmor.ArmorMaterial.DIAMOND, proxy.addArmor("hybridSolarHelmet"), 0, 2).setUnlocalizedName("hybridSolarHelmet");
    GameRegistry.registerItem(hybridSolarHelmet, "hybrid_solar_helmet");
    ultimateSolarHelmet = new ItemAdvancedSolarHelmet(ItemArmor.ArmorMaterial.DIAMOND, proxy.addArmor("ultimateSolarHelmet"), 0, 3).setUnlocalizedName("ultimateSolarHelmet");
    GameRegistry.registerItem(ultimateSolarHelmet, "ultimate_solar_helmet");
    itemAdvanced = new ItemAdvanced();
    GameRegistry.registerItem(itemAdvanced, "asp_crafting_items");
    itemSunnarium = new ItemStack(itemAdvanced.setUnlocalizedName("itemSunnarium"), 1, 0);
    itemSunnariumAlloy = new ItemStack(itemAdvanced.setUnlocalizedName("itemSunnariumAlloy"), 1, 1);
    itemIrradiantUranium = new ItemStack(itemAdvanced.setUnlocalizedName("itemIrradiantUranium"), 1, 2);
    itemEnrichedSunnarium = new ItemStack(itemAdvanced.setUnlocalizedName("itemEnrichedSunnarium"), 1, 3);
    itemEnrichedSunnariumAlloy = new ItemStack(itemAdvanced.setUnlocalizedName("itemEnrichedSunnariumAlloy"), 1, 4);
    itemIrradiantGlassPane = new ItemStack(itemAdvanced.setUnlocalizedName("itemIrradiantGlassPlane"), 1, 5);
    itemIridiumIronPlate = new ItemStack(itemAdvanced.setUnlocalizedName("itemIridiumIronPlate"), 1, 6);
    itemReinforcedIridiumIronPlate = new ItemStack(itemAdvanced.setUnlocalizedName("itemReinforcedIridiumIronPlate"), 1, 7);
    itemIrradiantReinforcedPlate = new ItemStack(itemAdvanced.setUnlocalizedName("itemIrradiantReinforcedPlate"), 1, 8);
    itemSunnariumPart = new ItemStack(itemAdvanced.setUnlocalizedName("itemSunnariumPart"), 1, 9);
    ingotIridium = new ItemStack(itemAdvanced.setUnlocalizedName("ingotIridium"), 1, 10);
    itemUranIngot = new ItemStack(itemAdvanced.setUnlocalizedName("itemUranIngot"), 1, 11);
    itemMTCore = new ItemStack(itemAdvanced.setUnlocalizedName("itemMTCore"), 1, 12);
    itemQuantumCore = new ItemStack(itemAdvanced.setUnlocalizedName("itemQuantumCore"), 1, 13);
    
    itemMolecularTransformer = new ItemStack(blockMolecularTransformer, 1, 0);
    itemUHSP = new ItemStack(blockAdvSolarPanel, 1, 2);
    
    Recipes.compressor.addRecipe(new RecipeInputItemStack(IC2Items.getItem("iridiumOre"), 1), null, new ItemStack[] { ingotIridium });
    Recipes.compressor.addRecipe(new RecipeInputItemStack(IC2Items.getItem("UranFuel"), 1), null, new ItemStack[] { itemUranIngot });
    Recipes.compressor.addRecipe(new RecipeInputItemStack(IC2Items.getItem("uraniumOre"), 1), null, new ItemStack[] { itemUranIngot });
    Recipes.compressor.addRecipe(new RecipeInputItemStack(IC2Items.getItem("crushedUraniumOre"), 1), null, new ItemStack[] { itemUranIngot });
    
    OreDictionary.registerOre("ingotUranium", itemUranIngot);
    OreDictionary.registerOre("ingotIridium", ingotIridium);
    OreDictionary.registerOre("craftingSolarPanelHV", itemUHSP);
    OreDictionary.registerOre("craftingSunnariumPart", itemSunnariumPart);
    OreDictionary.registerOre("craftingSunnarium", itemSunnarium);
    OreDictionary.registerOre("craftingMTCore", itemMTCore);
    OreDictionary.registerOre("craftingMolecularTransformer", itemMolecularTransformer);
    
    proxy.registerRenderers();
    proxy.load();
    
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    
    MTRecipeConfig.doDebug();
  }
  
  @Mod.EventHandler
  public void load(FMLInitializationEvent event) {}
  
  @Mod.EventHandler
  public void afterModsLoaded(FMLPostInitializationEvent event)
  {
    proxy.initRecipes();
    if (!disableAdvancedSolarHelmetRecipe) {
      Recipes.advRecipes.addRecipe(new ItemStack(advancedSolarHelmet, 1), new Object[] { " A ", "RBR", "FDF", Character.valueOf('A'), new ItemStack(blockAdvSolarPanel, 1, 0), Character.valueOf('B'), IC2Items.getItem("nanoHelmet"), Character.valueOf('R'), IC2Items.getItem("advancedCircuit"), Character.valueOf('D'), IC2Items.getItem("lvTransformer"), Character.valueOf('F'), IC2Items.getItem("insulatedGoldCableItem") });
    }
    if (!disableHybridSolarHelmetRecipe) {
      Recipes.advRecipes.addRecipe(new ItemStack(hybridSolarHelmet, 1), new Object[] { " A ", "RBR", "FDF", Character.valueOf('A'), new ItemStack(blockAdvSolarPanel, 1, 1), Character.valueOf('B'), IC2Items.getItem("quantumHelmet"), Character.valueOf('R'), IC2Items.getItem("advancedCircuit"), Character.valueOf('D'), IC2Items.getItem("hvTransformer"), Character.valueOf('F'), IC2Items.getItem("glassFiberCableItem") });
    }
    if (!disableUltimateSolarHelmetRecipe)
    {
      Recipes.advRecipes.addRecipe(new ItemStack(ultimateSolarHelmet, 1), new Object[] { " A ", "RBR", "FDF", Character.valueOf('A'), new ItemStack(blockAdvSolarPanel, 1, 2), Character.valueOf('B'), IC2Items.getItem("quantumHelmet"), Character.valueOf('R'), IC2Items.getItem("advancedCircuit"), Character.valueOf('D'), IC2Items.getItem("hvTransformer"), Character.valueOf('F'), IC2Items.getItem("glassFiberCableItem") });
      
      Recipes.advRecipes.addRecipe(new ItemStack(ultimateSolarHelmet, 1), new Object[] { "A", "B", Character.valueOf('A'), new ItemStack(blockAdvSolarPanel, 1, 2), Character.valueOf('B'), new ItemStack(hybridSolarHelmet, 1) });
    }
    if (!enableHardRecipes)
    {
      if (!disableAdvancedSolarPanelRecipe)
      {
        Recipes.advRecipes.addRecipe(itemIridiumIronPlate, new Object[] { "AAA", "ABA", "AAA", Character.valueOf('A'), "plateIron", Character.valueOf('B'), "ingotIridium" });
        
        GameRegistry.addRecipe(itemReinforcedIridiumIronPlate, new Object[] { "ABA", "BCB", "ABA", Character.valueOf('A'), IC2Items.getItem("advancedAlloy"), Character.valueOf('B'), IC2Items.getItem("carbonPlate"), Character.valueOf('C'), itemIridiumIronPlate });
        
        GameRegistry.addRecipe(itemIrradiantReinforcedPlate, new Object[] { "ABA", "DCD", "AFA", Character.valueOf('A'), Items.redstone, Character.valueOf('B'), itemSunnariumPart, Character.valueOf('D'), new ItemStack(Items.dye, 1, 4), Character.valueOf('C'), itemReinforcedIridiumIronPlate, Character.valueOf('F'), Items.diamond });
        if (enableSimpleAdvancedSolarPanelRecipes) {
          GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 0), new Object[] { "RRR", "DSD", "ABA", Character.valueOf('R'), IC2Items.getItem("reinforcedGlass"), Character.valueOf('D'), IC2Items.getItem("advancedAlloy"), Character.valueOf('S'), IC2Items.getItem("solarPanel"), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), IC2Items.getItem("advancedMachine") });
        } else {
          GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 0), new Object[] { "RRR", "DSD", "ABA", Character.valueOf('R'), IC2Items.getItem("reinforcedGlass"), Character.valueOf('D'), IC2Items.getItem("advancedAlloy"), Character.valueOf('S'), IC2Items.getItem("solarPanel"), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), itemIrradiantReinforcedPlate });
        }
      }
      if ((!disableUltimateSolarPanelRecipe) && (!disableAdvancedSolarPanelRecipe)) {
        GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 2), new Object[] { " A ", "XMX", "RXR", Character.valueOf('A'), Blocks.lapis_block, Character.valueOf('X'), IC2Items.getItem("coalChunk"), Character.valueOf('M'), new ItemStack(blockAdvSolarPanel, 1, 0), Character.valueOf('R'), itemSunnariumAlloy });
      }
      if ((!disableHybridSolarPanelRecipe) && (!disableAdvancedSolarPanelRecipe))
      {
        GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 1), new Object[] { "XMX", "DSD", "ABA", Character.valueOf('X'), IC2Items.getItem("carbonPlate"), Character.valueOf('M'), Blocks.lapis_block, Character.valueOf('D'), IC2Items.getItem("iridiumPlate"), Character.valueOf('S'), new ItemStack(blockAdvSolarPanel, 1, 0), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), itemSunnarium });
        if (!disableUltimateSolarPanelRecipe) {
          GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 2), new Object[] { "MMM", "MXM", "MMM", Character.valueOf('M'), new ItemStack(blockAdvSolarPanel, 1, 1), Character.valueOf('X'), IC2Items.getItem("advancedCircuit") });
        }
      }
    }
    else
    {
      if (!disableAdvancedSolarPanelRecipe)
      {
        ArrayList iridiumList = OreDictionary.getOres("ingotIridium");
        Iterator iterator = iridiumList.iterator();
        while (iterator.hasNext())
        {
          ItemStack itemStack = (ItemStack)iterator.next();
          GameRegistry.addRecipe(itemIridiumIronPlate, new Object[] { "AAA", "ABA", "AAA", Character.valueOf('A'), Items.iron_ingot, Character.valueOf('B'), itemStack });
        }
        GameRegistry.addRecipe(itemReinforcedIridiumIronPlate, new Object[] { "ABA", "BCB", "ABA", Character.valueOf('A'), IC2Items.getItem("advancedAlloy"), Character.valueOf('B'), IC2Items.getItem("carbonPlate"), Character.valueOf('C'), itemIridiumIronPlate });
        
        GameRegistry.addRecipe(itemIrradiantReinforcedPlate, new Object[] { "ABA", "DCD", "AFA", Character.valueOf('A'), Items.redstone, Character.valueOf('B'), itemSunnariumPart, Character.valueOf('D'), new ItemStack(Items.dye, 1, 4), Character.valueOf('C'), itemReinforcedIridiumIronPlate, Character.valueOf('F'), Items.diamond });
        if (enableSimpleAdvancedSolarPanelRecipes) {
          GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 0), new Object[] { "RRR", "DSD", "ABA", Character.valueOf('R'), itemIrradiantGlassPane, Character.valueOf('D'), IC2Items.getItem("advancedAlloy"), Character.valueOf('S'), IC2Items.getItem("solarPanel"), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), IC2Items.getItem("advancedMachine") });
        } else {
          GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 0), new Object[] { "RRR", "DSD", "ABA", Character.valueOf('R'), itemIrradiantGlassPane, Character.valueOf('D'), IC2Items.getItem("advancedAlloy"), Character.valueOf('S'), IC2Items.getItem("solarPanel"), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), itemIrradiantReinforcedPlate });
        }
      }
      if ((!disableUltimateSolarPanelRecipe) && (!disableAdvancedSolarPanelRecipe)) {
        GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 2), new Object[] { " A ", "XMX", "RXR", Character.valueOf('A'), Blocks.lapis_block, Character.valueOf('X'), IC2Items.getItem("coalChunk"), Character.valueOf('M'), new ItemStack(blockAdvSolarPanel, 1, 0), Character.valueOf('R'), itemEnrichedSunnariumAlloy });
      }
      if ((!disableHybridSolarPanelRecipe) && (!disableAdvancedSolarPanelRecipe)) {
        GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 1), new Object[] { "XMX", "DSD", "ABA", Character.valueOf('X'), IC2Items.getItem("carbonPlate"), Character.valueOf('M'), Blocks.lapis_block, Character.valueOf('D'), IC2Items.getItem("iridiumPlate"), Character.valueOf('S'), new ItemStack(blockAdvSolarPanel, 1, 0), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), itemEnrichedSunnarium });
      }
    }
    if (!disableUltimateSolarPanelRecipe)
    {
      GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 2), new Object[] { "MMM", "MXM", "MMM", Character.valueOf('M'), new ItemStack(blockAdvSolarPanel, 1, 1), Character.valueOf('X'), IC2Items.getItem("advancedCircuit") });
      GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 8, 1), new Object[] { "X", Character.valueOf('X'), new ItemStack(blockAdvSolarPanel, 1, 2) });
    }
    if (!disableMolecularTransformerRecipe)
    {
      ItemStack itemfix = IC2Items.getItem("reactorReflectorThick");
      itemfix.setItemDamage(1);
      GameRegistry.addRecipe(itemMTCore, new Object[] { "MXM", "M M", "MXM", 'M', itemIrradiantGlassPane, 'X', itemfix });
      
      GameRegistry.addRecipe(itemMolecularTransformer, new Object[] { "MXM", "ABA", "MXM", Character.valueOf('M'), IC2Items.getItem("advancedMachine"), Character.valueOf('X'), IC2Items.getItem("evTransformer"), Character.valueOf('A'), IC2Items.getItem("advancedCircuit"), Character.valueOf('B'), itemMTCore });
    }
    if (!disableQuantumSolarPanelRecipe)
    {
      GameRegistry.addRecipe(itemQuantumCore, new Object[] { "XRX", "RSR", "XRX", Character.valueOf('X'), itemEnrichedSunnariumAlloy, Character.valueOf('R'), Items.nether_star, Character.valueOf('S'), Items.ender_eye });
      GameRegistry.addRecipe(new ItemStack(blockAdvSolarPanel, 1, 3), new Object[] { "XXX", "XRX", "XXX", Character.valueOf('X'), new ItemStack(blockAdvSolarPanel, 1, 2), Character.valueOf('R'), itemQuantumCore });
    }
    GameRegistry.addRecipe(new ShapedOreRecipe(itemIrradiantUranium, new Object[] { " R ", "RSR", " R ", Character.valueOf('R'), Items.glowstone_dust, Character.valueOf('S'), "ingotUranium" }));
    GameRegistry.addRecipe(setItemsSize(itemIrradiantGlassPane, 6), new Object[] { "RRR", "ASA", "RRR", Character.valueOf('R'), IC2Items.getItem("reinforcedGlass"), Character.valueOf('A'), itemIrradiantUranium, Character.valueOf('S'), Items.glowstone_dust });
    GameRegistry.addRecipe(itemEnrichedSunnarium, new Object[] { "RRR", "RSR", "RRR", Character.valueOf('R'), itemIrradiantUranium, Character.valueOf('S'), itemSunnarium });
    GameRegistry.addRecipe(itemEnrichedSunnariumAlloy, new Object[] { " R ", "RSR", " R ", Character.valueOf('R'), itemEnrichedSunnarium, Character.valueOf('S'), itemSunnariumAlloy });
    GameRegistry.addRecipe(itemSunnariumAlloy, new Object[] { "MMM", "MXM", "MMM", Character.valueOf('M'), IC2Items.getItem("iridiumPlate"), Character.valueOf('X'), itemSunnarium });
    
    GameRegistry.addRecipe(itemSunnarium, new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), itemSunnariumPart });
    if (!disableDoubleSlabRecipe) {
      GameRegistry.addRecipe(new ItemStack(Blocks.double_stone_slab, 1, 0), new Object[] { "A", "A", Character.valueOf('A'), new ItemStack(Blocks.stone_slab, 1, 0) });
    }
  }
}
