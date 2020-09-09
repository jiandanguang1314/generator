package cc.yao01.rabbit.generator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
public class ShellRunner {
    private static final String CONFIG_FILE = "-configfile";
    private static final String OVERWRITE = "-overwrite";
    private static final String CONTEXT_IDS = "-contextids";
    private static final String TABLES = "-tables";
    private static final String VERBOSE = "-verbose";
    private static final String FORCE_JAVA_LOGGING = "-forceJavaLogging";
    private static final String HELP_1 = "-?";
    private static final String HELP_2 = "-h";

    public static void main(String[] args) {

        try {
            /*if (args.length != 1) {
                System.out.println("请指定配置文件名称！");
                System.exit(0);
            }*/
            File configurationFile = new File("D:\\work\\workspaceVip\\rabbit-generator-config\\RabbitGenConfiguration.xml");
            RabbitGenerator rabbitGenerator = new RabbitGenerator(configurationFile);
            rabbitGenerator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void usage() {
    }

    public static void writeLine(String message) {
        System.out.println(message);
    }

    public static Map<String, String> parseCommandLine(String[] args) {
//        List<String> errors = new ArrayList<>();
        Map<String, String> arguments = new HashMap<>(args.length);
//
//        for (int i = 0; i < args.length; i++) {
//            if (CONFIG_FILE.equalsIgnoreCase(args[i])) {
//                if ((i + 1) < args.length) {
//                    arguments.put(CONFIG_FILE, args[i + 1]);
//                } else {
//                    errors.add(Messages.getString("RuntimeError.19", CONFIG_FILE));
//                }
//                i++;
//            } else if (OVERWRITE.equalsIgnoreCase(args[i])) {
//                arguments.put(OVERWRITE, "Y");
//            } else if (VERBOSE.equalsIgnoreCase(args[i])) {
//                arguments.put(VERBOSE, "Y");
//            } else if (HELP_1.equalsIgnoreCase(args[i])) {
//                arguments.put(HELP_1, "Y");
//            } else if (HELP_2.equalsIgnoreCase(args[i])) {
//                // put HELP_1 in the map here too - so we only
//                // have to check for one entry in the mainline
//                arguments.put(HELP_1, "Y");
//            } else if (FORCE_JAVA_LOGGING.equalsIgnoreCase(args[i])) {
//            } else if (CONTEXT_IDS.equalsIgnoreCase(args[i])) {
//                if ((i + 1) < args.length) {
//                    arguments.put(CONTEXT_IDS, args[i + 1]);
//                } else {
//                    errors.add(Messages.getString("RuntimeError.19", CONTEXT_IDS));
//                }
//                i++;
//            } else if (TABLES.equalsIgnoreCase(args[i])) {
//                if ((i + 1) < args.length) {
//                    arguments.put(TABLES, args[i + 1]);
//                } else {
//                    errors.add(Messages.getString("RuntimeError.19", TABLES));
//                }
//                i++;
//            } else {
//                errors.add(Messages.getString("RuntimeError.20", args[i]));
//            }
//        }
//
//        if (!errors.isEmpty()) {
//            for (String error : errors) {
//                writeLine(error);
//            }
//
//            System.exit(-1);
//        }
//
        return arguments;
    }
}
