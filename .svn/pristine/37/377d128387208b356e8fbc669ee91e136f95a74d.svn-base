package cc.yao01.rabbit.generator.config;

import cc.yao01.rabbit.generator.codegen.freemarker.FreemarkerRabbitTemplete;
import cc.yao01.rabbit.generator.datamodel.ProjectModel;
import cc.yao01.rabbit.generator.utils.StringUtil;
import lombok.Data;

import java.sql.Date;
import java.util.Properties;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class ProjectConfig {
    private boolean generateProject;
    private boolean overrideProject;

    private String projectName;
    private String description;
    private String groupId;
    private String artifactId;
    private String version;
    private String packageName;
    private String projectLocation;
    private String author;
    private String email;
    private Properties properties = new Properties();
    private FreemarkerRabbitTemplete rabbitTemplete;

    public ProjectModel genProjectModel() {
        ProjectModel projectModel = new ProjectModel();
        projectModel.setProjectName(projectName);
        projectModel.setCamelProjectName(StringUtil.getCamelCaseString(projectName, true));
        projectModel.setDescription(description);
        projectModel.setAuthor(author);
        projectModel.setEmail(email);
        projectModel.setCreateDate(new Date(System.currentTimeMillis()));
        projectModel.setGroupId(groupId);
        projectModel.setArtifactId(artifactId);
        projectModel.setVersion(version);
        projectModel.setProjectRootPath(getProjectRootPath());
        projectModel.setPackageName(packageName);
        projectModel.setPackagePath(packageName.replaceAll("\\.", "/"));
        projectModel.setProperties(properties);
        return projectModel;
    }

    public String getProjectRootPath() {
        return this.getProjectLocation() + "/" + this.getProjectName();
    }

    public void addProperty(String name, Object value) {
        this.properties.put(name, value);
    }
}
