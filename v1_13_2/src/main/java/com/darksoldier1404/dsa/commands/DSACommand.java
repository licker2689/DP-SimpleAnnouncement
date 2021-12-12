package com.darksoldier1404.dsa.commands;

import com.darksoldier1404.dsa.SimpleAnnouncement;
import com.darksoldier1404.dsa.functions.DSAFunction;
import com.darksoldier1404.duc.utils.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class DSACommand implements CommandExecutor, TabCompleter {
    private final String prefix = SimpleAnnouncement.getInstance().prefix;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(prefix + "관리자 전용 명령어입니다.");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(prefix + "/공지 추가 <내용> - 공지를 추가합니다.");
            sender.sendMessage(prefix + "/공지 삭제 <번호> - 공지를 삭제합니다.");
            sender.sendMessage(prefix + "/공지 목록 - 공지목록을 보여줍니다.");
            sender.sendMessage(prefix + "/공지 수정 <번호> <내용> - 공지를 수정합니다.");
            sender.sendMessage(prefix + "/공지 간격 <초> - 공지 출력 간격을 설정합니다.");
            sender.sendMessage(prefix + "/공지 랜덤 - 공지 출력을 랜덤하게 설정하거나 순서대로 출력되게 설정합니다.");
            sender.sendMessage(prefix + "/공지 리로드 - 콘피그 설정을 리로드합니다.");
            return false;
        }
        if (args[0].equals("추가")) {
            if (args.length == 1) {
                sender.sendMessage(prefix + "추가할 공지 내용을 입력해주세요.");
                return false;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String announcement = ChatColor.translateAlternateColorCodes('&', sb.toString());
            DSAFunction.addAnnouncement(sender, announcement);
            return false;
        }
        if (args[0].equals("삭제")) {
            if (args.length == 1) {
                sender.sendMessage(prefix + "삭제할 공지 번호를 입력해주세요.");
                return false;
            }
            try {
                int index = Integer.parseInt(args[1]);
                DSAFunction.removeAnnouncement(sender, index);
            } catch (NumberFormatException e) {
                sender.sendMessage(prefix + "잘못된 번호입니다.");
            }
            return false;
        }
        if (args[0].equals("목록")) {
            DSAFunction.showAnnouncements(sender);
            return false;
        }
        if (args[0].equals("수정")) {
            if (args.length == 1) {
                sender.sendMessage(prefix + "수정할 공지 번호를 입력해주세요.");
                return false;
            }
            if (args.length == 2) {
                sender.sendMessage(prefix + "수정할 공지 내용을 입력해주세요.");
                return false;
            }
            try {
                int index = Integer.parseInt(args[1]);
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String announcement = ChatColor.translateAlternateColorCodes('&', sb.toString());
                DSAFunction.editAnnouncement(sender, index, announcement);
            } catch (NumberFormatException e) {
                sender.sendMessage(prefix + "잘못된 번호입니다.");
            }
            return false;
        }
        if (args[0].equals("간격")) {
            if (args.length == 1) {
                sender.sendMessage(prefix + "공지 출력 간격을 입력해주세요.");
                return false;
            }
            try {
                int interval = Integer.parseInt(args[1]);
                DSAFunction.setAnnouncementInterval(sender, interval);
            } catch (NumberFormatException e) {
                sender.sendMessage(prefix + "잘못된 간격입니다.");
            }
            return false;
        }
        if (args[0].equals("랜덤")) {
            DSAFunction.setAnnouncementRandom(sender);
            return false;
        }
        if (args[0].equals("리로드")) {
            SimpleAnnouncement.getInstance().config = ConfigUtils.reloadPluginConfig(SimpleAnnouncement.getInstance(), SimpleAnnouncement.getInstance().config);
            DSAFunction.initAnnouncementsTask();
            sender.sendMessage(prefix + "콘피그 설정이 리로드 되었습니다.");
            return false;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.isOp()) {
            return null;
        }
        if (args.length == 1) {
            return Arrays.asList("추가", "삭제", "목록", "수정", "간격", "랜덤", "리로드");
        }
        return null;
    }
}
