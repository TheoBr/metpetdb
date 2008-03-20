package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.BulkUploadService;
import edu.rpi.metpetdb.server.bulk.upload.sample.SampleParser;

public class BulkUploadServiceImpl extends SampleServiceImpl implements
		BulkUploadService {
	private static final long serialVersionUID = 1L;

	public String validate(final String fileOnServer)
			throws InvalidFormatException {

		try {
			final SampleParser sp = new SampleParser(new FileInputStream(
					fileOnServer));
			sp.initialize();

			final Set<SampleParser.Index> cell_errors = new HashSet<SampleParser.Index>();
			final Set<Integer> col_errors = new HashSet<Integer>();
			final Set<Integer> row_errors = new HashSet<Integer>();

			final List<List<String>> output = sp.validate(cell_errors,
					col_errors, row_errors);
			return output.toString();

		} catch (final FileNotFoundException fnfe) {
			throw new IllegalStateException(fnfe.getMessage());
			// throw new IOException();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
			// throw new IOException();
		} catch (final Exception e) {
			throw new InvalidFormatException();
		}
	}

	public String saveSamplesFromSpreadsheet(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		int savedSamples = 0;
		try {
			final SampleParser sp = new SampleParser(new FileInputStream(
					fileOnServer));
			sp.initialize();

			sp.parse();
			final Set<SampleDTO> samples = sp.getSamples();
			final UserDTO u = (UserDTO) cloneBean(byId("User", currentUser()));
			for (final SampleDTO s : samples) {
				try {
					System.out.println("Saving Sample" + s.toString());
					s.setOwner(u);
					System.out.println("check");
					super.save(s);
					System.out.println("check");
					savedSamples++;
				} catch (final SampleAlreadyExistsException saee) {
					System.err.println(saee.getMessage());
					// TODO: what to do?
				} catch (final ValidationException ve) {
					System.err.println("Validation Exception!");
					// TODO: what to do?
				} catch (final LoginRequiredException lre) {
					System.out.println("uncheck");
					throw lre;
				}
			}
		} catch (final FileNotFoundException fnfe) {
			throw new IllegalStateException(fnfe.getMessage());
		} catch (final NoSuchObjectException nsoe) {
			throw new LoginRequiredException();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return savedSamples + " samples saved.";
	}
}
