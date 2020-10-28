package org.ukmms.tigen.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theoly
 * @date 2020/10/28
 */
@State(name = "TiGenSetting", storages = @Storage("tigen-setting.xml"))
public class Settings implements PersistentStateComponent<Settings> {

    /**
     * author
     */
    private String author;

    /**
     * profile path
     */
    private String profile;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Settings(){
        this.author = "theoly";
        this.profile = "tigen/profile";
    }

    @Override
    public @Nullable Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings state) {
        // load from state
        if(state.author != null){
            XmlSerializerUtil.copyBean(state, this);
        }
    }

    /**
     * 获取单例实例对象
     *
     * @return 实例对象
     */
    public static Settings getInstance() {
        return ServiceManager.getService(Settings.class);
    }
}
