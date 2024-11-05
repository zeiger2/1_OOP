import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class WikipediaApi {

    public List<WikipediaArticle> searchArticles(String topic) throws IOException {
        String baseUrl = "https://ru.wikipedia.org/w/api.php";
        String searchUrl = baseUrl + "?action=query&list=search&utf8=&format=json&srsearch="
                + URLEncoder.encode(topic, StandardCharsets.UTF_8);

        Document doc = Jsoup.connect(searchUrl).ignoreContentType(true).get();
        String json = doc.body().text();

        Gson gson = new Gson();
        JsonObject root = gson.fromJson(json, JsonObject.class);
        JsonArray searchResults = root.getAsJsonObject("query").getAsJsonArray("search");

        List<WikipediaArticle> articles = new ArrayList<>();
        for (int i = 0; i < searchResults.size(); i++) {
            JsonObject articleJson = searchResults.get(i).getAsJsonObject();
            String title = articleJson.get("title").getAsString();
            String pageId = articleJson.get("pageid").getAsString();
            articles.add(new WikipediaArticle(title, pageId));
        }
        return articles;
    }
}

class WikipediaArticle {
    private final String title;
    private final String pageId;

    public WikipediaArticle(String title, String pageId) {
        this.title = title;
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getPageId() {
        return pageId;
    }

    public URI getUri() throws URISyntaxException {
        return new URI("https://ru.wikipedia.org/w/index.php?curid=" + pageId);
    }
}

class WikipediaSearcher {
    private final WikipediaApi api;
    private final Scanner scanner;

    public WikipediaSearcher() {
        this.api = new WikipediaApi();
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException, URISyntaxException {
        System.out.println("Введите тему для поиска на Википедии:");
        String topic = scanner.nextLine();

        List<WikipediaArticle> articles = api.searchArticles(topic);

        if (articles.isEmpty()) {
            System.out.println("Статьи не найдено");
        } else {
            displayArticles(articles);
            int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;
            openArticle(articles.get(selectedIndex));
        }
    }

    private void displayArticles(List<WikipediaArticle> articles) {
        System.out.println("Выберите интересующую вас статью:");
        for (int i = 0; i < articles.size(); i++) {
            System.out.println((i + 1) + ") " + articles.get(i).getTitle());
        }
    }

    private void openArticle(WikipediaArticle article) throws IOException, URISyntaxException {
        URI pageUri = article.getUri();
        java.awt.Desktop.getDesktop().browse(pageUri);
        System.out.println("Открыта страница на Википедии: " + pageUri);
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            new WikipediaSearcher().start();
        } catch (IOException | URISyntaxException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
