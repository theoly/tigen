package org.ukmms.tigen.config;

import com.intellij.ide.fileTemplates.impl.UrlUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.domain.TigenProfile;
import org.ukmms.tigen.domain.TigenTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
     * active profile
     */
    private TigenProfile profile;

    /**
     * profiles
     */
    private Map<String, TigenProfile> profileMap;

    public Settings() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get singleton
     *
     * @return settings
     */
    public static Settings getInstance() {
        return ServiceManager.getService(Settings.class);
    }

    /**
     * load template file
     *
     * @return file content
     */
    public static String loadResTemplate(String filePath) {
        try {
            return UrlUtil.loadText(Settings.class.getResource(filePath)).replace("\r", "");
        } catch (IOException e) {
            ExceptionUtil.rethrow(e);
        }
        return "";
    }

    public static List<TigenTemplate> loadResTemplateList(String dirPath) throws IOException {
        List<TigenTemplate> ret = new ArrayList<>();
        List<String> fileNodes = UrlUtil.getChildrenRelativePaths(Settings.class.getResource(dirPath));
        fileNodes.forEach(fn -> {
            String fPath = dirPath + "/" + fn;
            String extension = FileUtilRt.getExtension(fn);
            switch (extension) {
                case "btl":
                    ret.add(new TigenTemplate(fn.substring(0, fn.length() - 4), "beetl", loadResTemplate(fPath)));
                    break;
                case "ftl":
                    ret.add(new TigenTemplate(fn.substring(0, fn.length() - 4), "freemarker", loadResTemplate(fPath)));
                    break;
                case "vm":
                    ret.add(new TigenTemplate(fn.substring(0, fn.length() - 3), "velocity", loadResTemplate(fPath)));
                    break;
            }
        });

        return ret;
    }



    public String getSettingsRoot() {
        String userHome = System.getProperty("user.home");
        return userHome + "/.tigen";
    }

    public TigenProfile getProfileByName(String profileName) {
        return profileMap.get(profileName);
    }

    /**
     * load profile from path
     * @param profileFile profile file handler
     * @return tigenProfile
     */
    public TigenProfile loadProfile(File  profileFile) throws IOException {
        TigenProfile tigenProfile = new TigenProfile();
        if (!profileFile.exists() || !profileFile.isDirectory()) {
            return null;
        }else{
            tigenProfile.setName(profileFile.getName());
            tigenProfile.setPath(profileFile.getAbsolutePath());
            List<TigenTemplate> templates = loadTemplates(profileFile);
            tigenProfile.setTemplates(templates);
        }
        return tigenProfile;
    }

    /**
     * lsit templates from file's dir
     * @param profileDirFile dir file
     * @return templates under file's dir
     */
    public List<TigenTemplate> loadTemplates(File profileDirFile) throws IOException {
        List<TigenTemplate> templates = new ArrayList<>();

        if (profileDirFile.exists() && profileDirFile.isDirectory()) {
            File[] files = profileDirFile.listFiles(f -> {
                boolean ret = false;
                if (f.isFile()) {
                    String[] sfx = {"btl", "ftl", "vm"};
                    if (Arrays.asList(sfx).contains(FileUtilRt.getExtension(f.getName()))) {
                        ret = true;
                    }
                }
                return ret;
            });

            for (File file : files) {
                String extension = FileUtilRt.getExtension(file.getName());
                String fn = file.getName();
                String templateName = TigenTemplate.getTemplateName(fn, extension);
                switch (extension) {
                    case "btl":
                        templates.add(new TigenTemplate(templateName, "beetl", FileUtil.loadFile(file)));
                        break;
                    case "ftl":
                        templates.add(new TigenTemplate(templateName, "freemarker", FileUtil.loadFile(file)));
                        break;
                    case "vm":
                        templates.add(new TigenTemplate(templateName, "velocity", FileUtil.loadFile(file)));
                        break;
                }
            }
        }

        return templates;
    }

    public TigenProfile getDefaultProfile() throws IOException {
        TigenProfile tigenProfile = new TigenProfile();
        tigenProfile.setName("default");
        tigenProfile.setPath(getSettingsRoot() + "/profile/default");
        List<TigenTemplate> templates = loadResTemplateList("/template/default");
        tigenProfile.setTemplates(templates);

        return tigenProfile;
    }

    /**
     * init default profile
     * @throws IOException
     */
    public void init() throws IOException {
        this.author = "theoly";

        String settingsRoot = getSettingsRoot();
        // profiles
        File profileDir = new File(settingsRoot + "/profile");
        if (!profileDir.exists() || !profileDir.isDirectory()) {
            profileDir.delete();
            profileDir.mkdirs();
            this.profile = getDefaultProfile();
            this.profileMap = new HashMap<>();
            this.profileMap.put("default", this.profile);
        }else{
            File[] dirs = profileDir.listFiles(pathname -> pathname.isDirectory());
            profileMap = new HashMap<>();
            for (File dir : dirs) {
                TigenProfile tigenProfile = loadProfile(dir);
                profileMap.put(dir.getName(), tigenProfile);
            }

            TigenProfile defaultProfile = profileMap.get("default");
            if(defaultProfile == null){
                this.profile = getDefaultProfile();
                this.profileMap.put("default", this.profile);
            }else{
                this.profile = defaultProfile;
            }
        }
    }

    @Override
    public @Nullable Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings state) {
        // load from state
        if (state.author != null) {
            XmlSerializerUtil.copyBean(state, this);
        }
    }

    /**
     * write all profile to disk
     */
    public void saveProfiles() {
        this.profileMap.keySet().forEach(pn -> {
            saveProfile(this.profileMap.get(pn));
        });
    }

    /**
     * write profile to disk
     * @param profile
     */
    public void saveProfile(TigenProfile profile){
        String path = profile.getPath();
        File profileDir = new File(path);
        if(profileDir.exists() && profileDir.isDirectory()) {
            for (TigenTemplate template : profile.getTemplates()) {
                File tf = new File(path + "/" + getTemplateFileName(template));
                try (FileWriter fileWriter = new FileWriter(tf.getAbsoluteFile());) {
                    BufferedWriter bw = new BufferedWriter(fileWriter);
                    bw.write(template.getCode());
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else{
            profileDir.delete();
            profileDir.mkdirs();
        }
    }

    public String getTemplateFileName(TigenTemplate template) {
        String fn = template.getName();
        switch (template.getEngine()) {
            case "beetl":
                fn = fn.concat(".btl");
                break;
            case "freemarker":
                fn = fn.concat(".ftl");
                break;
            case "velocity":
                fn = fn.concat(".vm");
                break;
        }

        return fn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public TigenProfile getProfile() {
        return profile;
    }

    public void setProfile(TigenProfile profile) {
        this.profile = profile;
    }

    public Map<String, TigenProfile> getProfileMap() {
        return profileMap;
    }

    public void setProfileMap(Map<String, TigenProfile> profileMap) {
        this.profileMap = profileMap;
    }

}
