package cc.yao01.rabbit.generator.config;
import lombok.Data;

import java.util.Properties;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class JdbcConnectionConfig {

    private String driverClass;
    private String connectionURL;
    private String userId;
    private String password;
    private Properties properties = new Properties();

    public void setProperty(String key, String value) {
        this.properties.setProperty(key, value);
    }

}
