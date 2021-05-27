package com.ruinscraft.cinemadisplays.cef;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.screen.Screen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowserOsr;

import java.io.File;

public final class CefUtil {

    private static boolean init;
    private static CefApp cefAppInstance;
    private static CefClient cefClientInstance;

    public static boolean init() {
        String[] cefSwitches = new String[]{
                "--autoplay-policy=no-user-gesture-required",
                "--disable-web-security"
        };

        if (!CefApp.startup(cefSwitches)) {
            return false;
        }

        CefSettings cefSettings = new CefSettings();
        cefSettings.windowless_rendering_enabled = true;
        cefSettings.background_color = cefSettings.new ColorType(0, 255, 255, 255);
        cefSettings.cache_path = new File("chromium", "cache").getAbsolutePath();
        cefSettings.ignore_certificate_errors = true;
        cefSettings.user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36";

        cefAppInstance = CefApp.getInstance(cefSwitches, cefSettings);
        cefClientInstance = cefAppInstance.createClient();
        cefClientInstance.addLoadHandler(new LoadHandler());

        return init = true;
    }

    public static boolean isInit() {
        return init;
    }

    public static CefApp getCefApp() {
        return cefAppInstance;
    }

    public static CefClient getCefClient() {
        return cefClientInstance;
    }

    public static CefBrowserOsr createBrowser(String startUrl, Screen screen) {
        if (!init) {
            return null;
        }

        CefBrowserOsr browser = (CefBrowserOsr) cefClientInstance.createBrowser(startUrl, true, false);
        browser.setCloseAllowed();
        browser.createImmediately();

        // Adjust screen size
        {
            int widthBlocks = screen.getWidth();
            int heightBlocks = screen.getHeight();
            double scale = (double) widthBlocks / (double) heightBlocks;
            int height = 720;
            int width = (int) Math.floor(height * scale);
            browser.resize(width, height);
        }

        return browser;
    }

    public static void registerCefTick() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.world == null) {
                CinemaDisplaysMod.getInstance().getScreenManager().unloadAll();
            }
        });

        ClientTickEvents.START_WORLD_TICK.register(client -> {
            if (CefUtil.isInit()) {
                CefUtil.getCefApp().N_DoMessageLoopWork();
                CinemaDisplaysMod.getInstance().getScreenManager().updateAll();
            }
        });
    }

}
