import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;


public class HtmlTextExtractor {
    public static String extractText(File htmlFile) throws IOException {
        return Jsoup.parse(htmlFile, "UTF-8").text();
    }
}
