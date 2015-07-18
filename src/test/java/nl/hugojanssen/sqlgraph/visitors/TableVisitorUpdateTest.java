package nl.hugojanssen.sqlgraph.visitors;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TableVisitorUpdateTest
{
	TableVisitorTestHelper helper = new TableVisitorTestHelper();

	@BeforeTest
	public void setup()
	{
		File file = new File( this.getClass().getResource( "/scripts/valid/tables/UpdateTests.sql" ).getFile() );
		this.helper.getParseResults( file );
	}

	@Test( description = "Tests update statements with referenced table" )
	public void testReferencedUpdate()
	{
		this.helper.testParseResult( 1, "table1", 5, EClauseType.TARGET );
		this.helper.testParseResult( 2, "table2", 6, EClauseType.SOURCE );
	}

	@Test( description = "Tests update statements with multiple referenced tables" )
	public void testReferencedUpdate2()
	{
		this.helper.testParseResult( 3, "table1", 9, EClauseType.TARGET );
		this.helper.testParseResult( 4, "table2", 10, EClauseType.SOURCE );
		this.helper.testParseResult( 5, "table3", 10, EClauseType.SOURCE );
	}

	@Test( description = "Tests update statements referenced table and where condition" )
	public void testReferencedWithWhere()
	{
		this.helper.testParseResult( 6, "table1", 13, EClauseType.TARGET );
		this.helper.testParseResult( 7, "table2", 14, EClauseType.SOURCE );
	}

	@Test( description = "Tests size of the resultset" )
	public void testResultSet()
	{
		assertThat( this.helper.getResultSetSize() ).isEqualTo( 8 );
	}

	@Test( description = "Tests update statements with one target table" )
	public void testSimpleUpdate()
	{
		this.helper.testParseResult( 0, "table1", 2, EClauseType.TARGET );
	}
}
