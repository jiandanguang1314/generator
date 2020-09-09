package cc.yao01.rabbit.generator.output;

import lombok.Data;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class RabbitGeneratedFile {
	private String name;
	private boolean genetated;
	private String filePath;
	private String fileName;
	private String content;
}
