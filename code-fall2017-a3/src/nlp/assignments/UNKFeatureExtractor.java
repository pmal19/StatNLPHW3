package nlp.assignments;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import nlp.classify.*;
import nlp.util.CommandLineUtils;
import nlp.util.Counter;

public class UNKFeatureExtractor implements FeatureExtractor<String, String> {

    public Counter<String> extractFeatures(String input_s) {

        Counter<String> features = new Counter<String>();
        String[] ar = input_s.split("_");
        String pre;
        pre="";
        String type="none";
        String ss, s0, s1;
        ss=ar[2];
        s0=ar[1];
        s1=ar[0];
        if(ss.contains("-")) {
            type+="dash";
        }
        if(ss.matches(".*\\d+.*")) {
            type+="digits";
        }
        StringBuilder input1 = new StringBuilder();
        input1.append(ss);
        String capital="no";
        if(!ss.equals(ss.toLowerCase())) {
            capital="yes";
        }
        for(int suf=1 ;suf<=4;suf++) {
            features.incrementCount(capital+pre+"Last"+String.valueOf(suf)+String.format("%."+ suf +"s", input1.reverse()),1.0);
            features.incrementCount(pre+"Last"+String.valueOf(suf)+String.format("%."+ suf +"s", input1.reverse()),1.0);
        }
        for(int pref=1 ;pref<=4;pref++) {
            features.incrementCount(capital+pre+"Last"+String.valueOf(pref)+String.format("%."+ pref +"s", input1),1.0);
            features.incrementCount(pre+"Last"+String.valueOf(pref)+String.format("%."+ pref +"s", input1),1.0);
        }
        StringBuilder prev = new StringBuilder();
        prev.append(s0);
        StringBuilder prevprev = new StringBuilder();
        prevprev.append(s1);
        for(int suf=1 ;suf<=3;suf++) {
            features.incrementCount("PREV_sf_"+String.valueOf(suf)+String.format("%."+ suf +"s", prev.reverse()),1.0);
            features.incrementCount("PREV_sf_sf_"+String.valueOf(suf)+String.format("%."+ suf +"s", prevprev.reverse()),1.0);
        }
        features.incrementCount(type+"UNIL-"+ss.toLowerCase(), 1.0);
        features.incrementCount("BIGRAM-"+ss+"_"+s0,1.0);
        features.incrementCount("TRIGRAM-"+input_s,1.0);
        features.incrementCount("UNI-"+ss, 1.0);

        //features.incrementCount("WITHOUTASCII-UNI-"+ss.replaceAll("[^A-Za-z0-9]", ""), 1.0);
        return features;
    }

        //////////// ProperName ////////////
        public Counter<String> extractFeaturesProperName(String name) {
            char[] characters = name.toCharArray();
            Counter<String> features = new Counter<String>();
            double wordLength = characters.length;
            
            for (int endIndex = 1; endIndex < wordLength; endIndex++) {
                for (int nGram = 1; nGram < 5; nGram++) {
                    int beginIndex = endIndex - nGram;
                    if (beginIndex >= 0) {
                        features.incrementCount("pre_" + name.substring(beginIndex, endIndex), 1);
                    }
                }
            }



            ////////////////////////////////////////
			if(name.contains(":")) {
				features.setCount("containsColon", 1.0);
			}
			String[] arr = name.split(" ");
			String prev_word = "<S>";
			String pre_pre_word = "<S>";
			int ind = 0;
			for(String s: arr) {
				String pre;
				if(ind < arr.length-1) {
					pre = "LAST_";
				}
				else if(ind == 0) {
          			pre = "FIRST_";
				}
				else {
					pre = "";
				}
				StringBuilder input1 = new StringBuilder();
				input1.append(s);
				// features.incrementCount(pre+"Last5"+String.format("%."+ 5 +"s", input1.reverse()), 1.0);
				features.incrementCount(pre+"Last4"+String.format("%."+ 4 +"s", input1.reverse()), 1.0);
				features.incrementCount(pre+"Last3"+String.format("%."+ 3 +"s", input1.reverse()), 1.0);
				features.incrementCount(pre+"Last2"+String.format("%."+ 2 +"s", input1.reverse()), 1.0);
				features.incrementCount(pre+"first3"+String.format("%."+ 3 +"s", input1) ,1.0);

        		features.incrementCount("BIGRAM-"+prev_word+"_"+s, 1.0);
				features.incrementCount("TRIGRAM-"+pre_pre_word+"_"+prev_word+"_"+s, 1.0);
				features.incrementCount("UNI-"+s, 1.0);
				pre_pre_word = prev_word;
				prev_word = s;
				ind += 1;
			}
        	return features;
        }
        
