package nl.hugojanssen.sqlgraph.visitors;

import gudusoft.gsqlparser.ETableSource;
import gudusoft.gsqlparser.TBaseType;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.nodes.TTableList;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;
import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.apache.log4j.Logger;

/**
 * Labels all tables in a SQL statement as SOURCE or TARGET, depending on their role in the statement. This applies to
 * from and joins only. Tables that are used in i.e. where conditions are not considered relevant for data lineage. The
 * following statement types are currently supported: <br/>
 * <br/>
 * - INSERT <br/>
 * - UPDATE <br/>
 * 
 * @author hjanssen
 */
public class TableVisitor extends /*TParseTreeVisitor*/SQLVisitor
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( TableVisitor.class );

	/** Is the next table a SOURCE or TARGET table? */
	private EClauseType currRole;

	/**
	 * @param aInterface
	 */
	public TableVisitor( VisitorListener aInterface )
	{
		super( aInterface );
		//		this.parent = aInterface;
	}

	@Override
	public void preVisit( TInsertSqlStatement statement )
	{
		this.currRole = EClauseType.TARGET;
	}

	@Override
	public void preVisit( TJoin join )
	{
		int type = join.getKind();

		switch ( type )
		{
			case TBaseType.join_source_fake:
				//				LOG.info( "FAKE" );
				//				LOG.info( join );
				join.getTable().accept( this );
				break;
			case TBaseType.join_source_join:
				//				LOG.info( "SOURCE J" );
				//				LOG.info( join );
				break;
			case TBaseType.join_source_table:
				//				LOG.info( "SOURCE T" );
				//LOG.info( join );

				join.getTable().accept( this );

				//join.getJoinItems().accept( this );

				break;
			default:
				LOG.warn( "Unexpected join type: " + type + ", " + join );
				break;
		}
	}

	@Override
	public void preVisit( TJoinItem joinItem )
	{
		//		LOG.info( "PRE joinItem" );
		int kind = joinItem.getKind();

		switch ( kind )
		{
			case TBaseType.join_source_table:
				//				LOG.info( "joinItem ST" );
				//LOG.info( joinItem );
				joinItem.getTable().accept( this );
				break;
			case TBaseType.join_source_join:
				//				LOG.info( "joinItem SJ" );
				//joinItem.getJoin().accept( this );
				break;
			default:
				LOG.warn( "Unknown TJoinItem kind: " + kind );
				break;
		}
	}

	@Override
	public void preVisit( TSelectSqlStatement statement )
	{
		this.currRole = EClauseType.SOURCE;
	}

	@Override
	public void preVisit( TTable table )
	{
		//		LOG.debug( "TABLE: " + table.getTableType() );

		ETableSource type = table.getTableType();

		switch ( type )
		{
			case objectname:
				this.addParseResult( table );
				break;
			case subquery:
				table.getSubquery().accept( this );
				break;
			default:
				LOG.warn( "Unsupported TTable type: " + type );
				break;
		}
	}

	public void analyzeTableList( TTableList tableList )
	{
		this.currRole = EClauseType.SOURCE;
		for ( int i = 1; i < tableList.size(); i++ )
		{
			tableList.getTable( i ).accept( this );
		}
	}

	@Override
	public void preVisit( TUpdateSqlStatement statement )
	{
		this.currRole = EClauseType.TARGET;
	}

	@Override
	public void preVisit( TCreateTableSqlStatement statement )
	{
		this.currRole = EClauseType.TARGET;
		TTable target = statement.getTargetTable();
		target.accept( this );
	}

	/*
	@Override
	public void preVisit( TCreateIndexSqlStatement statement )
	{
		LOG.debug( "Skip TCreateIndexSqlStatement on table " + statement.getTableName() );
		//	statement.accept( null );
	}

	@Override
	public void preVisit( TDropIndexSqlStatement statement )
	{
		LOG.warn( "Skip TDropIndexSqlStatement on table " + statement.getIndexName() );
	}
	*/

	@Override
	public void postVisit( TUpdateSqlStatement statement )
	{
		this.analyzeTableList( statement.tables );
	}

	/**
	 * Creates a new ParseResult from the TTable object and passes it to the parent interface.
	 * 
	 * @param table
	 */
	private void addParseResult( TTable table )
	{
		ParseResult result = new ParseResult( this, table.getPrefixSchema(), table.getName(), this.currRole, table.getLineNo(), table.getColumnNo() );
		LOG.debug( "### " + result );
		super.getListener().update( result );
	}
}
