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

/**
 * This is the main harness for assignment 2. To run this harness, use
 * <p/>
 * java nlp.assignments.ProperNameTester -path ASSIGNMENT_DATA_PATH -model
 * MODEL_DESCRIPTOR_STRING
 * <p/>
 * First verify that the data can be read on your system using the baseline
 * model. Second, find the point in the main method (near the bottom) where a
 * MostFrequentLabelClassifier is constructed. You will be writing new
 * implementations of the ProbabilisticClassifer interface and constructing them
 * there.
 */
public class ProperNameTester {

	public static class ProperNameFeatureExtractor implements
			FeatureExtractor<String, String> {

		/**
		 * This method takes the list of characters representing the proper name
		 * description, and produces a list of features which represent that
		 * description. The basic implementation is that only character-unigram
		 * features are extracted. An easy extension would be to also throw
		 * character bigrams into the feature list, but better features are also
		 * possible.
		 */
		private double hyphenCount(String name) {
			return name.chars().filter(num -> num == '-').count();
		}
		private int numWordsInSentence(String name){
			String trim = name.trim();
			if(trim.isEmpty())
				return 0;
			return trim.split("\\s+").length;
		}

		private int numCharsFirstWord(String name){
			String trim = name.trim();
			if(trim.isEmpty())
				return 0;
			return trim.split("\\s+")[0].length();
		}

		private int numCharsLastWord(String name){
			String trim = name.trim();
			if(trim.isEmpty())
				return 0;
			String[] t = trim.split("\\s+");
			return t[t.length-1].length();
		}

		private boolean numberPresent(String name){
			return name.matches(".*\\d+.*");
		}

		private boolean endsInExclaimationMark(String name){
			return name.matches(".*[.!?]");
		}

		private boolean companyTermsPresent(String name){
			String temp = name.toLowerCase();
			return temp.contains("Inc".toLowerCase()) || temp.contains("co") || temp.contains("L.P") || temp.contains("Trust") || temp.contains("Fund");
		}
		private int firstCapital(String name){
			if(Character.isUpperCase(name.charAt(0)))
				return 10;
			return 0;
		}
		public Counter<String> extractFeatures(String name) {
			// char[] characters = name.toCharArray();
            // Counter<String> features = new Counter<String>();
            // double wordLength = characters.length;
            
            // for (int endIndex = 1; endIndex < wordLength; endIndex++) {
            //     for (int nGram = 1; nGram < 5; nGram++) {
            //         int beginIndex = endIndex - nGram;
            //         if (beginIndex == 0) {
            //             features.incrementCount("pre_" + name.substring(beginIndex, endIndex), 1);
            //         }
			// 	}
			// 	if (endIndex ==  wordLength - 1) {
			// 		for (int nGram = 1; nGram < 5; nGram++) {
			// 			int beginIndex = endIndex - nGram;
			// 			if (beginIndex >= 0) {
			// 				features.incrementCount("suf_" + name.substring(beginIndex, endIndex), 1);
			// 			}
			// 		}
			// 	}
            // }
			
			// if(name.contains(":")) {
			// 	features.setCount("colon", 1);
			// }

			// if(name.contains("-")) {
			// 	features.setCount("hyphen", 1);
			// }

			// if(Character.isUpperCase(name.charAt(0))) {
			// 	features.setCount("firstCharUpperCase", 1);
			// }
			


            // ////////////////////////////////////////
			
			// String[] arr = name.split(" ");
			// String prev_word = "<S>";
			// String pre_pre_word = "<S>";
			// int ind = 0;
			// for(String s: arr) {
			// 	String pre;
			// 	if(ind < arr.length-1) {
			// 		pre = "LAST_";
			// 	}
			// 	else if(ind == 0) {
          	// 		pre = "FIRST_";
			// 	}
			// 	else {
			// 		pre = "";
			// 	}
			// 	StringBuilder input1 = new StringBuilder();
			// 	input1.append(s);
			// 	// features.incrementCount(pre+"Last5"+String.format("%."+ 5 +"s", input1.reverse()), 1.0);
			// 	features.incrementCount(pre+"Last4"+String.format("%."+ 4 +"s", input1.reverse()), 1.0);
			// 	features.incrementCount(pre+"Last3"+String.format("%."+ 3 +"s", input1.reverse()), 1.0);
			// 	features.incrementCount(pre+"Last2"+String.format("%."+ 2 +"s", input1.reverse()), 1.0);
			// 	features.incrementCount(pre+"first3"+String.format("%."+ 3 +"s", input1) ,1.0);

        	// 	features.incrementCount("BIGRAM-"+prev_word+"_"+s, 1.0);
			// 	features.incrementCount("TRIGRAM-"+pre_pre_word+"_"+prev_word+"_"+s, 1.0);
			// 	features.incrementCount("UNI-"+s, 1.0);
			// 	pre_pre_word = prev_word;
			// 	prev_word = s;
			// 	ind += 1;
			// }
			// return features;
			
			char[] characters = name.toCharArray();
				Counter<String> features = new Counter<String>();
				// add character unigram features
				//System.out.println(name);
				/*for (int i = 0; i < characters.length; i++) {
					char character = characters[i];
					features.incrementCount("UNI-" + character, 1.0);
				}*/
				for (int i = 1; i < characters.length; i++) {
					features.incrementCount("Bx-" + characters[i-1] + characters[i], 1.0);
				}
				for (int i = 2; i < characters.length; i++) {
					features.incrementCount("Tx-" + characters[i-2] + characters[i-1] + characters[i], 1.0);
				}
				for (int i = 3; i < characters.length; i++) {
					features.incrementCount("Qx-" + characters[i-3] + characters[i-2] + characters[i-1] + characters[i], 1.0);
				}
				for (int i = 4; i < characters.length; i++) {
					features.incrementCount("Px-" + characters[i-4] + characters[i-3] + characters[i-2] + characters[i-1] + characters[i], 1.0);
				}
				/*for (int i = 5; i < characters.length; i++) {
					features.incrementCount("HEXGRAM-" + characters[i-5] + characters[i-4] + characters[i-3] + characters[i-2] + characters[i-1] + characters[i], 1.0);
				}*/

				/*double l1 = 5; double l2 = 5;
				for (int i = 2; i < characters.length; i++) {
					features.incrementCount("BTx-" + characters[i-2] + characters[i-1] + characters[i], l1*(features.getCount("Bx-"+characters[i-1] + characters[i-2])) + l2*(features.getCount("Tx-"+characters[i-2] + characters[i-1] + characters[i])));
				}*/
				//features.incrementCount("NW-", numWordsInSentence(name));

				//features.incrementCount("F1-", numCharsFirstWord(name));
				features.incrementCount("FC&-",firstCapital(name));
				//features.incrementCount("WL-", name.length());

				//double lword = 0;
				//if(numWordsInSentence(name) > 1)
				//	lword = numCharsLastWord(name);

				//features.incrementCount("L1-", lword);

				//if(companyTermsPresent(name))
				//	features.incrementCount("C&", 1.0);

				if(numberPresent(name))
					features.incrementCount("N&",1.0);


				/*if(endsInExclaimationMark(name))
					features.incrementCount("E&",1.0);*/

				features.incrementCount("HY&",hyphenCount(name));
				//Set<String> feats = new HashSet<>(features.keySet());

				String[] ttemp = new String[]{"S1-","S2-","S3-", "S4-", "S5-","S6-", "S7-", "S8-", "S9-", "S10-"};//, "S6-"};
				String temp = new String();
				for(int i=characters.length-1; i >= Math.max(0,characters.length-ttemp.length); i--){
					//System.out.println("x1");
					temp += characters[i];
					features.incrementCount(ttemp[characters.length- 1 - i]+temp,1.0);
					//System.out.println("x2");
				}

				String[] ttemp2 = new String[]{"P1-","P2-","P3-", "P4-", "P5-","P6-", "P7-","P8-","P9-","P10-"};//, "P6-"};//;
				String temp2 = new String();
				for(int i=0; i < Math.min(ttemp2.length,characters.length);i++){
					//System.out.println("x3");
					temp2 += characters[i];
					features.incrementCount(ttemp2[i]+temp2,1.0);
					//System.out.println("x4");
				}
				//System.out.println(features.size());
				//int ct = 0;

				/*for(String feat1: feats){
					//System.out.println("done " + ct + " out of " + feats.size());
					//ct++;
					double temp = features.getCount(feat1);
					features.incrementCount(feat1,temp*temp);
					//for(String feat2: feats){
					//	if(!(feat1.equals(feat2))){
					//		features.incrementCount("Q-"+feat1+feat2,features.getCount(feat1)*features.getCount(feat2));
					//	}
					//}
				}*/

				// TODO : extract better features!
				// TODO
				// TODO
				// TODO
				return features;
		}

	}

