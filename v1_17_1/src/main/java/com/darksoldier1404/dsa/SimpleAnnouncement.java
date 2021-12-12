package com.darksoldier1404.dsa;

import com.darksoldier1404.dsa.commands.DSACommand;
import com.darksoldier1404.dsa.functions.DSAFunction;
import com.darksoldier1404.duc.UniversalCore;
import com.darksoldier1404.duc.utils.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("all")
public class SimpleAnnouncement extends JavaPlugin {
    public UniversalCore core;
    private static SimpleAnnouncement plugin;
    public YamlConfiguration config;
    public String prefix;
    public BukkitTask announcementTask;

    public static SimpleAnnouncement getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Plugin pl = getServer().getPluginManager().getPlugin("DP-UniversalCore");
        if(pl == null) {
            getLogger().warning("DP-UniversalCore 플러그인이 설치되어있지 않습니다.");
            getLogger().warning("DP-SimplePrefix 플러그인을 비활성화 합니다.");
            plugin.setEnabled(false);
            return;
        }
        core = (UniversalCore) pl;
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Settings.prefix"));
        DSAFunction.initAnnouncementsTask();
        getCommand("공지").setExecutor(new DSACommand());
    }

    public void onDisable() {
    }
}
