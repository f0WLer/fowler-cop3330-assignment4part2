package ucf.assignments.data;

import org.json.JSONArray;
import org.json.JSONObject;
import ucf.assignments.App;
import ucf.assignments.todo.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class FileHandler {
		public static void writeList(List list, String filePath) throws IOException {
				BufferedWriter w = new BufferedWriter(new FileWriter(filePath));
				w.write(list.getSaveData());
				w.close();
		}

		public static void writeLists(ArrayList<List> lists, String filePath) throws IOException {
				StringBuilder data = new StringBuilder("{[");
				for (int i = 0; i < lists.size(); i++) {
						List l = lists.get(i);
						data.append(l.getSaveData());
						if (i != lists.size()-1)
								data.append(",");
				}
				BufferedWriter w = new BufferedWriter(new FileWriter(filePath));
				w.write(data.toString());
				w.close();
		}

		public static ArrayList<List> readFile(File file) throws FileNotFoundException {
				String data = new Scanner(file).nextLine();
				ArrayList<List> t = new ArrayList<>();
				JSONArray lists = new JSONArray(new JSONObject(data).getJSONArray("lists"));

				return parseLists(lists);
		}

		private static ArrayList<List> parseLists(JSONArray data) {
				ArrayList<List> newLists = new ArrayList<>();
				for (int i = 0; i < data.length(); i++)
						newLists.add(new List(data.getJSONObject(i)));
				return newLists;
		}


		/*
		public static ArrayList<List> readLists(String data) {
				App.debugLog("Parsing lists from file...");
				Scanner s = new Scanner(data);
				ArrayList<List> t = new ArrayList<>();
				App.debugLog(data);
				String[] lines = (s.reset().skip("\\{\\[\\{").nextLine()).split("\"title\"");
				System.out.println(Arrays.toString(lines));
				App.debugLog("A");
				if (lines.length == 2) {
						App.debugLog("B");
						// One list
						StringBuilder sb = new StringBuilder("{\"title\"");
						t.add(new List(sb.append(lines[1]).delete(sb.length()-4, sb.length()).toString()));
						App.debugLog("B");
				} else {
						App.debugLog("C");
						// Many Lists
						for (String l : lines) {
								if (l.equals("[")) continue;
								System.out.println(l);
								StringBuilder sb = new StringBuilder("{\"title\"");
								if (l.charAt(l.length()-1) == ',') {
										// Non-Last Case
										sb.append(l).deleteCharAt(sb.length()-1);
								} else
										// Last Case
										sb.append(l).delete(sb.length()-2, sb.length()-1);
								t.add(new List(sb.toString()));
						}
						App.debugLog("C");
				}

				String[] lines = s.reset().skip("\\{\\[").nextLine().split("\"title\"");
				for (String l : lines) {
						StringBuilder sb = new StringBuilder();
						if (l.charAt(0) != '{')
								sb.append("{");
						sb.append(l);
						if (l.charAt(l.length()-1) != '}')
								sb.append("}");
						t.add(new List(sb.toString()));
				}

				return t;
		}
		*/

}