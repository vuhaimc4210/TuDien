package com.example.tudien;

import java.io.*;
import java.util.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class DictionaryManagement extends Dictionary {
    public static void insertFromCommandline() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập số lượng từ cần thêm:  ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Nhập từ tiếng Anh: ");
            String engWord = sc.nextLine();
            System.out.println("Nhập nghĩa tiếng Việt: ");
            String vieWord = sc.nextLine();
            Word word = new Word(engWord, vieWord);
            Dictionary.words.add(word);
        }

        Collections.sort(Dictionary.words);
        return;
    }

    private static final String url = "src/main/java/com/example/tudien/dictionaries.txt";
    private static final String url1 = "src/main/java/com/example/tudien/history.txt";

    public static void insertFromFile() {

        try {
            File file = new File(url);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = "";
            while ((line = reader.readLine()) != null) {
                int tmp = line.indexOf("\t");
                String Eng = line.substring(0, tmp);
                String Vie = line.substring(tmp + 1, line.length());
                Word newWord = new Word(Eng, Vie);
                Dictionary.words.add(newWord);
            }

            Collections.sort(Dictionary.words);
            reader.close();
        } catch (Exception e) {
        }
        return;
    }

    public static void phatAm(String Eng) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice;//Creating object of Voice class
        voice = VoiceManager.getInstance().getVoice("kevin");//Getting voice
        if (voice != null) {
            voice.allocate();//Allocating Voice
        }
        try {
            voice.setRate(120);//Setting the rate of the voice
            voice.setPitch(180);//Setting the Pitch of the voice
            voice.setVolume(5);//Setting the volume of the voice
            voice.speak(Eng);//Calling speak() method

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String dictionaryLookup() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập từ cần tìm:  ");
        String searchWord = sc.nextLine();

        //Collections.sort(Dictionary.words);
        int index = Collections.binarySearch(Dictionary.words, new Word(searchWord, null));
        if (index >= 0) {
            return Dictionary.words.get(index).getWord_explain();
        } else {
            return "Not Found!";
        }
    }

    public static void addWord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập từ tiếng Anh muốn thêm: ");
        String engWord = sc.nextLine();

        int index = Collections.binarySearch(Dictionary.words, new Word(engWord, null));
        if (index >= 0) {
            System.out.println("Từ này đã tồn tại!");
            return;
        } else {
            System.out.println("Nhập nghĩa tiếng Việt: ");
            String vieWord = sc.nextLine();
            Word word = new Word(engWord, vieWord);
            Dictionary.words.add(word);
            Collections.sort(Dictionary.words);
            return;
        }
    }

    public static void addWord(Word word) {
        int index = Collections.binarySearch(Dictionary.words, word);
        if (index >= 0) {
            return;
        } else {
            Dictionary.words.add(word);
            Collections.sort(Dictionary.words);
            return;
        }
    }

    public static void modifiWord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập từ muốn sửa: ");
        String engWord = sc.nextLine();
        //Collections.sort(Dictionary.words);

        int index = Collections.binarySearch(Dictionary.words, new Word(engWord, null));
        if (index >= 0) {
            System.out.println("Sửa nghĩa thành: ");
            String vieWord = sc.nextLine();
            Dictionary.words.get(index).setWord_explain(vieWord);
        } else {
            System.out.println("Not Found");
            return;
        }
    }

    public static void modifiWord(Word word) {

        int index = Collections.binarySearch(Dictionary.words, word);
        if (index >= 0) {
            Dictionary.words.get(index).setWord_explain(word.getWord_explain());
        } else {
            return;
        }
    }

    public static void deleteWord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập từ muốn xoá: ");
        String engWord = sc.nextLine();
        //Collections.sort(Dictionary.words);
        int index = Collections.binarySearch(Dictionary.words, new Word(engWord, null));
        if (index >= 0) {
            Dictionary.words.remove(Dictionary.words.get(index));
            System.out.println("Đã xoá!");
        } else {
            System.out.println("Not Found");
            return;
        }
    }

    public static void deleteWord(Word word) {
        int index = Collections.binarySearch(Dictionary.words, word);
        if (index >= 0) {
            Dictionary.words.remove(Dictionary.words.get(index));
        } else {
            return;
        }
    }

    public static void dictionarySearcher() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Search Word: ");
        String searchWord = sc.nextLine();
        for (Word i : Dictionary.words) {
            if (i.getWord_target().indexOf(searchWord) == 0) {
                System.out.println(i.getWord_target());
            }
        }
    }

    public static void dictionaryExportToFile() {
        try {
            File file = new File(url);
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            for (int i = 0; i < Dictionary.words.size(); i++) {
                outputStreamWriter.write(Dictionary.words.get(i).getWord_target() + "\t" + Dictionary.words.get(i).getWord_explain() + "\n");
            }
            outputStreamWriter.flush();
        } catch (Exception e) {
        }
    }

    public static void insertFromFileHistory() {

        try {
            File file = new File(url1);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = "";
            while ((line = reader.readLine()) != null) {
//                int tmp = line.indexOf("\t");
//                String Eng = line.substring(0, tmp);
//                String Vie = line.substring(tmp + 1, line.length());
//                Word newWord = new Word(Eng, Vie);
//                int tmp = line.indexOf("\n");
//                String newWord = line.substring(0, tmp);
//                String
                Dictionary.historyWords.add(line);
            }

//            Collections.sort(Dictionary.historyWords);
            reader.close();
        } catch (Exception e) {
        }
        return;
    }

    public static void dictionaryExportToFileHistory() {
        try {
            File file = new File(url1);
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            for (int i = 0; i < Dictionary.historyWords.size(); i++) {
                outputStreamWriter.write(Dictionary.historyWords.get(i) + "\n");
            }
            outputStreamWriter.flush();
        } catch (Exception e) {
        }
    }
}
