package wizag.com.supa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Questionnaire {

    int question_id;
    String answer;

    public Model_Questionnaire(int question_id, String answer) {
        this.question_id = question_id;
        this.answer = answer;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("question_id", question_id);
            obj.put("answer", answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}


