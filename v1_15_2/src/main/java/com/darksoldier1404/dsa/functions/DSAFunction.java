package com.darksoldier1404.dsa.functions;

import com.darksoldier1404.dsa.SimpleAnnouncement;
import com.darksoldier1404.duc.utils.ConfigUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

@SuppressWarnings("all")
public class DSAFunction {
    private static final SimpleAnnouncement plugin = SimpleAnnouncement.getInstance();
    private static final YamlConfiguration config = plugin.config;
    private static final String prefix = plugin.prefix;

    public static void addAnnouncement(CommandSender sender, String s) {

        List<String> announcements = config.getStringList("Settings.announcements");
        announcements.add(s);
        config.set("Settings.announcements", announcements);
        ConfigUtils.savePluginConfig(plugin, config);
        sender.sendMessage(prefix + "공지가 추가되었습니다.");
        initAnnouncementsTask();
    }

    // remove announcement by index

    public static void removeAnnouncement(CommandSender sender, int index) {
        List<String> announcements = config.getStringList("Settings.announcements");
        if (announcements.size() <= index) {
            sender.sendMessage(prefix + "공지를 찾을 수 없습니다.");
            return;
        }
        announcements.remove(index);
        config.set("Settings.announcements", announcements);
        ConfigUtils.savePluginConfig(plugin, config);
        sender.sendMessage(prefix + "공지가 삭제되었습니다.");
        initAnnouncementsTask();
    }

    // clear announcement

    public static void clearAnnouncement(CommandSender sender){
        List<String> announcements = config.getStringList("Settings.announcements");
        announcements.clear();
        config.set("Settings.announcements", announcements);
        ConfigUtils.savePluginConfig(plugin, config);
        sender.sendMessage(prefix + "공지가 초기화 되었습니다.");
        initAnnouncementsTask();
    }

    // edit announcement by index

    public static void editAnnouncement(CommandSender sender, int index, String s) {
        List<String> announcements = config.getStringList("Settings.announcements");
        if (announcements.size() <= index) {
            sender.sendMessage(prefix + "공지를 찾을 수 없습니다.");
            return;
        }
        announcements.set(index, s);
        config.set("Settings.announcements", announcements);
        ConfigUtils.savePluginConfig(plugin, config);
        sender.sendMessage(prefix + "공지가 수정되었습니다.");
        initAnnouncementsTask();
    }

    // show announcements list

    public static void showAnnouncements(CommandSender sender) {
        List<String> announcements = config.getStringList("Settings.announcements");
        if (announcements.size() == 0) {
            sender.sendMessage(prefix + "공지가 없습니다.");
            return;
        }
        for (String announcement : announcements) {
            sender.sendMessage(prefix + announcement);
        }
    }

    // set announcement interval

    public static void setAnnouncementInterval(CommandSender sender, int interval) {
        config.set("Settings.interval", interval);
        ConfigUtils.savePluginConfig(plugin, config);
        sender.sendMessage(prefix + "공지 출력 간격이" + interval + "초로 설정되었습니다.");
        initAnnouncementsTask();
    }

    // set announcement random

    public static void setAnnouncementRandom(CommandSender sender) {
        boolean random = config.getBoolean("Settings.random");
        if (random) {
            config.set("Settings.random", false);
            sender.sendMessage(prefix + "공지 출력 순서가 정해진 순서로 출력됩니다.");
        } else {
            config.set("Settings.random", true);
            sender.sendMessage(prefix + "공지 출력 순서가 랜덤으로 출력됩니다.");
        }
        ConfigUtils.savePluginConfig(plugin, config);
        initAnnouncementsTask();
    }

    // init announcements task

    public static void initAnnouncementsTask() {
        try {
            plugin.announcementTask.cancel();
            plugin.announcementTask = null;
        } catch (Exception ignored) {
        }
        plugin.announcementTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            List<String> announcements = config.getStringList("Settings.announcements");
            @Override
            public void run() {
                if (announcements.size() == 0) {
                    announcements = config.getStringList("Settings.announcements");
                    return;
                }
                if (config.getBoolean("Settings.random")) {
                    int index = (int) (Math.random() * announcements.size());
                    plugin.getServer().broadcastMessage(prefix + announcements.get(index));
                    announcements.remove(index);
                } else {
                    plugin.getServer().broadcastMessage(prefix + announcements.get(0));
                    announcements.add(announcements.get(0));
                    announcements.remove(0);
                }
            }
        }, 20L, config.getInt("Settings.interval") * 20);
    }
}
