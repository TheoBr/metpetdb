package edu.rpi.metpetdb.client.error.validation;

import java.util.ArrayList;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class InvalidMetamorphicGradeException extends ValidationException {

	private static final long serialVersionUID = 1L;
	private String rockType;
	private ArrayList<String> metamorphicGrades;

	public InvalidMetamorphicGradeException() {

	}

	public InvalidMetamorphicGradeException(final String rockType,
			final ArrayList<String> rockTypes) {
		this.rockType = rockType;
		this.metamorphicGrades = rockTypes;
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidMetamorphicGrade(rockType,
				format(this.metamorphicGrades));
	}

	private String format(final ArrayList<String> rockTypes) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < metamorphicGrades.size(); ++i) {

			sb.append(metamorphicGrades.get(i));
			if (i != metamorphicGrades.size() - 1)
				sb.append(", ");
		}
		return sb.toString();
	}
}