        //////////// Arnav ////////////
        // char[] characters = name.toCharArray();
		// 	Counter<String> features = new Counter<String>();
		// 	// add character unigram features
		// 	//System.out.println(name);
		// 	/*for (int i = 0; i < characters.length; i++) {
		// 		char character = characters[i];
		// 		features.incrementCount("UNI-" + character, 1.0);
		// 	}*/
		// 	for (int i = 1; i < characters.length; i++) {
		// 		features.incrementCount("Bx-" + characters[i-1] + characters[i], 1.0);
		// 	}
		// 	for (int i = 2; i < characters.length; i++) {
		// 		features.incrementCount("Tx-" + characters[i-2] + characters[i-1] + characters[i], 1.0);
		// 	}
		// 	for (int i = 3; i < characters.length; i++) {
		// 		features.incrementCount("Qx-" + characters[i-3] + characters[i-2] + characters[i-1] + characters[i], 1.0);
		// 	}
		// 	for (int i = 4; i < characters.length; i++) {
		// 		features.incrementCount("Px-" + characters[i-4] + characters[i-3] + characters[i-2] + characters[i-1] + characters[i], 1.0);
		// 	}
		// 	/*for (int i = 5; i < characters.length; i++) {
		// 		features.incrementCount("HEXGRAM-" + characters[i-5] + characters[i-4] + characters[i-3] + characters[i-2] + characters[i-1] + characters[i], 1.0);
		// 	}*/

		// 	/*double l1 = 5; double l2 = 5;
		// 	for (int i = 2; i < characters.length; i++) {
		// 		features.incrementCount("BTx-" + characters[i-2] + characters[i-1] + characters[i], l1*(features.getCount("Bx-"+characters[i-1] + characters[i-2])) + l2*(features.getCount("Tx-"+characters[i-2] + characters[i-1] + characters[i])));
		// 	}*/
		// 	//features.incrementCount("NW-", numWordsInSentence(name));

		// 	//features.incrementCount("F1-", numCharsFirstWord(name));
		// 	features.incrementCount("FC&-",firstCapital(name));
		// 	//features.incrementCount("WL-", name.length());

		// 	//double lword = 0;
		// 	//if(numWordsInSentence(name) > 1)
		// 	//	lword = numCharsLastWord(name);

		// 	//features.incrementCount("L1-", lword);

		// 	//if(companyTermsPresent(name))
		// 	//	features.incrementCount("C&", 1.0);

		// 	if(numberPresent(name))
		// 		features.incrementCount("N&",1.0);


		// 	/*if(endsInExclaimationMark(name))
		// 		features.incrementCount("E&",1.0);*/

		// 	features.incrementCount("HY&",hyphenCount(name));
		// 	//Set<String> feats = new HashSet<>(features.keySet());

		// 	String[] ttemp = new String[]{"S1-","S2-","S3-", "S4-", "S5-","S6-", "S7-", "S8-", "S9-", "S10-"};//, "S6-"};
		// 	String temp = new String();
		// 	for(int i=characters.length-1; i >= Math.max(0,characters.length-ttemp.length); i--){
		// 		//System.out.println("x1");
		// 		temp += characters[i];
		// 		features.incrementCount(ttemp[characters.length- 1 - i]+temp,1.0);
		// 		//System.out.println("x2");
		// 	}

		// 	String[] ttemp2 = new String[]{"P1-","P2-","P3-", "P4-", "P5-","P6-", "P7-","P8-","P9-","P10-"};//, "P6-"};//;
		// 	String temp2 = new String();
		// 	for(int i=0; i < Math.min(ttemp2.length,characters.length);i++){
		// 		//System.out.println("x3");
		// 		temp2 += characters[i];
		// 		features.incrementCount(ttemp2[i]+temp2,1.0);
		// 		//System.out.println("x4");
		// 	}
		// 	//System.out.println(features.size());
		// 	//int ct = 0;

		// 	/*for(String feat1: feats){
		// 		//System.out.println("done " + ct + " out of " + feats.size());
		// 		//ct++;
		// 		double temp = features.getCount(feat1);
		// 		features.incrementCount(feat1,temp*temp);
		// 		//for(String feat2: feats){
		// 		//	if(!(feat1.equals(feat2))){
		// 		//		features.incrementCount("Q-"+feat1+feat2,features.getCount(feat1)*features.getCount(feat2));
		// 		//	}
		// 		//}
		// 	}*/

		// 	// TODO : extract better features!
		// 	// TODO
		// 	// TODO
		// 	// TODO
		// 	return features;
    // }

    // private double hyphenCount(String name) {
    //     return name.chars().filter(num -> num == '-').count();
    // }

}
