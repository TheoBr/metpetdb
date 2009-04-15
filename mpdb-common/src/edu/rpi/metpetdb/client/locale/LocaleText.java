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
	String sample();
	String addSample();
	String addSampleDescription();
	String publicDataWarning();
	String noSamplesFound();

	/* Subsamples */
	String subsamples();
	String addSubsample();
	String addSubsampleDescription(String sampleNumber);
	String noSubsamplesFound();

	/* Mineral Analyses */
	String addChemicalAnalysis();
	String noChemicalAnalysesFound();

	/* Projects */
	String addProject();
	String addProjectDescription();
	String noProjectsFound();

	/* Comments */
	String comments();
	
	/* Google Earth */
	String googleEarth_GoToSample();

	/* Search */
	String search();
	String search_exportExcel();
	String search_viewGoogleEarth();
	String search_exportKML();
	String search_exportResults();
	String search_noSamplesFound();
	String search_noChemicalAnalysesFound();

	/* Tools */
	String tools_EditProfile();

	/* Notices */
	String notice_Welcome();
	String notice_PasswordChanged(String username);
	String notice_InfoChanged(String username);
	String notice_GridSaved(String Subsample);
	String notice_AccountDisabled();

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
	String buttonUpdateInfo();
	String buttonSave();
	String buttonEdit();
	String buttonDelete();
	String tab_Login();
	String tab_ForgotPassword();
	String buttonUploadImage();
	String buttonUploadSpreadsheet();
	String buttonSubmit();
	String buttonContinue();
	String buttonExportExcel();
	String buttonExportKML();
	String listBoxSelect();
	String buttonYes();

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
	String errorDesc_InvalidBoolean(String field);
	String errorDesc_InvalidShort(String field);
	String errorDesc_InvalidFloat(String field);
	String errorDesc_InvalidInteger(String field);
	String errorDesc_InvalidGeometry(String field);
	String errorDesc_InvalidSESARNumber(String field);
	String errorDesc_InvalidCharacter(String field, String accepted, String bad);
	String errorDesc_InvalidLogin();
	String errorDesc_InvalidImage(String val);
	String errorDesc_NotEqual(String fieldA, String fieldB);
	String errorDesc_NotAfter(String fieldA, String fieldB);
	String errorDesc_Duplicate(String field, String taken);
	String errorDesc_UnableToSendEmail();
	String errorDesc_InvalidImageType(String field);
	String errorDesc_InvalidSubsampleType(String value);
	String errorDesc_InvalidRockType(String rockType, String rockTypes);
	String errorDesc_InvalidFormat();
	String errorDesc_InvalidDateString();
	String errorDesc_InvalidLongitude(String field);
	String errorDesc_InvalidLatitude(String field);
	String errorTitle_UnexpectedError(String throwable);
	String errorDesc_ValueNotInCollection(String property, String value, String collection);
	String errorDesc_TimeExpired();

	/* DAO Exceptions */
	String errorDesc_ChemicalAnalysisAlreadyExists();
	String errorDesc_ChemicalAnalysisNotFound();
	String errorDesc_ElementNotFound();
	String errorDesc_FunctionNotImplemented();
	String errorDesc_GenericDAO(String msg);
	String errorDesc_GridNotFound();
	String errorDesc_ImageAlreadyExists();
	String errorDesc_ImageNotFound();
	String errorDesc_MetamorphicGradeNotFound();
	String errorDesc_MineralNotFound();
	String errorDesc_ProjectAlreadyExists();
	String errorDesc_ProjectNotFound();
	String errorDesc_ReferenceNotFound();
	String errorDesc_RegionNotFound();
	String errorDesc_SampleAlreadyExists();
	String errorDesc_SampleNotFound();
	String errorDesc_SampleNotFoundId(Long id);
	String errorDesc_SampleNotFoundNumber(String number);
	String errorDesc_SubsampleAlreadyExists();
	String errorDesc_SubsampleNotFound();
	String errorDesc_UserAlreadyExists();
	String errorDesc_UserNotFound();
	String errorDesc_SampleCommentNotFound();

	/* Character Names */
	String character_space();
	String character_period();
	String character_comma();
	String character_singleQuote();
	String character_doubleQuote();
	String character_underscore();
	String character_backtick();

	/* Permission */
	String Permission_Denied();
	String Permission_NotYours();
	String Account_Not_Enabled();
	String Not_Owner();
	String Modify_Public_Data();
	String Cannot_Load_Private_Data();
	String Cannot_Load_Public_Data();
	String Cannot_Load_Other_Users();
	String Cannot_Load_Pending_Roles();
	String Cannot_Save_Data();

	/* User Samples List */
	String cannotDeletePublicSamples();

	/* Other Messages */
	String message_WhyRegister();
	String message_NewPasswordSet();
	String confirmation_MakePublic();
	String confirmation_Delete();
	String confirmation_AddToProject();
	String message_ChooseSamples();
	String message_resetForm();
	
	/* Misc */
	String tokenSeparater();
	
	/* Bulk Upload */
	String bulkUpload_Upload();
	String bulkUpload_SubmitData();
	String bulkUpload_Desc();
	String bulkUpload_Help();
	String bulkUpload_Samples();
	String bulkUpload_ChemicalAnalyses();
	String bulkUpload_Images();
	String bulkUpload_LooksGoodSoCommit();
}
