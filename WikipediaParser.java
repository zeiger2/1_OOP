
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WikipediaParser {

    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите тему для поиска на Википедии:");
        String topic = scanner.nextLine();

        String searchUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles="
                + URLEncoder.encode(topic, StandardCharsets.UTF_8);

    }
}
