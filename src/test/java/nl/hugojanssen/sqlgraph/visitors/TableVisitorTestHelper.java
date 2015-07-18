package nl.hugojanssen.sqlgraph.visitors;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.model.sql.EClauseType;
import nl.hugojanssen.sqlgraph.model.sql.SQLScript;
import nl.hugojanssen.sqlgraph.model.sql.SQLWorkflow;
import nl.hugojanssen.sqlgraph.visitors.TableVisitor;
import nl.hugojanssen.sqlgraph.visitors.VisitorListener;

import org.fest.assertions.Fail;

public class TableVisitorTestHelper implements VisitorListener
{
	private SQLWorkflow workflow;

	private ArrayList<ParseResult> parseResults;

	public TableVisitorTestHelper()
	{
	}

	@Override
	public void update( ParseResult obj )
	{
		this.parseResults.add( obj );
	}

	public int getResultSetSize()
	{
		return this.parseResults.size();
	}

	public void getParseResults( File aFile )
	{
		this.workflow = new SQLWorkflow();
		this.workflow.addVisitor( new TableVisitor( this ) );
		this.parseResults = new ArrayList<ParseResult>();
		try
		{
			this.workflow.addWorkflowFile( aFile );
			this.workflow.parse();
		}
		catch ( IOException e )
		{
			Fail.fail( e.getMessage() );
		}
	}

	public void testParseResult( int idx, String name, int lineNo, EClauseType type )
	{
		ParseResult result = this.parseResults.get( idx );
		assertThat( result.getName() ).isEqualTo( name );
		assertThat( result.getLineNo() ).isEqualTo( lineNo );
		assertThat( result.getRole() ).isEqualTo( type );
	}

	@Override
	public void setLocation( SQLScript aFile, long aIndex )
	{
		// Omitted
	}
}
