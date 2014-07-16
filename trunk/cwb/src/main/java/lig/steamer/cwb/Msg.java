package lig.steamer.cwb;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Msg {
	private static final String BUNDLE_NAME = "lig.steamer.cwb.msg.msg";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Msg() {
	}

	public static String get(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
