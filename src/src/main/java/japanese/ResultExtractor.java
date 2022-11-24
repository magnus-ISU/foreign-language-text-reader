package japanese;


import com.jayway.jsonpath.JsonPath;

import java.util.List;

public class ResultExtractor {
    public String extractDefinitions(String response) {
        StringBuilder definitionsProperlyFormatted = new StringBuilder();
        List<String> definitionsList = JsonPath.read(response, "$..data[0].senses[0].english_definitions[0:3]");
        for (String def: definitionsList){
            definitionsProperlyFormatted.append(def).append("; ");
        }

        return definitionsProperlyFormatted.toString();
    }

    public String extractReading(String response) {
        List<String> reading = JsonPath.read(response, "$..data");
        return reading.toString();
    }

    public List<String> extractTerm(String response){
        return JsonPath.read(response, "$..data[0].slug");
    }
}
