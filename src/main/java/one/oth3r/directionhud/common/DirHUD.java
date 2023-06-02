package one.oth3r.directionhud.common;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import one.oth3r.directionhud.fabric.DirectionHUD;
import one.oth3r.directionhud.fabric.files.LangReader;
import one.oth3r.directionhud.fabric.files.PlayerData;
import one.oth3r.directionhud.fabric.files.config;
import one.oth3r.directionhud.fabric.utils.CTxT;
import one.oth3r.directionhud.fabric.utils.CUtl;

public class DirHUD {
    public static void setDefaults(ServerPlayerEntity player) {
        config.DESTAutoClear = PlayerData.get.dest.setting.autoclear(player);
        config.DESTAutoClearRad = PlayerData.get.dest.setting.autoclearrad(player);
        config.DESTAutoConvert = PlayerData.get.dest.setting.autoconvert(player);
        config.DESTDestParticles = PlayerData.get.dest.setting.particle.dest(player);
        config.DESTDestParticleColor = PlayerData.get.dest.setting.particle.destcolor(player);
        config.DESTLineParticles = PlayerData.get.dest.setting.particle.line(player);
        config.DESTLineParticleColor = PlayerData.get.dest.setting.particle.linecolor(player);
        config.DESTTrackingParticles = PlayerData.get.dest.setting.particle.tracking(player);
        config.DESTTrackingParticleColor = PlayerData.get.dest.setting.particle.trackingcolor(player);
        config.DESTYLevel = PlayerData.get.dest.setting.ylevel(player);
        config.DESTSend = PlayerData.get.dest.setting.send(player);
        config.DESTTrack = PlayerData.get.dest.setting.track(player);

        config.HUD24HR = PlayerData.get.hud.setting.time24h(player);
        config.HUDTracking = PlayerData.get.hud.module.tracking(player);
        config.HUDCoordinates = PlayerData.get.hud.module.coordinates(player);
        config.HUDDistance = PlayerData.get.hud.module.distance(player);
        config.HUDDirection = PlayerData.get.hud.module.direction(player);
        config.HUDDestination = PlayerData.get.hud.module.destination(player);
        config.HUDTime = PlayerData.get.hud.module.time(player);
        config.HUDWeather = PlayerData.get.hud.module.weather(player);

        config.HUDEnabled = PlayerData.get.hud.state(player);
        config.HUDOrder = PlayerData.get.hud.order(player);

        config.HUDPrimaryColor = HUD.color.getHUDColors(player)[0];
        config.HUDPrimaryBold = HUD.color.getHUDBold(player,1);
        config.HUDPrimaryItalics = HUD.color.getHUDItalics(player, 1);

        config.HUDSecondaryColor = HUD.color.getHUDColors(player)[1];
        config.HUDSecondaryBold = HUD.color.getHUDBold(player, 2);
        config.HUDSecondaryItalics = HUD.color.getHUDItalics(player, 2);
        config.save();
        player.sendMessage(CUtl.tag().append(CUtl.lang("dirhud.defaults.set")).b());
    }
    public static void resetDefaults(ServerPlayerEntity player) {
        config.resetDefaults();
        player.sendMessage(CUtl.tag().append(CUtl.lang("dirhud.defaults.reset")).b());
    }
    public static void defaults(ServerPlayerEntity player) {
        CTxT msg = CTxT.of("");
        msg.append(CUtl.lang("dirhud.ui.defaults").color(CUtl.pTC()))
                .append(CTxT.of("\n                                 \n").strikethrough(true))
                .append(" ")
                .append(CUtl.TBtn("dirhud.defaults.set").btn(true).color(CUtl.c.set).cEvent(1,"/dirhud defaults set")
                        .hEvent(CUtl.TBtn("dirhud.defaults.set.hover")))
                .append("  ")
                .append(CUtl.TBtn("dirhud.defaults.reset").btn(true).color('c').cEvent(1,"/dirhud defaults reset")
                        .hEvent(CUtl.TBtn("dirhud.defaults.reset.hover")))
                .append("  ")
                .append(CUtl.CButton.back("/dirhud"))
                .append(CTxT.of("\n                                 ").strikethrough(true));
        player.sendMessage(msg.b());
    }
    public static void reload(ServerPlayerEntity player) {
        if (player != null && !player.hasPermissionLevel(2)) return;
        if (DirectionHUD.configFile == null) DirectionHUD.configFile = FabricLoader.getInstance().getConfigDir().toFile()+"/";
        LangReader.loadLanguageFile();
        config.load();
        for (ServerPlayerEntity pl:DirectionHUD.server.getPlayerManager().getPlayerList()) {
            PlayerData.removePlayer(pl);
            PlayerData.addPlayer(pl);
        }
        if (player == null) DirectionHUD.LOGGER.info(CUtl.lang("dirhud.reload", CUtl.lang("dirhud.reload_2")).getString());
        else player.sendMessage(CUtl.tag().append(CUtl.lang("dirhud.reload",CUtl.lang("dirhud.reload_2").color('a'))).b());
    }
    public static void UI(ServerPlayerEntity player) {
        CTxT msg = CTxT.of("")
                .append(CTxT.of(" DirectionHUD ").color(CUtl.pTC()))
                .append(CTxT.of(DirectionHUD.VERSION+"⧉").color(CUtl.sTC()).cEvent(3,"https://modrinth.com/mod/directionhud/changelog")
                        .hEvent(CUtl.TBtn("version.hover").color(CUtl.sTC())))
                .append(CTxT.of("\n                                 \n").strikethrough(true)).append(" ");
        //hud
        if (config.HUDEditing) msg.append(CUtl.CButton.dirHUD.hud()).append("  ");
        //dest
        msg.append(CUtl.CButton.dirHUD.dest());
        if (!DirectionHUD.server.isRemote()) {
            msg.append("\n\n ").append(CUtl.CButton.dirHUD.defaults());
        } else if (player.hasPermissionLevel(2)) {
            msg.append("\n\n ").append(CUtl.CButton.dirHUD.reload());
        }
        msg.append(CTxT.of("\n                                 ").strikethrough(true));
        player.sendMessage(msg.b());
    }
}