package nl.hugojanssen.sqlgraph.model;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ParseResultTest
{
	private String name = "table1", schema = "schema1";

	private ParseResult table1, table2;

	@BeforeTest
	public void setup()
	{
		this.table1 = new ParseResult( this, this.schema, this.name, EClauseType.SOURCE, -1, -1 );
		this.table2 = new ParseResult( this, "", "table2", null, -1, -1 );
	}

	@Test( description = "Test getColNo" )
	public void testGetColNo()
	{
		assertThat( this.table1.getColNo() ).isEqualTo( -1 );
	}

	@Test( description = "Test getId" )
	public void testGetId()
	{
		assertThat( this.table1.getId() ).isEqualTo( this.schema + "." + this.name );
	}

	@Test( description = "Test getLineNo" )
	public void testGetLineNo()
	{
		assertThat( this.table1.getLineNo() ).isEqualTo( -1 );
	}

	@Test( description = "Test getName" )
	public void testGetName()
	{
		assertThat( this.table1.getName() ).isEqualTo( this.name );
	}

	@Test( description = "Test getRole" )
	public void testGetRole()
	{
		assertThat( this.table1.getRole() ).isEqualTo( EClauseType.SOURCE );
	}

	@Test( description = "Test getSchema" )
	public void testGetSchema()
	{
		assertThat( this.table1.getSchema() ).isEqualTo( this.schema );
	}

	@Test( description = "Test hashCode" )
	public void testHashCode()
	{
		assertThat( this.table1.hashCode() ).isEqualTo( 364251310 );
	}

	@Test( description = "Test getToString" )
	public void testToString()
	{
		assertThat( this.table1.toString() ).isNotNull();
	}

	@Test( description = "Test validateState" )
	public void testValidateState()
	{
		assertThat( this.table1.equals( null ) ).isFalse();
		assertThat( this.table1.equals( this.table2 ) ).isFalse();
		assertThat( this.table1.equals( Boolean.TRUE ) ).isFalse();
		assertThat( this.table1.equals( 1L ) ).isFalse();
		assertThat( this.table1.equals( new Date() ) ).isFalse();

		assertThat( this.table2.equals( this.table1 ) ).isFalse();

		assertThat( this.table1.equals( this.table1 ) ).isTrue();

		try
		{
			new ParseResult( this, "test", "", null, -1, -1 );
		}
		catch ( IllegalArgumentException e )
		{
			assertThat( e.getMessage() ).isEqualTo( "Name must be non-null and non-empty." );
		}
	}
}
