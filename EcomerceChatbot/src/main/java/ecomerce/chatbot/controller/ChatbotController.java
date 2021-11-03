package ecomerce.chatbot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ecomerce.chatbot.model.NLPTools;
import ecomerce.chatbot.model.TextMessage;
import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class ChatbotController {

	@Autowired
	NLPTools nlpTools ;
	@PostMapping("/chat")
	@ResponseBody
	@JsonIgnoreProperties(ignoreUnknown = true)
	public TextMessage chat(@RequestBody TextMessage textMessage) throws FileNotFoundException, IOException {
		Map<String, String> questionAnswer = new HashMap<>();
		/*
		 * Define answers for each given category.
		 */
		
			questionAnswer.put("salutations", "Bonjour,Comment je peux vous aider ?");
			//questionAnswer.put("product-inquiry",
				//	"Product is a TipTop mobile phone. It is a smart phone with latest features like touch screen, blutooth etc.");
			questionAnswer.put("demande-de-prix", "prix est $300");
			//questionAnswer.put("conversation-continue", "What else can I help you with?");
			//questionAnswer.put("conversation-complete", "Nice chatting with you. Bbye.");
			
			questionAnswer.put("demande-categorie", "Parfait ! Votre demande concerne : pantalon,T-Shirt....?");
			questionAnswer.put("mon-choix", "Parfait , quelle taille ?");
			questionAnswer.put("ma-taille", "Quelle est votre couleur pr�f�r�e?");
			questionAnswer.put("ma-couleur", "Pour mieux comprendre votre demande et trouver le bon produit, je vous conseil de consulter la page http://www.Shop4Yourself.com/T-shirt/Taille-M , votre facture sera entre 150Dhs ET 250Dhs selon votre chox!");
			
			questionAnswer.put("remerciement", "Nous vous souhaitons de bons achats ");
		
		// Train categorizer model to the training data we created.
	    DoccatModel model = nlpTools.trainCategorizerModel();
		String[] sentences = nlpTools.breakSentences(textMessage.getText());
		String answer = "";
		for (String sentence : sentences) {
			// Separate words from each sentence using tokenizer.
			String[] tokens = nlpTools.tokenizeSentence(sentence);

			// Tag separated words with POS tags to understand their gramatical structure.
			String[] posTags = nlpTools.detectPOSTags(tokens);

			// Lemmatize each word so that its easy to categorize.
			String[] lemmas = nlpTools.lemmatizeTokens(tokens, posTags);

			// Determine BEST category using lemmatized tokens used a mode that we trained
			// at start.
			String category = nlpTools.detectCategory(model, lemmas);

			// Get predefined answer from given category & add to answer.
			answer = answer + " " + questionAnswer.get(category);
		}
		
		
		return new TextMessage("ERRAHALI", answer) ;
	}
}
