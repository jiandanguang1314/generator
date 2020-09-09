/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cc.yao01.rabbit.generator.config.xmlparser;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
public class TemplateParserEntityResolver implements EntityResolver {

	/**
	 *  
	 */
	public TemplateParserEntityResolver() {
		super();
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("template_1_0.dtd");
		InputSource ins = new InputSource(is);

		return ins;
	}
}
