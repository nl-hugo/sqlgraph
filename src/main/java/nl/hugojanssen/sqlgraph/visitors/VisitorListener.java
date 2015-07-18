package nl.hugojanssen.sqlgraph.visitors;

import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.model.sql.SQLScript;

public interface VisitorListener
{
	public void update( ParseResult aResult );

	public void setLocation( SQLScript aFile, long aIndex );
}