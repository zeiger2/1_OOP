import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WikipediaParser_OLD {

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

        int len=0;
        JsonArray searchResults = root.getAsJsonObject("query").getAsJsonArray("search");
        len=searchResults.size();
        String[] key = new String[len];
        String[] value = new String[len];
        try {
            for (int i = 0; i < searchResults.size(); i++) {
                String pageTitle = searchResults.get(i).getAsJsonObject().get("title").getAsString();
                String pageId = searchResults.get(i).getAsJsonObject().get("pageid").getAsString();
                key[i]=pageTitle;
                value[i]=pageId;
            }
        } catch (Exception e){
            System.out.println("Ошибка: Больше статей нет");
        }

        if(len==0){
            System.out.println("Статьи не найдено");
        }
        else {
            System.out.println("Выберите интересующую вас статью:");
            for (int i = 0; i < len; i++) {
                System.out.println((i + 1) + ") " + key[i]);
            }
            int topic2 = Integer.parseInt(scanner.nextLine());

            URI pageUri = new URI("https://ru.wikipedia.org/w/index.php?curid=" + value[topic2 - 1]);
            java.awt.Desktop.getDesktop().browse(pageUri);

            System.out.println("Открыта страница на Википедии: " + pageUri);
        }
    }
}