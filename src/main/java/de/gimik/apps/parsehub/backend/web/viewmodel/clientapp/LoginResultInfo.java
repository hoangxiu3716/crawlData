package de.gimik.apps.parsehub.backend.web.viewmodel.clientapp;

import java.util.List;

import de.gimik.apps.parsehub.backend.web.viewmodel.TokenInfo;
public class LoginResultInfo {
	private TokenInfo tokenInfo;

	
	public LoginResultInfo() {
		super();
	}
	public LoginResultInfo(TokenInfo tokenInfo) {
		super();
		this.tokenInfo = tokenInfo;
	}
	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}
	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}


}
