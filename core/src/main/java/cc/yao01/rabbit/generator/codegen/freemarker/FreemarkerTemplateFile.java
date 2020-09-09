package cc.yao01.rabbit.generator.codegen.freemarker;

import lombok.Data;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class FreemarkerTemplateFile {
	private String name;
	private String filePath;
	private String fileName;
	private String content;
	private String fileType;
}
