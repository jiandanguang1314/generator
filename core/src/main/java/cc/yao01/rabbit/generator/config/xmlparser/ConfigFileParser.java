package cc.yao01.rabbit.generator.config.xmlparser;

import cc.yao01.rabbit.generator.codegen.freemarker.FreeMarkerTemplateLoader;
import cc.yao01.rabbit.generator.codegen.freemarker.FreemarkerRabbitTemplete;
import cc.yao01.rabbit.generator.config.*;
import cc.yao01.rabbit.generator.config.GenerateFlags;
import cc.yao01.rabbit.generator.exception.BizException;
import cc.yao01.rabbit.generator.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@SuppressWarnings("unchecked")
public class ConfigFileParser {

    public RabbitConfig parseConfiguration(File inputFile) {
        Document doc;
        RabbitConfig config;
        try {
            SAXReader reader = new SAXReader();
            reader.setValidation(true);
            reader.setEntityResolver(new ConfigParserEntityResolver());
            doc = reader.read(inputFile);
            Element rootElement = doc.getRootElement();
            config = parseConfiguration(rootElement);
        } catch (Exception e) {
            throw new BizException("解析配置文件" + inputFile.getAbsolutePath() + "时出现错误：", e);
        }
        return config;
    }

    private RabbitConfig parseConfiguration(Element rootElement) {
        RabbitConfig configuration = new RabbitConfig();
        parseProject(configuration, rootElement.element("project"));
        parseGenerateBizModel(configuration, rootElement.element("bizModels"), configuration.getProjectConfig());

        return configuration;
    }

    private void parseProject(RabbitConfig rabbitConfig, Element element) {
        ProjectConfig projectConfig = new ProjectConfig();
        rabbitConfig.setProjectConfig(projectConfig);

        projectConfig.setProjectName(element.elementText("projectName"));
        projectConfig.setDescription(element.elementText("description"));
        projectConfig.setGroupId(element.elementText("groupId"));
        projectConfig.setArtifactId(element.elementText("artifactId"));
        projectConfig.setVersion(element.elementText("version"));
        projectConfig.setPackageName(element.elementText("packageName"));
        projectConfig.setProjectLocation(element.elementText("projectLocation"));
        projectConfig.setAuthor(element.elementText("author"));
        projectConfig.setEmail(element.elementText("email"));

        String templatePath = element.elementText("templatePath");
        FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader();
        FreemarkerRabbitTemplete freemarkerTemplete = templateLoader.load(templatePath);
        projectConfig.setRabbitTemplete(freemarkerTemplete);

        projectConfig.setOverrideProject(Boolean.valueOf(element.attributeValue("override")));
        projectConfig.setGenerateProject(Boolean.valueOf(element.attributeValue("generate")));

        Element propertiesElement = element.element("properties");
        if (propertiesElement != null) {
            List<Element> propertyList = propertiesElement.elements("property");
            for (Element propertyElement : propertyList) {
                projectConfig.addProperty(propertyElement.attributeValue("name"),
                        propertyElement.attributeValue("value"));
            }
        }
    }

    private void parseGenerateBizModel(RabbitConfig rabbitConfig, Element element,
                                       ProjectConfig projectConfig) {

        BizModelsConfig bizModelsConfig = new BizModelsConfig();
        rabbitConfig.setBizModelsConfig(bizModelsConfig);

        bizModelsConfig.setGenerateTables(Boolean.valueOf(element.attributeValue("generate")));

        Element classPathEntryElement = element.element("classPathEntry");
        if (classPathEntryElement != null) {
            bizModelsConfig.setClassPathEntry(classPathEntryElement.attributeValue("location"));
        }
        Element jdbcConnectionElement = element.element("jdbcConnection");
        if (jdbcConnectionElement != null) {
            parseJdbcConnection(bizModelsConfig, jdbcConnectionElement);
        }

        Element tableDefaultElement = element.element("tableDefault");
        parseTableGenerateFlag(tableDefaultElement, bizModelsConfig.getDefaultGenerateFlags());

        List<Element> tableList = element.elements("table");
        parseTableConfig(bizModelsConfig, tableList, projectConfig, null);

        List<Element> moduleList = element.elements("module");
        for (Element moduleElement : moduleList) {
            String moduleName = moduleElement.attributeValue("moduleName");
            tableList = moduleElement.elements("table");
            parseTableConfig(bizModelsConfig, tableList, projectConfig, moduleName);
        }
    }

