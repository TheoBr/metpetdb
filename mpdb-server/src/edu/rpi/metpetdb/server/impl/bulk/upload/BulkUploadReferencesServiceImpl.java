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

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
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
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			Map<String, Collection<String>> subsampleNames,
			Map<String, Sample> samples, Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		final BulkUploadResultCount resultCount = new BulkUploadResultCount();
		
		//Get the names of all the reference files
		FileInputStream is = new FileInputStream(MpDbServlet.getFileUploadPath()
				+ fileOnServer);
		ArrayList<ZipEntry> refFileEntries;		
		try{
			refFileEntries = getReferenceNames(is);
		} catch (IOException ioe) {
			throw new GenericDAOException(ioe.getMessage());
		}
		
		//Get the zip file stored on the server
		ZipFile zip;
		try{
			zip = new ZipFile(MpDbServlet.getFileUploadPath() + fileOnServer);
		} catch (IOException ioe) {
			throw new GenericDAOException(ioe.getMessage());
		}

		//Map GeoRef names to their unparsed text
		//Stripping off the .txt extension leaving just the reference name
		//ex: 1962-009249.txt -> 1962-009249
		Map<String,String> geoRefUnparsedMap = new HashMap<String,String>();
		final Iterator<ZipEntry> ent = refFileEntries.iterator();
		while(ent.hasNext()){
			final ZipEntry refFile = ent.next();
			String unparsedRefText;
			
			try {
				unparsedRefText = getTextFromFile(zip, refFile);
			} catch (IOException ioe) {
				throw new GenericDAOException(ioe.getMessage());
			}
			
			//Get the name of this GeoRef
			int dotIndex = refFile.getName().lastIndexOf('.');
			String geoName;
			//Filename's starting in '.' should have been filtered out, but check anyway
			if(dotIndex > 0){
				geoName = refFile.getName().substring(0,dotIndex);
			} else{
				//This was a hidden file. This should never happen because it's filtered out earlier
				resultCount.incrementInvalid();
				continue;
			}
			
			geoRefUnparsedMap.put(geoName, unparsedRefText);
		}
		
		ArrayList<GeoReference> parsedGeoRefs = parseGeoRefs(geoRefUnparsedMap);
		
		//For each GeoReference, find samples that it belongs to
		Iterator<GeoReference> itr = parsedGeoRefs.iterator();
		while(itr.hasNext()){
			GeoReference geoRef = itr.next();
			ArrayList<Sample> sampleList = (ArrayList<Sample>) sampleDao.getSamplesForReference(geoRef.getReferenceNumber());
			//For each sample found, link it to this GeoRef
			Iterator<Sample> sampleItr = sampleList.iterator();
			while(sampleItr.hasNext()){
				Sample s = sampleItr.next();
				Set<GeoReference> currentRefs = s.getGeoReferences();
				//If the sample already has this geoRef, do nothing
				Iterator<GeoReference> geoItr = currentRefs.iterator();
				boolean old = false;
				GeoReference currentRef = null;
				while(geoItr.hasNext()){
					currentRef = geoItr.next();
					if(currentRef.getReferenceNumber().equals(geoRef.getReferenceNumber())){
						old = true;
						break;
					}
				}
				if(!old){
					currentRefs.add(geoRef);
					s.setGeoReferences(currentRefs);
					sampleDao.save(s);
					resultCount.incrementFresh();
				} else {
					//replace the existing reference with this number with the new one
					if(currentRef != null){
						currentRefs.remove(currentRef);
						currentRefs.add(geoRef);
						s.setGeoReferences(currentRefs);
						sampleDao.save(s);
						resultCount.incrementOld();
					}
				}
			}	
		}
		
		results.addResultCount("GeoReference", resultCount);
	}

	/**
	 * 
	 * This parsing is by necessity very hard coded. If there is a change in the way
	 * these text files are produced, this function will likely need changing.
	 * 
	 */
	private ArrayList<GeoReference> parseGeoRefs(
			Map<String, String> geoRefUnparsedMap) {
		ArrayList<GeoReference> parsedGeoRefs = new ArrayList<GeoReference>();
		
		Iterator itr = geoRefUnparsedMap.entrySet().iterator();
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
}
