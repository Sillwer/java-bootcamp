package s21;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class App {
    static int threadsCount;
    static final String SOURCE_LIST_FILE = "src/main/java/s21/files_urls.txt";
    static final String DOWNLOAD_DIR_PATH = "downloads";

    public static void main(String[] args) {
        if (!setupArgs(args)) {
            System.exit(-1);
        }

        LoadManager lm = new LoadManager(threadsCount, SOURCE_LIST_FILE, DOWNLOAD_DIR_PATH);

        lm.startDownloading();
        System.out.println("> End of main thread");
    }

    private static boolean setupArgs(String[] args) {
        Options options = new Options();
        options.addOption("t", "threadsCount", true, "");
        try {
            DefaultParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            threadsCount = Integer.parseInt(cmd.getOptionValue("t", "3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
