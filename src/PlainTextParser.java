import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * To parse different kinds of input text into plain text.
 * 
 * @author Xiaoxiao Li
 * 
 */
public class PlainTextParser {
	public static String html2PlainText(String html) {
		String result = "";

		// Clean HTML tags
		result = cleanTagPerservingLineBreaks(html);
		// Unescape HTML eg. &lt; to <
		result = org.apache.commons.lang3.StringEscapeUtils
				.unescapeHtml4(result);
		// Remove URLs
		result = removeUrl(result);
		// Remove extended chars
		result = removeExtendedChars(result);

		return result;
	}

	/**
	 * Clean HTML tags while preserving the line breaks
	 * 
	 * 1. if the original html contains newline(\n), it gets preserved
	 * 
	 * 2. if the original html contains br or p tags, they gets translated to
	 * newline(\n).
	 * 
	 * @param html
	 * @return
	 */
	public static String cleanTagPerservingLineBreaks(String html) {
		String result = "";
		// result = Jsoup.clean(html, "", Whitelist.none(),
		// new OutputSettings().prettyPrint(false));
		if (html == null)
			return html;
		Document document = Jsoup.parse(html);
		document.outputSettings(new Document.OutputSettings()
				.prettyPrint(false));// makes html() preserve linebreaks and
										// spacing
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		result = document.html().replaceAll("\\\\n", "\n");
		result = Jsoup.clean(result, "", Whitelist.none(),
				new Document.OutputSettings().prettyPrint(false));
		return result;
	}

	/**
	 * Remove extended chars TODO: just remove unwanted chars, check format
	 * String, this version removes all chars above 127 like i in Zaïane!
	 * 
	 * @param str
	 * @return
	 */
	public static String removeExtendedChars(String str) {
		// return str.replaceAll("[^\\x21-\\x7E]", " ");
		return str.replaceAll("[^\\x00-\\x7F]", " ");
	}

	/**
	 * When doing text analysis, we don't want the urls, this method removes
	 * them from text.
	 * 
	 * @param str
	 * @return
	 */
	public static String removeUrl(String str) {
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		str = str.replaceAll(regex, "");

		return str;
	}

	public static void main(String[] argh) throws IOException {
		PlainTextParser parser = new PlainTextParser();
		System.out.println(parser.html2PlainText("a<br>b"));
	}
}