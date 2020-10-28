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
import org.ukmms.tigen.domain.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * profiles
     */
    private List<String> profileList;

    /**
     * template list
     */
    private List<Template> templates;

    public Settings() {
        try {
            initDefault();
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

    public static List<Template> loadProfile(String profilePath) throws IOException {
        List<Template> templs = new ArrayList<>();
        File profileDir = new File(profilePath);
        if (profileDir.exists() && profileDir.isDirectory()) {
            File[] files = profileDir.listFiles(f -> {
                boolean ret = false;
                if (f.isFile()) {
                    String[] sfx = {"btl", "ftl", "vm" };
                    if (Arrays.asList(sfx).contains(FileUtilRt.getExtension(f.getName()))) {
                        ret = true;
                    }
                }
                return ret;
            });

            for (File file : files) {
                String extension = FileUtilRt.getExtension(file.getName());
                String fn = file.getName();
                switch (extension) {
                    case "btl":
                        templs.add(new Template(fn.substring(0, fn.length() - 4), "beetl", FileUtil.loadFile(file)));
                        break;
                    case "ftl":
                        templs.add(new Template(fn.substring(0, fn.length() - 4), "freemarker",  FileUtil.loadFile(file)));
                        break;
                    case "vm":
                        templs.add(new Template(fn.substring(0, fn.length() - 3), "velocity",  FileUtil.loadFile(file)));
                        break;
                }
            }
        }

        return templs;
    }

    public static List<Template> loadResTemplateList(String dirPath) throws IOException {
        List<Template> ret = new ArrayList<>();
        List<String> fileNodes = UrlUtil.getChildrenRelativePaths(Settings.class.getResource(dirPath));
        fileNodes.forEach(fn -> {
            String fPath = dirPath + "/" + fn;
            String extension = FileUtilRt.getExtension(fn);
            switch (extension) {
                case "btl":
                    ret.add(new Template(fn.substring(0, fn.length() - 4), "beetl", loadResTemplate(fPath)));
                    break;
                case "ftl":
                    ret.add(new Template(fn.substring(0, fn.length() - 4), "freemarker", loadResTemplate(fPath)));
                    break;
                case "vm":
                    ret.add(new Template(fn.substring(0, fn.length() - 3), "velocity", loadResTemplate(fPath)));
                    break;
            }
        });

        return ret;
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

    public List<String> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<String> profileList) {
        this.profileList = profileList;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public void initDefault() throws IOException {
        this.author = "theoly";
        this.profile = "default";
        String userHome = System.getProperty("user.home");
        File profileHome = new File(userHome + "/.tigen");
        if (!profileHome.exists() || !profileHome.isDirectory()) {
            // create default profile
            profileHome.delete();
            profileHome.mkdir();
            File profileDefault = new File(userHome + "/.tigen/profile/" + this.profile);
            profileDefault.mkdirs();
            templates = loadResTemplateList("/template/default");
            templates.forEach(t -> {
                String fileName = profileDefault + "/" + t.getName();
                switch (t.getEngine()) {
                    case "beetl":
                        fileName += ".btl";
                        break;
                    case "freemarker":
                        fileName += ".ftl";
                        break;
                    case "velocity":
                        fileName += ".vm";
                        break;
                }
                File templ = new File(fileName);
                try (FileWriter fileWriter = new FileWriter(templ.getAbsoluteFile());) {
                    BufferedWriter bw = new BufferedWriter(fileWriter);
                    bw.write(t.getCode());
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } else {
            // load profiles
            List<String> plist = new ArrayList<>();
            File profileRoot = new File(userHome + "/.tigen/profile");
            File[] dirs = profileRoot.listFiles(pathname -> pathname.isDirectory());
            boolean profileExist = false;
            for (File dir : dirs) {
                plist.add(dir.getName());
                if (profile.equals(dir.getName())) {
                    profileExist = true;
                }
            }

            this.profileList = plist;
            if (!profileExist) {
                this.profile = "default";
            }
            this.templates = loadProfile(userHome + "/.tigen/profile/" + this.profile);
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


}
