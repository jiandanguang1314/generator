package cc.yao01.rabbit.generator.config.xmlparser;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
public class ConfigParserEntityResolver implements EntityResolver {

    public ConfigParserEntityResolver() {
        super();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("rabbit-generator-config_1_0.dtd");
        return new InputSource(is);
    }
}
