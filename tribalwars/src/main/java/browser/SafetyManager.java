package browser;

import org.jsoup.nodes.Document;

public class SafetyManager {

	public static void checkCaptcha(Document document) throws CaptchaException {
		// TODO check captcha

		/*
		 * <div id="bot_check"> <h2>Botschutz</h2>
		 * 
		 * <div id="bot_check_error"
		 * style="color:red; font-size:large; display: none"></div> <img
		 * id="bot_check_image" src="/human.php?s=75ad99cc1c26&amp;small"
		 * "="" alt=""><br><br> <form id="bot_check_form" method="post"
		 * action=""> Gib die Nummern und Buchstaben in das Textfeld ein: <input
		 * id="bot_check_code" type="text" name="code" style="width: 70px">
		 * <input id="bot_check_submit" class="btn" type="submit"
		 * value="Weiter"> </form> </div>
		 */
	}

	public static void checkSession(Document document) throws SessionException {
		// TODO check session
	}

}
