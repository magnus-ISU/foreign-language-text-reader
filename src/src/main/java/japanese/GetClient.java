package japanese;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class GetClient {
    private final OkHttpClient client = new OkHttpClient();
    private static final String JISHO_URL = "https://jisho.org/api/v1/search/words?keyword=";
    private String word;

    public GetClient(String word) {
//        this.word = word;
//        searchWord(word);
    }

    public String searchWord(String word) {
        Request request = new Request.Builder().url(JISHO_URL + word).build();
        try (Response response = client.newCall(request).execute()) {
            //ResultExtractor re = new ResultExtractor();
           //re.extractDefinitions(response.body().string());

            return response.body().string();
        } catch (IOException e) {
            System.out.println("An error has occurred while contacting Jisho's website: " + e.getMessage());
        }
        return "Something went wrong.";
    }
}
