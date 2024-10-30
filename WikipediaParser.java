import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WikipediaParser {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите тему для поиска на Википедии:");
        String topic = scanner.nextLine();

        String searchUrl = "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch="
                + URLEncoder.encode(topic, StandardCharsets.UTF_8);

        Document doc = Jsoup.connect(searchUrl).ignoreContentType(true).get();
        String json = doc.body().text();

        Gson gson = new Gson();
        JsonObject root = gson.fromJson(json, JsonObject.class);
//        JsonObject search = root.getAsJsonObject("query").getAsJsonObject("search");
        JsonArray search = root.getAsJsonArray("query").getAsJsonArray();
        
        String pageTitle = search.getAsJsonObject().get("title").getAsString();
        String pageId = search.getAsJsonObject().get("pageid").getAsString();
        System.out.println(pageTitle);
        System.out.println(pageId);

        URI pageUri = new URI("https://ru.wikipedia.org/w/index.php?curid=" + pageId);
        //java.awt.Desktop.getDesktop().browse(pageUri);

        System.out.println("Открыта страница на Википедии: " + pageUri);
    }
}
