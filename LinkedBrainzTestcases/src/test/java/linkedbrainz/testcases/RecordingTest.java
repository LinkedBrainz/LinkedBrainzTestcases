package linkedbrainz.testcases;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class RecordingTest
{

	/**
	 * Fetches 5 recordings from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordings()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"recording", "gid", "mo:Signal", "RecordingsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	/**
	 * Fetches 5 recordings from the DB and resolves their names against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingNames()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("recording");
		classTables.add("track_name");

		classTableRows.add("name");
		classTableRows.add("name");

		// add "Lucy in the Sky With Diamonds" from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:Signal", "dct:title", "title", 1,
						5, false, false,
						"eb9bf15c-29e8-4c6b-bfa1-9b2a5b33a5b6",
						"RecordingNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 recordings from the DB and resolves their ISRC codes against
	 * the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingISRCs()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("isrc");
		classTables.add("recording");

		classTableRows.add("recording");
		classTableRows.add("isrc");

		// add "Lucy in the Sky With Diamonds" from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheRight(classTables,
						classTableRows, "mo:Signal", "mo:isrc", "ISRC", 5,
						false, false, "eb9bf15c-29e8-4c6b-bfa1-9b2a5b33a5b6",
						"RecordingISRCsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) recordings and their music artists from the DB and
	 * resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingMusicArtistsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("recording");
		classTables.add("artist");
		classTables.add("artist_credit");
		classTables.add("artist_credit_name");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");
		classTableRows.add("artist");

		classNames.add("mo:Signal");
		classNames.add("mo:MusicArtist");

		valueNames.add("recordingURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add "Lucy in the Sky With Diamonds" from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDs(classTables, classTableRows,
						classNames, "foaf:maker", valueNames, null, 3, 5,
						"eb9bf15c-29e8-4c6b-bfa1-9b2a5b33a5b6",
						"RecordingsArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) recordings(s) and its tracks from the DB and resolves them
	 * via a SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingTracksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("recording");
		classTables.add("track");

		classTableRows.add("gid");
		classTableRows.add("id");
		classTableRows.add("recording");

		classNames.add("mo:Signal");
		classNames.add("mo:Track");

		valueNames.add("recordingURI");
		valueNames.add("trackURI");
		valueNames.add("trackURI");

		// add "Lucy in the Sky With Diamonds" from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDOnTheLeftAndIDOnTheRight(
						classTables, classTableRows, classNames,
						"mo:published_as", valueNames, "#_", 1, 5,
						"eb9bf15c-29e8-4c6b-bfa1-9b2a5b33a5b6",
						"RecordingsTracksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) recordings from the DB and resolves their PUIDs against
	 * the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkRecordingsPUIDs()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("recording");
		classTables.add("recording_puid");
		classTables.add("puid");

		classTableRows.add("puid");
		classTableRows.add("recording");
		classTableRows.add("puid");

		// add "Lucy in the Sky With Diamonds" from The Beatles as proof
		// GUID
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft(classTables,
						classTableRows, "mo:Signal", "mo:puid", "puid", 2,
						5, false, true, "eb9bf15c-29e8-4c6b-bfa1-9b2a5b33a5b6",
						"RecordingsPUIDsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(RecordingTest.class);
	}
}