    private void parseTableConfig(BizModelsConfig bizModelsConfig, List<Element> tableList,
                                  ProjectConfig projectConfig, String moduleName) {
        String projectPackageName = projectConfig.getPackageName();
        for (Element tableElement : tableList) {
            TableConfig tableConfig = new TableConfig();
            String tableName = tableElement.attributeValue("tableName");
            tableConfig.setTableName(tableName);

            String domainClassName = tableElement.attributeValue("domainClassName");
            if (StringUtils.isEmpty(domainClassName)) {
                domainClassName = StringUtil.getCamelCaseString(tableName, true);
            }
            tableConfig.setDomainClassName(domainClassName);

            String lModuleName = tableElement.attributeValue("moduleName");
            if (lModuleName == null) {
                lModuleName = moduleName;
            }
            tableConfig.setModuleName(lModuleName);
            String packageName = projectPackageName;
            if (!StringUtils.isEmpty(lModuleName)) {
                packageName += "." + lModuleName;
            }
            tableConfig.setPackageName(packageName);

            tableConfig.getGenerateFlags().putAll(bizModelsConfig.getDefaultGenerateFlags());
            parseTableGenerateFlag(tableElement, tableConfig.getGenerateFlags());

            bizModelsConfig.addTables(tableConfig);
        }
    }

    private void parseTableGenerateFlag(Element tableElement, GenerateFlags generateFlags) {
        List<Element> elements;
        elements = tableElement.elements("methodType");
        for (Element element : elements) {
            String type = element.attributeValue("type");
            Boolean generateFlag = Boolean.valueOf(element.attributeValue("generate"));
            generateFlags.put(type, generateFlag);
            if ("api".equals(type)) {
                generateFlags.put("apiImpl", generateFlag && Boolean.valueOf(element.attributeValue("includeImpl")));
            } else if ("service".equals(type)) {
                generateFlags.put("serviceImpl",
                        generateFlag && Boolean.valueOf(element.attributeValue("includeImpl")));
            }
        }

        elements = tableElement.elements("method");
        for (Element element : elements) {
            generateFlags.put(element.attributeValue("type"), Boolean.valueOf(element.attributeValue("generate")));
        }
        elements = tableElement.elements("modelType");
        for (Element element : elements) {
            generateFlags.put(element.attributeValue("type"), Boolean.valueOf(element.attributeValue("generate")));
        }
    }

    private void parseJdbcConnection(BizModelsConfig bizModelsConfig, Element element) {
        JdbcConnectionConfig jdbcConnectionConfig = new JdbcConnectionConfig();
        bizModelsConfig.setJdbcConnectionConfig(jdbcConnectionConfig);

        String driverClass = element.attributeValue("driverClass");
        String connectionURL = element.attributeValue("connectionURL");
        String userId = element.attributeValue("userId");
        String password = element.attributeValue("password");

        jdbcConnectionConfig.setDriverClass(driverClass);
        jdbcConnectionConfig.setConnectionURL(connectionURL);
        jdbcConnectionConfig.setUserId(userId);
        jdbcConnectionConfig.setPassword(password);

        List<Element> list = element.elements("property");
        for (Element property : list) {
            String name = property.attributeValue("name");
            String value = property.attributeValue("value");
            jdbcConnectionConfig.setProperty(name, value);
        }
    }

}
