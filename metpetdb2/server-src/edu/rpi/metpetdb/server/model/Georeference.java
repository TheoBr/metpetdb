package edu.rpi.metpetdb.server.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gwt.validation.client.NotNull;

import edu.rpi.metpetdb.server.Foo;

@Entity
@Table(name = "georeference")
public class Georeference {

	private Long id;
	
	private Long referenceId;
	
	private Reference reference;
	@NotNull
	private String referenceNumber;
	@NotNull
	private String title;
	@NotNull
	private String firstAuthor;
	private String secondAuthors;
	@NotNull
	private String journalName;
	@NotNull
	private String fullText;
	private String doi;
	private String journalName2;

	private Pattern myPattern = Pattern
			.compile(
					"(MO-|MA-|MF-|NR-|AF-|TI-|AU-|JN-|SO-|PU-|PD-|LA-|ES-|SU-|NO-|PT-|CD-|IS-|RS-|UC-|AN-|UR-|CY-|ES-|AB-|AV-|DO-|BL-)+",
					Pattern.MULTILINE);

	public Georeference() {
	}

	public Georeference(String referenceEntry) {

		this.fullText = "Empty";
		Matcher myMatch = myPattern.matcher(referenceEntry);

		
		int beginCapture = 0;
		int endCapture = 0;
		String groupCapture = null;

		if (myMatch.find()) {
			beginCapture = myMatch.end();
			groupCapture = myMatch.group();
		}

		while (myMatch.find()) {
			endCapture = myMatch.start();
			this.add(groupCapture, referenceEntry.subSequence(beginCapture,
					endCapture).toString().trim());
			beginCapture = endCapture + 4;
			groupCapture = myMatch.group();
		}

	}

	@SequenceGenerator(sequenceName = "georeference_seq", name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	public Long getgeoRef_Id() {
		return id;
	}

	public void setgeoRef_Id(Long i) {
		id = i;
	}

	
	
	/*
	@Column(name = "reference_id", nullable = false)
	public Long getReference_Id() {
		return this.referenceId;
	}

	public void setReference_Id(Long referenceId) {
		this.referenceId = referenceId;
	}*/
	
	@Column(name = "doi", nullable = true)
	public String getDOI() {
		return doi;
	}
	
	@Foo(expression = "\\QDO-\\E")
	public void setDOI(String r)
	{
		doi = r;
	}

	@Column(name = "reference_number", nullable = true)
	public String getReferenceNumber() {
		return referenceNumber;
	}

	@Foo(expression = "\\QAN-\\E")
	public void setReferenceNumber(String r) {
		referenceNumber = r;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	@Foo(expression = "\\QTI-\\E")
	public void setTitle(final String t) {
		title = t;
	}

	@Column(name = "first_author", nullable = false)
	public String getFirstAuthor() {
		return firstAuthor;
	}

	@Foo(expression = "\\QAU-\\E")
	public void setFirstAuthor(final String a) {
		if (firstAuthor != null)
			this.setSecondAuthors(a);
		else
			firstAuthor = a;

	}

	@Column(name = "second_authors", nullable = true)
	public String getSecondAuthors() {
		return secondAuthors;
	}

	public void setSecondAuthors(final String a) {
		secondAuthors = a;
	}

	@Column(name = "journal_name", nullable = false)
	public String getJournalName() {
		return journalName;
	}

	@Foo(expression = "\\QSO-\\E|\\QAV-\\E")
	public void setJournalName(final String j) {
		journalName = j;
	}

	@Column(name = "journal_name_2", nullable = false)
	public String getJournalName2() {
		return journalName2;
	}

	@Foo(expression = "\\QJN-\\E")
	public void setJournalName2(final String j) {
		journalName2 = j;
	}
	@Column(name = "full_text", nullable = false)
	public String getFullText() {
		return fullText;
	}

	@Foo(expression = "\\QAB-\\E")
	public void setFullText(final String t) {
		fullText = t;
	}

	public void add(String key, String value)  {
		Method matchedMethod = this.findMethodMatch(key);
		if (matchedMethod != null)
			try {
				matchedMethod.invoke(this, value);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		
	}

	public Method findMethodMatch(String headerName) {

		for (Method currentMethod : Georeference.class.getMethods()) {

			if (currentMethod.getName().startsWith("set")) {

				Annotation[] annotations = currentMethod
						.getDeclaredAnnotations();

				for (Annotation annotation : annotations) {

					if (annotation instanceof Foo) {
						Foo matchAnnotation = (Foo) annotation;

						if (Pattern.compile(matchAnnotation.expression(),
								Pattern.CASE_INSENSITIVE).matcher(headerName)
								.matches())

						{
							return currentMethod;
						}
					}
				}

			}
		}

		return null;

	}

	@Override
	public String toString() {
		return "Georeference [firstAuthor=" + firstAuthor + ", fullText="
				+ fullText + ", journalName=" + journalName
				+ ", referenceNumber=" + referenceNumber + ", secondAuthors="
				+ secondAuthors + ", title=" + title + "]";
	}

	@OneToOne(optional=true, fetch=FetchType.LAZY, targetEntity=edu.rpi.metpetdb.server.model.Reference.class)
	@JoinColumn(name = "reference_Id")

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

}
