package cc.yao01.rabbit.generator.codegen.freemarker;

import cc.yao01.rabbit.generator.datamodel.IDataModel;
import cc.yao01.rabbit.generator.datamodel.TableModel;
import cc.yao01.rabbit.generator.exception.BizException;
import cc.yao01.rabbit.generator.output.RabbitGeneratedFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.AllArgsConstructor;

import java.io.StringWriter;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@AllArgsConstructor
public class FreeMarkerGenerator {

    private static final String FREE_MARKER_VERSION = "2.3.20";
    private static Configuration freeMarkerConfiguration;

    static {
        freeMarkerConfiguration = new Configuration(new Version(FREE_MARKER_VERSION));
    }

    private FreemarkerTemplateFile templateFile;
    private IDataModel dataModel;

    public RabbitGeneratedFile generateFile() {
        RabbitGeneratedFile result = new RabbitGeneratedFile();
        String name = templateFile.getName();
        result.setName(name);
        if (dataModel instanceof TableModel) {
            TableModel tableModel = (TableModel) dataModel;
            if (!tableModel.isGenerate(templateFile.getFileType())) {
                return result;
            }
        }
        result.setGenetated(true);

        result.setFilePath(processFreemarker(name, templateFile.getFilePath()));
        result.setFileName(processFreemarker(name, templateFile.getFileName()));
        result.setContent(processFreemarker(name, templateFile.getContent()));

        return result;
    }

    private String processFreemarker(String name, String sourceCode) {
        try (StringWriter sw = new StringWriter()) {
            Template template = new Template(name, sourceCode, freeMarkerConfiguration);
            template.process(dataModel, sw);
            return sw.toString();
        } catch (Exception e) {
            throw new BizException("转换freemarker模板文件时出错：name=" + name + "，sourceCode=" + sourceCode, e);
        }
    }

}
