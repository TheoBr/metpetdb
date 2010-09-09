package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.bulk.upload.InvalidReferenceFormatException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadReferencesService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadReferencesServiceImpl extends BulkUploadService implements
	BulkUploadReferencesService {

	@Override
	public void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao, GeoReferenceDAO geoDao,
			Map<String, Collection<String>> subsampleNames,
			Map<String, Sample> samples, Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		
		throw new RuntimeException("Not implemented.");
	}

	/**
	 * 
	 * This parsing is by necessity very hard coded. If there is a change in the way
	 * these text files are produced, this function will likely need changing.
	 * 
	 */
	private ArrayList<GeoReference> parseGeoRefs(
			Map<String, String> geoRefUnparsedMap, 
			BulkUploadResult results) {
		ArrayList<GeoReference> parsedGeoRefs = new ArrayList<GeoReference>();
		
		Iterator itr = geoRefUnparsedMap.entrySet().iterator();
		int rowNumber = 0; //Not really row numbers, but something to keep track of what file we're on
		while(itr.hasNext()){
			Map.Entry pairs = (Map.Entry)itr.next();
			String unparsedText = (String) pairs.getValue();
			
			//Set the reference number using the Map Key
			GeoReference geoRef = new GeoReference();
			geoRef.setReferenceNumber((String) pairs.getKey());
			
			//Find each of the GeoRef properties in the unparsed text
			int titleIndex = unparsedText.indexOf("TI- ");
			int firstAuthorIndex = unparsedText.indexOf("AU- ");
			int secondAuthorsIndex = unparsedText.indexOf("AU- ", firstAuthorIndex+1);
			int journalNameIndex = unparsedText.indexOf("JN- ");
			
			//If any of these strings are not found, there's a problem with this file's format
			//Add an error and skip this file
			rowNumber++;
			if(titleIndex == -1 || firstAuthorIndex == -1 || journalNameIndex == -1){
				InvalidReferenceFormatException e = 
					new InvalidReferenceFormatException("Error parsing file: " + geoRef.getReferenceNumber());
				results.addError(rowNumber, e);
				continue;
			}
			geoRef.setTitle(unparsedText.substring(titleIndex+4, 
					unparsedText.indexOf('\n',titleIndex)-1));
			geoRef.setFirstAuthor(unparsedText.substring(firstAuthorIndex+4, 
					unparsedText.indexOf('\n',firstAuthorIndex)-1));
			geoRef.setSecondAuthors(unparsedText.substring(secondAuthorsIndex+4, 
					unparsedText.indexOf('\n',secondAuthorsIndex)-1));
			geoRef.setJournalName(unparsedText.substring(journalNameIndex+4, 
					unparsedText.indexOf('\n',journalNameIndex)-1));
			
			//Get fulltext
			geoRef.setFullText(unparsedText.substring(titleIndex,
					unparsedText.indexOf('\n',unparsedText.indexOf("UR- "))));
			
			parsedGeoRefs.add(geoRef);
		}
		
		return parsedGeoRefs;
	}

	private String getTextFromFile(ZipFile zip, ZipEntry refFile) throws IOException {
		InputStream instream = zip.getInputStream(refFile);
		//Build a string from the InputStream by reading one line at a time
		if(instream != null){
			StringBuilder sb = new StringBuilder();
			String line;
			
			try{
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream, "UTF-8"));
				while((line = reader.readLine()) != null){
					sb.append(line).append("\n");
				}
			} finally {
				instream.close();
			}
			return sb.toString();
		} else {
			throw new IOException();
		}
	}

	private ArrayList<ZipEntry> getReferenceNames(FileInputStream is) throws IOException{
		ArrayList<ZipEntry> refFileEntries = new ArrayList<ZipEntry>();		
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ent;
		while ((ent = zis.getNextEntry()) != null) {
			String entryName = ent.getName();
			String[] entryNameSplit = entryName.split("/");
			// Ignore any subdirectories
			if (entryNameSplit.length > 1){
				continue;
			}
			
			// Ignore anything that is in a' hidden' directory
			for (String s : entryNameSplit) {
				if (s.startsWith("__") || entryName.startsWith("."))
					continue;
			}
			
			//Otherwise treat it as a reference
			//TODO are there any other oddities that may need checking?
			refFileEntries.add(ent);		
		}
		
		return refFileEntries;
	}

	public BulkUploadResult imageZipUploadImpl(String spreadsheetFile, String imageFile,
			boolean save) throws InvalidFormatException,
			LoginRequiredException, MpDbException {
		return null;
	}

}
