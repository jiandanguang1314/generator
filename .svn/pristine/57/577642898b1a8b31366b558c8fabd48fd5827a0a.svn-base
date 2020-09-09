package cc.yao01.rabbit.generator.codegen.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import cc.yao01.rabbit.generator.exception.BizException;

@RunWith(JUnit4.class)
public class TestTemplateProcess {

	private SAXReader reader;
	private OutputFormat format;

	@Before
	public void init() {
		reader = new SAXReader();
		format = new OutputFormat("\t", true);
	}

	@Test
	public void testProcessTemplate() {
		try {
			String rootPath = "/Volumes/datadisk/IdeaProjects/yao01/rabbit-generator/core/src/main/resources/templates/web-1.1/";

			String[] paths = { "project", "table" };
			for (String path : paths) {
				File directory = new File(rootPath + path);
				File[] files = directory.listFiles();
				for (File file : files) {
					if (file.getName().toLowerCase().endsWith(".xml")) {
						processFile(file);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processFile(File file) throws DocumentException, IOException, SAXException {
		String filename = file.getName();
		XMLWriter writer = null;
		try {
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			Attribute modelType = root.attribute("modelType");
			if (modelType != null) {
				root.remove(modelType);
				root.addAttribute("fileType", "project");
			}
			// root.addAttribute("name", filename);
			writer = new XMLWriter(new FileWriter(file), format);
			writer.write(doc);
		} catch (Exception e) {
			throw new BizException("处理文件" + filename + "出错！", e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}
