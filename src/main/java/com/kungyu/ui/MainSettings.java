package com.kungyu.ui;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.kungyu.model.common.UrlPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wengyongcheng
 * @since 2020/6/28 10:14 上午
 */
@State(name = "MainSettings", storages = {@Storage("main-setting.xml")})
public class MainSettings implements PersistentStateComponent<MainSettings> {

    public Map<String, UrlPair> getSettingMap() {
        return settingMap;
    }

    public void setSettingMap(Map<String, UrlPair> settingMap) {
        this.settingMap = settingMap;
    }

    private Map<String, UrlPair> settingMap;


    public static MainSettings getInstance() {
        return ServiceManager.getService(MainSettings.class);
    }

    public MainSettings() {
        init();
    }

    private void init() {
        settingMap = new HashMap<>();
    }

    @Nullable
    @Override
    public MainSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MainSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
