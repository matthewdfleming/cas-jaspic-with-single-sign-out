package com.googlecode.cas.jaspic.soap;

import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;

import java.util.Map;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ClientAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;

@SuppressWarnings({ "rawtypes", "unused" })
public class CasClientAuthModule implements ClientAuthModule {

	private static final String WSDL_SERVICE_INFO_KEY = "javax.xml.ws.wsdl.service";

	private static final Class[] supportedMessageTypes = new Class[]{ SOAPMessage.class };

	private static Logger logger = Logger.getLogger(CasClientAuthModule.class
			.getName());

	private MessagePolicy requestPolicy;
	private MessagePolicy responsePolicy;
	private CallbackHandler handler;
	private Map options;

	private LoginContext lc;

	public CasClientAuthModule() {
	}

	public CasClientAuthModule(String str) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.message.module.ServerAuthModule#getSupportedMessageTypes
	 * ()
	 */
	public Class[] getSupportedMessageTypes() {
		return supportedMessageTypes;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.message.ClientAuth#secureRequest(javax.security.auth
	 * .message.MessageInfo, javax.security.auth.Subject)
	 */
	public AuthStatus secureRequest(MessageInfo arg0, Subject arg1)
			throws AuthException {
		return SEND_SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.message.ServerAuth#cleanSubject(javax.security.auth
	 * .message.MessageInfo, javax.security.auth.Subject)
	 */
	public void cleanSubject(MessageInfo msgInfo, Subject subject)
			throws AuthException {
		if (subject != null) {
			subject.getPrincipals().clear();
		}
		if (this.lc != null) {
			try {
				this.lc.logout();
			} catch (LoginException e) {
				logger.throwing(CasClientAuthModule.class.getName(),
						"cleanSubject", e);
				AuthException ae = new AuthException();
				ae.initCause(e);
				throw ae;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.message.module.ServerAuthModule#initialize(javax.
	 * security.auth.message.MessagePolicy,
	 * javax.security.auth.message.MessagePolicy,
	 * javax.security.auth.callback.CallbackHandler, java.util.Map)
	 */
	public void initialize(MessagePolicy requestPolicy,
			MessagePolicy responsePolicy, CallbackHandler handler, Map options)
			throws AuthException {
		this.requestPolicy = requestPolicy;
		this.responsePolicy = responsePolicy;
		this.handler = handler;
		this.options = options;
		if (options != null) {
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.message.ClientAuth#validateResponse(javax.security
	 * .auth.message.MessageInfo, javax.security.auth.Subject,
	 * javax.security.auth.Subject)
	 */
	public AuthStatus validateResponse(MessageInfo msgInfo, Subject clientSubject,
			Subject serverSubject) throws AuthException {
		return SUCCESS;
	}

}
