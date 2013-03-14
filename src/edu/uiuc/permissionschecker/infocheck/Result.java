package edu.uiuc.permissionschecker.infocheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Result implements Serializable {	
	private static final long serialVersionUID = -4454012664157108338L;
	public static final String NORMAL = "normal";
	public static final String SNEAKY = "sneaky";
	public static final String HACK = "hacky";
	
	public static final int SUCCESS = 2;
	public static final int FAILURE = 0;
	public static final int SEMI = 1;
	public static final int NOT_APPLICABLE = -9999;
	
	public String name;
	public int success = NOT_APPLICABLE;
	public String data = "";
	public List<Attempt> attempts;

	
	public static class Attempt implements Serializable {
		private static final long serialVersionUID = -5943088951988482328L;
		public String kind;
		public int success;
		public String error;
		public String data;
		public Attempt(String kind, int success, String error, String data) {
			this.kind = kind; this.success = success; this.error = error; this.data = data;
		}
		public JSONObject toJSON() throws JSONException {
			JSONObject object = new JSONObject();
			object.put("kind", kind);
			object.put("success", success);
			object.put("data", data);
			object.put("error", error);
			return object;
		}
		public static Attempt fromJSON(JSONObject object) throws JSONException {
			String kind = object.getString("kind");
			int success = object.getInt("success");
			String data = object.getString("data");
			String error = object.getString("error");
			return new Attempt(kind, success, error, data);
		}
		
	}
	
	public Result() {
		this.attempts = new ArrayList<Attempt>();
	}
	public Result(String name) {
		this();
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addAttempt(Attempt attempt) {
		this.attempts.add(attempt);
		this.success = this.success == SUCCESS ? this.success : attempt.success;
		if (attempt.success == SUCCESS) this.data = attempt.data;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(name);
		b.append(": ");
		if (success == SUCCESS) {
			b.append("Success");
			b.append("\t data:");
			b.append(data);
		} else {
			b.append("Code: ");
			b.append(success);
			b.append("\t last error:");
			if (attempts.size() > 0) {
				b.append(attempts.get(attempts.size()-1).error);
			}
		}
		b.append("\t\t attempts:");
		b.append(attempts.size());
		return b.toString();
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject object = new JSONObject();

		object.put("name", name);
		object.put("success", success);
		object.put("data", data);
		JSONArray array = new JSONArray();
		for (Attempt a : attempts) {
			array.put(a.toJSON());
		}
		object.put("attempts", array);
		return object;
	}
	public void fromJSON(JSONObject object) throws JSONException {
		name = object.getString("name");
		success = object.getInt("success");
		data = object.getString("data");
		JSONArray array = object.getJSONArray("attempts");
		for (int i=0; i<array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			attempts.add(Attempt.fromJSON(obj));
		}
	}

}
