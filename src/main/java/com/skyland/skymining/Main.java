package com.skyland.skymining;

import java.io.File;
import java.io.IOException;

import com.skyland.skymining.commands.GiveBookCommand;
import com.skyland.skymining.commands.MinaCommand;
import com.skyland.skymining.listeners.BlockBreak;
import com.skyland.skymining.listeners.HasteListener;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main i;

    @Override
    public void onLoad() {
        setInstance(this);
    }

    @Override
    public void onEnable() {

        new MinaCommand(this);
        new GiveBookCommand(this);

        new BlockBreak(this);
        Bukkit.getPluginManager().registerEvents(new HasteListener(), this);

        checkWorld();
        copy();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        delete();
    }

    private void checkWorld() {
        final World mundoori = Bukkit.getWorld("minaoriginal");
        if (mundoori == null) {
            this.criarMundo("minaoriginal");
        }
    }

    public void copy() {
        File srcDir = new File("minaoriginal");
        if (!srcDir.exists()) {
            Bukkit.getLogger().warning("Mapa 'minaoriginal' não existe");
            return;
        }
        File destDir = new File("mina");
        Bukkit.getWorld("minaoriginal").getEntities().stream().forEach(r -> r.remove());
        try {
            FileUtils.copyDirectory(srcDir, destDir);
            Bukkit.getLogger().warning("Mapa 'minaoriginal' foi copiado para 'mina' com sucesso!");

            World w = Bukkit.getWorld("mina");
            Bukkit.getServer().getWorlds().add(w);
            w.setSpawnFlags(false, false);
            Bukkit.getLogger().warning("Mapa 'mina' adicionado na lista de mundos.");
            Bukkit.getServer().unloadWorld("minaoriginal", false);
            Bukkit.getLogger().warning("Mapa 'minaoriginal' foi desativado!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void delete() {
        Bukkit.getServer().unloadWorld("mina", false);
        try {
            File dir = new File(Bukkit.getServer().getWorld("mina").getWorldFolder().getPath());
            dir.delete();
            Bukkit.getLogger().warning("Mapa 'mina' foi deletado com sucesso!");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void criarMundo(String nome) {
        final WorldCreator mundo = WorldCreator.name(nome);
        mundo.generatorSettings(
                "{\"coordinateScale\":684.412,\"heightScale\":684.412,\"LowerLimitScale\":512.0,\"upperLimitScale\":512.0,\"depthNoiseScaleX\":200.0,\"depthNoiseScaleZ\":200.0,\"depthNoiseScaleExponent\":0.5,\"mainNoiseScaleX\":80.0,\"mainNoiseScaleY\":160.0,\"mainNoiseScaleZ\":80.0,\"baseSize\":8.5,\"stretchY\":12.0,\"biomeDepthWeight\":1.0,\"biomeDepthOffset\":0.0,\"biomeScaleWeight\":1.0,\"biomeScaleOffset\":0.0,\"seaLevel\":63,\"useCaves\":true,\"useDungeons\":false,\"dungeonChance\":0,\"useStrongholds\":true,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useMonuments\":false,\"useRavines\":true,\"useWaterLakes\":false,\"waterLakeChance\":4,\"useLavaLakes\":false,\"lavaLakeChance\":80,\"useLavaOceans\":false,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":256,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":256,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":1,\"coalCount\":0,\"coalMinHeight\":0,\"coalMaxHeight\":0,\"ironSize\":1,\"ironCount\":0,\"ironMinHeight\":0,\"ironMaxHeight\":0,\"goldSize\":1,\"goldCount\":0,\"goldMinHeight\":0,\"goldMaxHeight\":0,\"redstoneSize\":1,\"redstoneCount\":0,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":0,\"diamondSize\":1,\"diamondCount\":0,\"DiamondMinHeight\":0,\"diamondMaxHeight\":0,\"lapisSize\":1,\"lapisCount\":0,\"lapisCenterHeight\":0,\"lapisSpread\":0}");
        mundo.createWorld();
    }

    public static final Main get() {
        return i;
    }

    public static void setInstance(Main i) {
        Main.i = i;
    }
}
