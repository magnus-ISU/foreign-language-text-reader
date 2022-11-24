import japanese.ResultExtractor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultExtractorTest {
    String response = "{\"meta\":{\"status\":200},\"data\":[{\"slug\":\"決める\",\"is_common\":true,\"tags\":[\"wanikani8\"],\"jlpt\":[\"jlpt-n4\"],\"japanese\":[{\"word\":\"決める\",\"reading\":\"きめる\"},{\"word\":\"極める\",\"reading\":\"きめる\"}],\"senses\":[{\"english_definitions\":[\"to decide\",\"to choose\",\"to determine\",\"to make up one's mind\",\"to resolve\",\"to set one's heart on\",\"to settle\",\"to arrange\",\"to set\",\"to appoint\",\"to fix\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to clinch (a victory)\",\"to decide (the outcome of a match)\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to persist in doing\",\"to go through with\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to always do\",\"to have made a habit of\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[\"as 決めている\"]},{\"english_definitions\":[\"to take for granted\",\"to assume\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to dress up\",\"to dress to kill\",\"to dress to the nines\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to carry out successfully (a move in sports, a pose in dance, etc.)\",\"to succeed in doing\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to immobilize with a double-arm lock (in sumo, judo, etc.)\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[\"Martial arts\",\"Sumo\"],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]},{\"english_definitions\":[\"to eat or drink something\",\"to take illegal drugs\"],\"parts_of_speech\":[\"Ichidan verb\",\"Transitive verb\"],\"links\":[],\"tags\":[],\"restrictions\":[],\"see_also\":[],\"antonyms\":[],\"source\":[],\"info\":[]}],\"attribution\":{\"jmdict\":true,\"jmnedict\":false,\"dbpedia\":false}}]}";
    ResultExtractor re = new ResultExtractor();
    @Test
    public void canExtractTranslation() {
//        assertEquals("to decide", re.extractDefinitions(response).get(0));
//        assertEquals("to choose", re.extractDefinitions(response).get(1));
//        assertEquals("to determine", re.extractDefinitions(response).get(2));
        System.out.println("Definitions extracted\n" + re.extractDefinitions("決める"));
    }

    @Test
    public void canExtractTerm(){
        assertEquals("決める", re.extractTerm(response).get(0));
        System.out.println("The term extracted was " + re.extractTerm(response));
    }

    @Test
    public void canExtractReading(){
        String word = "決める";
        assertEquals("きめる", re.extractReading(word));
    }
}
