package edu.rpi.metpetdb.server.impl;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import edu.rpi.metpetdb.client.service.RecaptchaRemoteService;
import edu.rpi.metpetdb.server.MpDbServlet;

public class RecaptchaRemoteServiceImpl extends MpDbServlet implements RecaptchaRemoteService
{

	
	private Boolean VerifyChallenge(String remoteAddress, String challenge, String response)
	{
		//TODO: Externalize Recaptcha key  SCB
		ReCaptcha r = ReCaptchaFactory.newReCaptcha("6LeCaL0SAAAAAD-dKyj9t3PTOqdW8j9svbfHn9P2",
				"6LeCaL0SAAAAAH8GxuMBrXBZLFZ0EQdBRbl_Wr2Q", true);
		return r.checkAnswer(remoteAddress, challenge,
				response).isValid();
	
	}
	
	public  Boolean verifyChallenge(String challenge, String response) {
		String remoteAddress = getThreadLocalRequest().getRemoteAddr().toString();
		
		return this.VerifyChallenge(remoteAddress, challenge, response);
	}
	
}