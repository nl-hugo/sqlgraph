package nl.hugojanssen.sqlgraph.visitors;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TableVisitorCreateTableAsTest
{
	TableVisitorTestHelper helper = new TableVisitorTestHelper();

	@BeforeTest
	public void setup()
	{
		File file = new File( this.getClass().getResource( "/scripts/valid/tables/CreateTableAsTests.sql" ).getFile() );
		this.helper.getParseResults( file );
	}

	@Test( description = "Creates films_recent table" )
	public void testCreateFilmsRecent()
	{
		this.helper.testParseResult( 0, "films_recent", 5, EClauseType.TARGET );
		this.helper.testParseResult( 1, "films", 6, EClauseType.SOURCE );
	}

	@Test( enabled = false, description = "Creates films_recent table with prepared statement" )
	public void testCreateFilmsRecentWithPrepared()
	{
		this.helper.testParseResult( 2, "films", 11, EClauseType.SOURCE );
		this.helper.testParseResult( 3, "films_recent", 12, EClauseType.TARGET );
	}

	@Test( enabled = false, description = "Creates table with left join" )
	public void testCreateTableWithLeftJoin()
	{
		//		this.helper.testParseResult( 0, "films", 5, EClauseType.TARGET );
		//		this.helper.testParseResult( 0, "films", 5, EClauseType.TARGET );
		//		this.helper.testParseResult( 0, "films", 5, EClauseType.TARGET );
	}

	@Test( description = "Tests size of the resultset" )
	public void testResultSet()
	{
		assertThat( this.helper.getResultSetSize() ).isEqualTo( 5 );
	}
}
