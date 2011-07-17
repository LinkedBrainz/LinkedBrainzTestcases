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
public class WorkTest
{
	/**
	 * Fetches 5 works from the DB and resolves them via a SPARQL query.
	 * 
	 */
	@Test
	public void checkWorks()
	{
		TestResult testResult = Utils.getInstance().checkClassViaGUIDSimple(
				"work", "gid", "mo:MusicalWork", "WorksCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 works from the DB and resolves their names against the result
	 * of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkWorkNames()
	{
		TestResult testResult = Utils.getInstance()
				.checkSimplePropertyViaGUIDOnTheLeft("work", "work_name",
						"name", "name", "mo:MusicalWork", "dct:title", "title",
						5, false, "WorkNamesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works from the DB and resolves their aliases against the
	 * result of the related SPARQL query.
	 * 
	 */
	@Test
	public void checkWorkAliases()
	{
		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance().checkInstanceAliases(
				"work", "mo:MusicalWork",
				"00955628-ace0-3873-9ef2-e0e66b203fc3", "WorkAliasesCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	/**
	 * Fetches 5 (+1) works and their music artists from the DB and resolves
	 * them via a SPARQL query.
	 * 
	 * ATTENTION: the 'artist_credit' row in the table 'work' is currently not
	 * used and might be removed in the future (that is why it won't deliver
	 * results at the moment)
	 * 
	 */
	@Test
	public void checkWorkMusicArtistsRelations()
	{
		ArrayList<String> classTables = new ArrayList<String>();
		ArrayList<String> classTableRows = new ArrayList<String>();
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> valueNames = new ArrayList<String>();

		classTables.add("work");
		classTables.add("artist");
		classTables.add("artist_credit");
		classTables.add("artist_credit_name");

		classTableRows.add("gid");
		classTableRows.add("gid");
		classTableRows.add("artist_credit");
		classTableRows.add("artist_credit");
		classTableRows.add("artist");

		classNames.add("mo:MusicalWork");
		classNames.add("mo:MusicArtist");

		valueNames.add("workURI");
		valueNames.add("artistURI");
		valueNames.add("artistGUID");

		// add
		// "Il dissoluto punito, ossia il Don Giovanni, K. 527: Act I, Scene VII. No. 5 Coro "Giovinette,
		// che fate all'amore" (Zerlina, Coro)" as proof GUID
		TestResult testResult = Utils.getInstance()
				.checkURIInversePropertyViaGUIDs(classTables, classTableRows,
						classNames, "foaf:maker", valueNames, 3,
						"00955628-ace0-3873-9ef2-e0e66b203fc3",
						"WorksArtistsRelationsCheck");

		assertTrue(testResult.getFailMsg(), testResult.isSucceeded());
	}

	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(WorkTest.class);
	}

}
