package wizag.com.supa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Questionnaire {

    int question_id, answer;

    public Model_Questionnaire(int question_id, int answer) {
        this.question_id = question_id;
        this.answer = answer;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public int getAnswer() {
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