	private static List<LabeledInstance<String, String>> loadData(
			String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		List<LabeledInstance<String, String>> labeledInstances = new ArrayList<LabeledInstance<String, String>>();
		while (reader.ready()) {
			String line = reader.readLine();
			String[] parts = line.split("\t");
			String label = parts[0];
			String name = parts[1];
			LabeledInstance<String, String> labeledInstance = new LabeledInstance<String, String>(
					label, name);
			labeledInstances.add(labeledInstance);
		}
		reader.close();
		return labeledInstances;
	}

	private static void testClassifier(
			ProbabilisticClassifier<String, String> classifier,
			List<LabeledInstance<String, String>> testData, boolean verbose) {
		double numCorrect = 0.0;
		double numTotal = 0.0;
		for (LabeledInstance<String, String> testDatum : testData) {
			String name = testDatum.getInput();
			String label = classifier.getLabel(name);
			double confidence = classifier.getProbabilities(name).getCount(
					label);
			if (label.equals(testDatum.getLabel())) {
				numCorrect += 1.0;
			}
				if (verbose) {
					// display an error
					System.err.println("Example: " + name + " guess=" + label
							+ " gold=" + testDatum.getLabel() + " confidence="
							+ confidence);
			
			}
			numTotal += 1.0;
		}
		double accuracy = numCorrect / numTotal;
		// comment when using verbose
		System.out.println("Accuracy: " + accuracy);
	}

