package edu.rpi.metpetdb.client.ui.excel;

import java.util.ArrayList;
import java.util.List;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.MpDb;

public class ExcelUtil {
	final public static String[] columnHeaders = {
		LocaleHandler.lc_entity.Sample_number(),
		LocaleHandler.lc_entity.Sample_publicData(),
		LocaleHandler.lc_entity.Sample_subsampleCount(),
		LocaleHandler.lc_entity.Sample_imageCount(),
		LocaleHandler.lc_entity.Sample_analysisCount(),
		LocaleHandler.lc_entity.Sample_owner(),
		LocaleHandler.lc_entity.Sample_regions(),
		LocaleHandler.lc_entity.Sample_country(),
		LocaleHandler.lc_entity.Sample_rockType(),
		LocaleHandler.lc_entity.Sample_metamorphicGrades(),
		LocaleHandler.lc_entity.Sample_minerals(),
		LocaleHandler.lc_entity.Sample_references(),
		LocaleHandler.lc_entity.Sample_latitude(),
		LocaleHandler.lc_entity.Sample_longitude(),
		LocaleHandler.lc_entity.Sample_sesarNumber(),
		LocaleHandler.lc_entity.Sample_collector(),
		LocaleHandler.lc_entity.Sample_collectionDate(),
		LocaleHandler.lc_entity.Sample_locationText()
	};
	final public static String columnHeaderParameter = "column";
	
	final public static List<String> chemColumnHeaders = new ArrayList<String>();
	
	static
	{
		

		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_sampleName());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_subsampleName());
				
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_spotId());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_mineral());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_method());
										
		chemColumnHeaders.add(LocaleHandler.lc_entity.Subsample_subsampleType());
												
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_analyst());
			
	/*	chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_amount());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_precisionType());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_measurementUnit());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_minAmount());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_maxAmount());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_name());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_symbol());
		chemColumnHeaders.add(LocaleHandler.lc_entity. ChemicalAnalysis_ElementOrOxide_atomicNumber());
		chemColumnHeaders.add(LocaleHandler.lc_entity.ChemicalAnalysis_ElementOrOxide_weight()); */
		
		
	}
	
}
