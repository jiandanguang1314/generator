package cc.yao01.rabbit.generator.codegen.freemarker;

import cc.yao01.rabbit.generator.exception.BizException;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class GenerateTemplate {
    private OutputFormat format;
    private String rootPath;





    @Before
    public void init() {
        format = new OutputFormat("\t", true);
        rootPath = "/Volumes/datadisk/IdeaProjects/yao01/rabbit-generator/core/src/main/resources/templates/ToonOcm-1.0/";
    }

    @Test
    public void testGenerateTemplate() {
        try {
            String originalPath = rootPath + "original";
            File originalFile = new File(originalPath);
            genPath(originalFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genPath(File path) {
        if (!path.isDirectory()) {
            System.out.println("路径" + path.getAbsolutePath() + "不存在，或者不是目录！");
            return;
        }
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.getName().equals(".DS_Store")) {
                continue;
            }
            if (file.isFile()) {
                genTemplate(file);
            } else if (file.isDirectory()) {
                genPath(file);
            }
        }
    }

    private void genTemplate(File file) {
        String filename = file.getName();
        String relativePath = file.getAbsolutePath().replaceFirst(rootPath, "").replaceFirst(filename, "");
        String outputFilename = filename + ".xml";
        XMLWriter writer = null;
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                char[] charArray = new char[8192];
                int len;
                len = br.read(charArray);
                while (len > 0) {
                    sb.append(charArray, 0, len);
                    len = br.read(charArray);
                }
            }
            Document doc = DocumentHelper.createDocument();
            doc.addDocType("freemarker", "", "../../../template_1_0.dtd");
            Element root = doc.addElement("freemarker");
            root.addAttribute("name", outputFilename);
            root.addAttribute("filePath", "${projectRootPath}/" + relativePath);
            root.addAttribute("fileName", filename);
            root.addAttribute("fileType", "project");
            CDATA data = DocumentHelper.createCDATA(sb.toString());
            root.add(data);
            writer = new XMLWriter(new FileWriter(rootPath + "project/" + outputFilename), format);
            writer.write(doc);
        } catch (Exception e) {
            throw new BizException("处理文件" + filename + "出错！", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