	public static void main(String[] args) throws IOException {
		// Parse command line flags and arguments
		Map<String, String> argMap = CommandLineUtils
				.simpleCommandLineParser(args);

		// Set up default parameters and settings
		String basePath = ".";
		String model = "baseline";
		boolean verbose = false;
		boolean useValidation = true;

		// Update defaults using command line specifications

		// The path to the assignment data
		if (argMap.containsKey("-path")) {
			basePath = argMap.get("-path");
		}
		System.out.println("Using base path: " + basePath);

		// A string descriptor of the model to use
		if (argMap.containsKey("-model")) {
			model = argMap.get("-model");
		}
		System.out.println("Using model: " + model);

		// A string descriptor of the model to use
		if (argMap.containsKey("-test")) {
			String testString = argMap.get("-test");
			if (testString.equalsIgnoreCase("test"))
				useValidation = false;
		}
		System.out.println("Testing on: "
				+ (useValidation ? "validation" : "test"));

		// Whether or not to print the individual speech errors.
		if (argMap.containsKey("-verbose")) {
			verbose = true;
		}

		// Load training, validation, and test data
		List<LabeledInstance<String, String>> trainingData = loadData(basePath
				+ "/pnp-train.txt");
		List<LabeledInstance<String, String>> validationData = loadData(basePath
				+ "/pnp-validate.txt");
		List<LabeledInstance<String, String>> testData = loadData(basePath
				+ "/pnp-test.txt");

		// Learn a classifier
		ProbabilisticClassifier<String, String> classifier = null;
		if (model.equalsIgnoreCase("baseline")) {
			classifier = new MostFrequentLabelClassifier.Factory<String, String>()
					.trainClassifier(trainingData);
		} else if (model.equalsIgnoreCase("n-gram")) {
			// TODO: construct your n-gram model here
		} else if (model.equalsIgnoreCase("maxent")) {
			// TODO: construct your maxent model here
			ProbabilisticClassifierFactory<String, String> factory = new MaximumEntropyClassifier.Factory<String, String, String>(
					1.0, 20, new ProperNameFeatureExtractor());
			classifier = factory.trainClassifier(trainingData);
		} else {
			throw new RuntimeException("Unknown model descriptor: " + model);
		}

		// Test classifier
		testClassifier(classifier, (useValidation ? validationData : testData),
				verbose);
	}
}
