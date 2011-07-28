package linkedbrainz.testcases;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import linkedbrainz.testcases.model.Condition;
import linkedbrainz.testcases.model.TestResult;

import org.junit.Test;

/**
 * 
 * @author zazi
 * 
 */
public class MediumTest
{
	/**
	 * Fetches 5 mediums (mo:Record instances) from the DB and resolves it via a
	 * SPARQL query.
	 * 
	 * ATTENTION: requires to switch off the resultSizeLimit
	 * 
	 */
	@Test
	public void checkMediums()
	{
		TestResult testResult = Utils.getInstance()
				.checkClassViaGUIDOrIDAndFragmentId("medium", "id",
						"mo:Record", "#_", 5, "MediumsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());

	}

	/**
	 * Fetches 5 mediums (mo:Record instances) from the DB and resolves their
	 * track counts against the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMediumsTrackCount()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();

		classTables.add("medium");
		classTables.add("tracklist");

		classTableRows.add("tracklist");
		classTableRows.add("track_count");

		// add track list from "Sgt. Pepper’s Lonely Hearts Club Band" (PMC
		// 7027) from The
		// Beatles as proof id (see 44b7cab1-0ce1-404e-9089-b458eb3fa530 for the
		// related release)
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaIDOnTheLeft(classTables, classTableRows,
						"mo:Record", "mo:track_count", "trackCount", "#_", 1,
						5, false, false, "93", "MediumsTrackCountCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) mediums (mo:Record instances) from the DB and resolves
	 * their media types against the result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkMediumsMediaTypes()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("medium");
		classTables.add("medium_format");

		classTableRows.add("id");
		classTableRows.add("id");
		classTableRows.add("format");

		classNames.add("mo:Record");

		valueNames.add("reocordURI");
		valueNames.add("mediaTypeURI");
		valueNames.add("mediaTypeURI");

		// add track list from "Sgt. Pepper’s Lonely Hearts Club Band" (PMC
		// 7027) from The
		// Beatles as proof id (see 44b7cab1-0ce1-404e-9089-b458eb3fa530 for the
		// related release)
		TestResult testResult = Utils
				.getInstance()
				.checkURIPropertyViaIDOnTheLeftAndURIOnTheRight(
						classTables,
						classTableRows,
						classNames,
						"mo:media_type",
						valueNames,
						"#_",
						1,
						5,
						new Condition(
								"linkedbrainz.d2rs.translator.MediumTranslator"),
						"93", "MediumsMediaTypesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 1 (+1) medium(s) and its tracks from the DB and resolves them via
	 * a SPARQL query.
	 * 
	 */
	@Test
	public void checkMediumsTracksRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("medium");
		classTables.add("track");
		classTables.add("tracklist");

		classTableRows.add("id");
		classTableRows.add("id");
		classTableRows.add("tracklist");
		classTableRows.add("tracklist");

		classNames.add("mo:Record");
		classNames.add("mo:Track");

		valueNames.add("recordURI");
		valueNames.add("trackURI");
		valueNames.add("trackURI");

		Utils utils = Utils.getInstance();

		// let's start hacking ;)
		// this option initialises another SQL query pattern where two table
		// columns are in the FK position of third table's identifier one
		utils.setOptionThree(true);

		// add track list from "Sgt. Pepper’s Lonely Hearts Club Band" (PMC
		// 7027) from The
		// Beatles as proof id (see 44b7cab1-0ce1-404e-9089-b458eb3fa530 for the
		// related release)
		TestResult testResult = utils
				.checkURIPropertyViaIDOnTheLeftAndIDOnTheRight(classTables,
						classTableRows, classNames, "mo:track", valueNames,
						"#_", "#_", 2, 1, "93", "MediumsTracksRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(MediumTest.class);
	}
}
