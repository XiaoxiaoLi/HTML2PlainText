import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * To parse HTML into plain text for text analysis
 * 
 * @author Xiaoxiao Li
 * 
 */
public class PlainTextParser {
	public static String html2PlainText(String html) {
		String result = "";

		// Clean HTML tags
		result = cleanTagPerservingLineBreaks(result);
		// Unescape HTML eg. &lt; to < with StringEscapeUtils
		result = StringEscapeUtils.unescapeHtml4(result);
		// Remove URLs
		result = removeUrl(result);
		// Remove extended chars
		result = removeExtendedChars(result);

		return result;
	}

	/**
	 * Clean HTML tags while preserving the line breaks using JSoup
	 * 
	 * Credit: http://stackoverflow.com/a/19602313/1830564
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
	 * Remove extended chars
	 * 
	 * this version removes all chars above 127
	 * 
	 * @param str
	 * @return
	 */
	public static String removeExtendedChars(String str) {
		return str.replaceAll("[^\\x00-\\x7F]", " ");
	}

	/**
	 * Remove URLs from text
	 * 
	 * Credit: http://stackoverflow.com/a/5713697/1830564
	 * 
	 * Sometimes when doing text analysis, we don't want the urls
	 * 
	 * @param str
	 * @return
	 */
	public static String removeUrl(String str) {
		String regex = "\\b(https?|ftp|file|telnet|http|Unsure)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		str = str.replaceAll(regex, "");
		return str;
	}

	public static void main(String[] argh) {
		System.out.println(PlainTextParser.html2PlainText("a<br>b"));
	}
}