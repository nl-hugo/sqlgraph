package nl.hugojanssen.sqlgraph.visitors;

import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.model.graph.ParseResultNode;
import nl.hugojanssen.sqlgraph.model.graph.SQLGraphModel;
import nl.hugojanssen.sqlgraph.model.sql.EClauseType;
import nl.hugojanssen.sqlgraph.model.sql.SQLScript;

import org.apache.log4j.Logger;

public class TableVisitorListener implements VisitorListener
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( TableVisitorListener.class );

	private SQLScript currFile;

	private ParseResultNode target;

	private long statementIdx;

	private List<ParseResultNode> sources = new ArrayList<ParseResultNode>();

	public TableVisitorListener()
	{
	}

	@Override
	public void update( ParseResult aResult )
	{

		String name = "UNKNOWN";
		if ( this.currFile != null )
		{
			name = this.currFile.getName();
		}

		if ( this.validateParseResult( aResult ) )
		{

			System.out.println( name + " -- " + aResult.getSource().getClass().getSimpleName() + " -- " + this.statementIdx + " -- "
				+ aResult.getName() + " -- " + aResult.getRole() );

			EClauseType role = aResult.getRole();

			switch ( role )
			{
				case SOURCE:
					this.addSource( aResult );
					break;
				case TARGET:
					this.setTarget( aResult );
					break;
				default:
					break;
			}
		}
	}

	private boolean validateParseResult( ParseResult aResult )
	{
		boolean result = true;
		if ( aResult == null )
		{
			LOG.warn( "Found ParseResult object 'null' in file " + this.currFile.getName() );
			result = false;
		}
		else if ( !aResult.getSource().getClass().equals( TableVisitor.class ) )
		{
			//			LOG.warn( "Not listening to class " + aResult.getSource().getClass() );
			result = false;
		}
		else
		{
			result = this.validateRole( aResult.getName(), aResult.getRole() );
		}
		return result;
	}

	private boolean validateRole( String aName, EClauseType aRole )
	{
		boolean result = true;
		if ( aRole == null )
		{
			LOG.warn( "Ignoring " + aName + ", object has no role." );
			result = false;
		}
		else if ( !aRole.equals( EClauseType.SOURCE ) && !aRole.equals( EClauseType.TARGET ) )
		{
			LOG.warn( "Ignoring " + aName + ", unknown role " + aRole );
			result = false;
		}
		return result;
	}

	private void setTarget( ParseResult aResult )
	{
		if ( this.target == null )
		{
			this.target = new ParseResultNode( aResult );
		}
		else
		{
			LOG.warn( "Target already set to " + this.target.getName() + "; ignoring " + aResult.getName() );
		}
	}

	private void addSource( ParseResult aResult )
	{
		if ( !this.sources.contains( aResult ) )
		{
			this.sources.add( new ParseResultNode( aResult ) );
		}
		else
		{
			LOG.warn( "Source already present: " + aResult.getName() );
		}
	}

	private void clear()
	{
		LOG.debug( "Clearing parse results for file " + this.currFile );

		// make sure to write results to graph before clearing!
		this.currFile = null;
		this.sources.clear();
		this.target = null;
	}

	@Override
	public void setLocation( SQLScript aFile, long aIndex )
	{
		// location has changed, so update the graph
		LOG.info( "~~~ LOCATION CHANGE: " + aFile.getName() + ", stmt " + aIndex );

		this.updateGraph();

		this.currFile = aFile;
		this.statementIdx = aIndex;
	}

	private void updateGraph()
	{
		LOG.info( this.toString() );

		if ( this.target != null )
		{
			SQLGraphModel.getInstance().addGraphElements( this.currFile.getName(), this.target, this.sources );
		}
		else
		{
			LOG.warn( "No target, skipping " + this.currFile + " -- " + this.statementIdx );
		}
		// clear results once graph is updated
		this.clear();
	}

	@Override
	public String toString()
	{
		String result = this.currFile + " -- " + this.statementIdx + " \nTARGET=\n\t" + this.target;

		result += "\nSOURCES=";
		for ( int i = 0; i < this.sources.size(); i++ )
		{
			result += "\n\t" + this.sources.get( i );
		}
		return result;
	}
}
