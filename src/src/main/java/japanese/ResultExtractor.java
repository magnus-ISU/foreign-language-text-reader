package japanese;

import com.jayway.jsonpath.JsonPath;

import java.util.List;

public class ResultExtractor {
    private GetClient getClient = new GetClient();
    public String extractDefinitions(String word) {
        String response = getClient.searchWord(word);
        StringBuilder definitionsProperlyFormatted = new StringBuilder();
        List<String> definitionsList = JsonPath.read(response, "$..data[0].senses[0].english_definitions[0:3]");
        for (String def: definitionsList){
            definitionsProperlyFormatted.append(def).append("; ");
        }

        return definitionsProperlyFormatted.toString();
    }

    public String extractReading(String word) {
        String response = getClient.searchWord(word);
        List<String> reading = JsonPath.read(response, "$..data[0].japanese[0].reading");
        return reading.get(0);
    }

    //Todo: Use this for something besides tests
    public List<String> extractTerm(String response){
        return JsonPath.read(response, "$..data[0].slug");
    }
}
