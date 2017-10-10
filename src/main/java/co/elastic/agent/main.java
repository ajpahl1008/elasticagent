package co.elastic.agent;

import java.lang.System;
import java.util.ArrayList;

/**
 * Created By: aj
 */
public class main {
    public static ArrayList<String> sample = new ArrayList<>();
    public static void main(String[] args) {

        sample.add("java");
        sample.add("com.pahlsoft");
        sample.add("co.elastic");

        if (shouldSkip("com.pahlsoft.util")) {
            System.out.println("Yes");
        }

        if (shouldSkip("java.sun.com")) {
            System.out.println("Yes");
        }

        if (matchesList("com.pahlsoft.blah.blah")) {
            System.out.println("Yes");
        }

    }

    private static boolean matchesList(String packageName) {
        for (String str: sample) {
            System.out.println("Matching: " + str + " to: " + packageName);
            if (packageName.contains(str)) return true;
        }

        return false;
    }

    private static boolean shouldSkip(String packageName) {
        return sample.parallelStream().anyMatch(p -> packageName.contains(p));

        //return sample.stream().filter(p->packageName.contains(p)).findAny().isPresent();
    }
}
