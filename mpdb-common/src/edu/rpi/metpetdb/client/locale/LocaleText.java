package edu.rpi.metpetdb.client.locale;

import com.google.gwt.i18n.client.Messages;

/** All message text constants, to support i18n. */
public interface LocaleText extends Messages {
	/* Menus */
	String homeMenu();
	String mySamplesMenu();
	String mySamplesMenu_AllMySamples();
	String mySamplesMenu_NewestSamples();
	String mySamplesMenu_FavoriteSamples();
	String mySamplesMenu_EnterSample();
	String projectMenu();
	String projectsMenu_NewProject();
	String projectsMenu_MyProjects();
	String projectsMenu_EnterSample();
	String searchMenu();
	String searchMenu_AllPublicSamples();
	String searchMenu_TestSearch();
	String newsMenu();
	String aboutMenu();
	String aboutMenu_Introduction();
	String aboutMenu_Wiki();
	String aboutMenu_VersionControl();
	String peopleMenu();
	String faqMenu();
	String wikiMenu();

	/* Samples */
	String addSample();
	String addSampleDescription();

	/* Subsamples */
	String addSubsample();
	String addSubsampleDescription(String sampleAlias);

	/* Mineral Analyses */
	String addChemicalAnalysis();

	/* Projects */
	String addProject();
	String addProjectDescription();

	/* Search */
	String search();

	/* Tools */
	String tools_EditProfile();

	/* Notices */
	String notice_Welcome();
	String notice_PasswordChanged(String username);
	String notice_GridSaved(String subsample);

	/* Title Bars */
	String title_PleaseLogin();
	String title_RegisterAccountInfo();
	String title_ChangeAccountPassword(String username);
	String title_ObtainPassword();
	String title_UploadImage();

	/* Basic UI */
	String buttonClose();
	String buttonCancel();
	String buttonLogin();
	String buttonLogout();
	String buttonRegister();
	String buttonEmailPassword();
	String buttonChangePassword();
	String buttonSave();
	String buttonEdit();
	String buttonDelete();
	String tab_Login();
	String tab_ForgotPassword();
	String buttonUploadImage();
	String buttonUploadSpreadsheet();
	String buttonSubmit();
	String buttonContinue();

	/* Errors */
	String errorTitle_UnknownError();
	String errorDesc_UnknownError();
	String errorTitle_NoObject();
	String errorDesc_NoObject(String type, String id);
	String errorTitle_InvalidData();
	String errorDesc_Required(String field);
	String errorDesc_TooShort(String field, int min);
	String errorDesc_TooLong(String field, int max);
	String errorDesc_IsoDateFormat(String field);
	String errorDesc_WrongType(String field);
	String errorDesc_InvalidFloat(String field);
	String errorDesc_InvalidGeometry(String field);
	String errorDesc_InvalidSESARNumber(String field);
	String errorDesc_InvalidCharacter(String field, String accepted, String bad);
	String errorDesc_InvalidLogin();
	String errorDesc_NotEqual(String fieldA, String fieldB);
	String errorDesc_NotAfter(String fieldA, String fieldB);
	String errorDesc_Duplicate(String field, String taken);
	String errorDesc_UnableToSendEmail();
	String errorDesc_InvalidRockType(String rockType, String rockTypes);
	String errorDesc_InvalidFormat();
	String errorDesc_InvalidDateString();
	String errorDesc_InvalidLongitude(String field);
	String errorDesc_InvalidLatitude(String field);

	/* Character Names */
	String character_space();
	String character_period();
	String character_comma();
	String character_singleQuote();
	String character_doubleQuote();
	String character_underscore();
	String character_backtick();

	/* Other Messages */
	String message_WhyRegister();
	String message_NewPasswordSet();
}
