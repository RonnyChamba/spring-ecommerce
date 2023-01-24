package com.curso.ecommerce.editors;

import java.beans.PropertyEditorSupport;

public class FirstLetterUpperCase extends PropertyEditorSupport {

	private String typeText = "";

	public FirstLetterUpperCase(String typeText) {
		this.typeText = typeText;

	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		String newValue = "";

		switch (typeText.toLowerCase()) {

		case "upper":
			newValue = text.toUpperCase();
			break;

		case "lower":
			newValue = text.toLowerCase();
			break;
		case "capitalize":
			String lowerCase = text.toLowerCase();
			char mayus = lowerCase.charAt(0);
			newValue = String.valueOf(mayus).toUpperCase().concat(lowerCase.substring(1, lowerCase.length()));
			break;

		default:
			newValue = text;
			break;
		}
		setValue(newValue);

	}

}
