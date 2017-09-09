package com.tool.weixin.tool;

public class Configure {
	private static String key = "MkEpzhKaMoNHbh7idwKpiGjboKxT7YtD";
	//小程序ID
	private static String appID = "wxe743be01a8f86a82";
	//商户号
	private static String mch_id = "1467216202";
	//秘钥
	private static String secret = "72300eef79bf17ee4aedb8e67ef2366d";

	public static String getSecret() {
		return secret;
	}

	public static void setSecret(String secret) {
		Configure.secret = secret;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static String getAppID() {
		return appID;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static String getMch_id() {
		return mch_id;
	}

	public static void setMch_id(String mch_id) {
		Configure.mch_id = mch_id;
	}

}
